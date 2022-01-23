package eu.surething_project.core.rpc_comm.prover;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import eu.surething_project.core.crypto.CertificateAccess;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.database.AsyncDatabaseWriter;
import eu.surething_project.core.database.data.LocationClaimData;
import eu.surething_project.core.database.data.LocationEndorsementData;
import eu.surething_project.core.database.data.LocationProofData;
import eu.surething_project.core.database.data.Pair;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.*;
import eu.surething_project.core.grpc.google.type.LatLng;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

public class LocationProofVerifier {

    private final CryptoHandler cryptoHandler;

    private static final int minEndorsementApproval = 1;

    private LocationCertificateBuilder certificateBuilder;

    private String externalData;

    private AsyncDatabaseWriter databaseWriter;

    public LocationProofVerifier(CryptoHandler cryptoHandler, String verifierId, String externalData,
                                 String certPath, AsyncDatabaseWriter databaseWriter) {
        this.certificateBuilder = new LocationCertificateBuilder(cryptoHandler, verifierId, certPath);
        this.cryptoHandler = cryptoHandler;
        this.externalData = externalData;
        this.databaseWriter = databaseWriter;
    }

    /**
     * @param locationProof
     * @return
     */
    public LocationCertificate verifyLocationProof(SignedLocationProof locationProof)
            throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException,
            KeyStoreException, NoSuchAlgorithmException, BadPaddingException, SignatureException,
            InvalidKeyException, FileNotFoundException, CertificateException, NoSuchProviderException {
        // Get list Endorsements
        List<SignedLocationEndorsement> endorsementList = locationProof.getVerification().getLocationEndorsementsList();
        List<String> endorsementIds = new ArrayList<>();

        // Validated endorsement Data
        List<LocationEndorsement> approvedEndorsements = new ArrayList<>();

        // Get location claim
        LocationClaim claim = locationProof.getVerification().getLocClaim();

        // Prover id
        String proverId = locationProof.getVerification().getLocClaim().getProverId();

        // Get Proof signature data
        Signature sig = locationProof.getProverSignature();
        String cryptoAlg = sig.getCryptoAlgo();
        long nonce = sig.getNonce();
        byte[] certData = sig.getCertificateData().toByteArray();

        // Create Certificate if necessary
        boolean certFileCreate = CertificateAccess.createCertificateFile(externalData, proverId, certData);

        // create Certificate and verify validity
        cryptoHandler.verifyCertificate(proverId);

        for (SignedLocationEndorsement endorsement : endorsementList) {
            boolean isValid = validateLocationEndorsement(endorsement, claim);
            if (isValid) {
                LocationEndorsement locEndorse = endorsement.getEndorsement();
                approvedEndorsements.add(locEndorse);
                endorsementIds.add(locEndorse.getEndorsementId());
            }
        }

        // Verify if there are enough endorsements
        LocationCertificate certificate = endorsementList.size() >= minEndorsementApproval ?
                certificateBuilder.buildCertificate(claim.getClaimId(), endorsementIds, nonce, cryptoAlg) :
                certificateBuilder.buildCertificate(claim.getClaimId(), new ArrayList<>(), nonce, cryptoAlg);

        addVerifierDataToDB(locationProof.getVerification(), approvedEndorsements);

        return certificate;
    }

    /**
     * @param signedLocationEndorsement
     */
    private boolean validateLocationEndorsement(SignedLocationEndorsement signedLocationEndorsement,
                                                LocationClaim claim)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException,
            FileNotFoundException, CertificateException, NoSuchProviderException {
        // Get signed data
        Signature signature = signedLocationEndorsement.getWitnessSignature();
        byte[] signedEndorsement = signature.getValue().toByteArray();
        byte[] certData = signature.getCertificateData().toByteArray();
        String cryptoAlg = signature.getCryptoAlgo();

        // Get Location Endorsement Data
        LocationEndorsement locEndorsement = signedLocationEndorsement.getEndorsement();
        String witnessId = locEndorsement.getWitnessId();

        boolean isValid;

        // Create Certificate if necessary
        boolean certFileCreate = CertificateAccess.createCertificateFile(externalData, witnessId, certData);

        // create Certificate and verify validity
        cryptoHandler.verifyCertificate(witnessId);

        // Verify signed data (With witness public key)
        isValid = cryptoHandler.verifyData(locEndorsement.toByteArray(), signedEndorsement,
                locEndorsement.getWitnessId(), cryptoAlg);

        // Only check data contents after it has been verified
        if (isValid) {
            isValid = checkData(locEndorsement, claim);
        }

        return isValid;
    }

    /**
     * @param locEndorsement
     * @param locClaim
     */
    private boolean checkData(LocationEndorsement locEndorsement, LocationClaim locClaim) {
        String witnessId = locEndorsement.getWitnessId();
        String claimId = locEndorsement.getClaimId();

        // Time - oneof (TIMESTAMP, INTERVAL, RELATIVETOEPOCH, EMPTY)
        Time time = locEndorsement.getTime();
        Time.TimeCase timeCase = time.getTimeCase();
        Timestamp timestamp = null;
        switch (timeCase) {
            case TIMESTAMP -> timestamp = time.getTimestamp();
            case INTERVAL -> time.getInterval();
            case RELATIVETOEPOCH -> time.getRelativeToEpoch();
            case EMPTY -> time.getEmpty();  // check if TIME_NOT_SET will be enough instead of this case
            // case TIME_NOT_SET: break;
        }

        // TODO: Check if timestamp is not old when compared to locClaim

        String evidenceType = locEndorsement.getEvidenceType(); // Evidence Type - WiFi

        // WiFi Networks Evidence details - Any - unpack evidence content
        Any evidence = locEndorsement.getEvidence();
        WiFiNetworksEvidence wifiNetworksEvidence = null;
        try {
            wifiNetworksEvidence = evidence.unpack(WiFiNetworksEvidence.class);
        } catch (InvalidProtocolBufferException e) {
            throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        }

        List<WiFiNetworksEvidence.AP> aps = new ArrayList<>();
        String evidenceId = wifiNetworksEvidence.getId();  // evidenceId
        aps = wifiNetworksEvidence.getApsList();  // list of APs in WiFi evidence
        WiFiNetworksEvidence.AP ap = aps.get(0);  // First AP
        final Timestamp t = timestamp; // timestamp of location endorsement

        // TODO: Check network evidence

        // TODO: What about Location Verification
        return true;
    }

    private void addVerifierDataToDB(LocationProof proof,
                                     List<LocationEndorsement> verifiedEndorsements) {
        // get endorsements
        List<LocationEndorsementData> endorsementData = new ArrayList<>();
        for (LocationEndorsement e : verifiedEndorsements) {
            Pair<Double, Double> latLngPair = getLatitudeLongitude(e.getEvidence());
            endorsementData.add(new LocationEndorsementData(e.getEndorsementId(),
                    e.getWitnessId(), e.getClaimId(), latLngPair.getFirst(), latLngPair.getSecond(),
                    e.getTime().getRelativeToEpoch().getTimeValue(), proof.getProofId()));
        }

        // get claim
        LocationClaim claim = proof.getLocClaim();
        Pair<Double, Double> latLngPair = getLatitudeLongitude(claim.getEvidence());
        LocationClaimData claimData = new LocationClaimData(claim.getClaimId(), claim.getProverId(),
                latLngPair.getFirst(), latLngPair.getSecond(),
                claim.getTime().getRelativeToEpoch().getTimeValue(), proof.getProofId());

        // Build data
        LocationProofData data = new LocationProofData(proof.getProofId(),
                proof.getTime().getRelativeToEpoch().getTimeValue(), claimData, endorsementData);

        // Schedule database write
        databaseWriter.scheduleDBWrite(data);
    }

    private Pair<Double, Double> getLatitudeLongitude(Any e) {
        double latitude;
        double longitude;
        LatLng latLng;
        Pair<Double, Double> latLngPair = null;
        try {
            latLng = e.unpack(LatLng.class);
            latitude = latLng.getLatitude();
            longitude = latLng.getLongitude();
        } catch (InvalidProtocolBufferException ex) {
            latitude = -1.0;
            longitude = -1.0;
        }

        return new Pair<>(latitude, longitude);
    }
}
