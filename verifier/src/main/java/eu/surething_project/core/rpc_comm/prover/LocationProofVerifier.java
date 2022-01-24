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
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.*;
import eu.surething_project.core.grpc.google.type.LatLng;
import eu.surething_project.core.location_simulation.DistanceCalculator;

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
        if (!certFileCreate) {
            throw new VerifierException(ErrorMessage.ERROR_CREATING_CERTIFICATE);
        }

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

        // Get Witness Certificate (we assume that the verifier already has witness certificates)
        boolean certFileExists = CertificateAccess.checkCertificateExists(externalData, witnessId);
        if (!certFileExists) {
            throw new VerifierException(ErrorMessage.ERROR_GETTING_CERTIFICATE);
        }

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
        // Check if timestamp is not old when compared to locClaim
        Time timeClaim = locClaim.getTime();
        Time timeEndorse = locEndorsement.getTime();
        Timestamp t1 = getTimestamp(timeClaim);
        Timestamp t2 = getTimestamp(timeEndorse);
        // Assuming endorse happens after claim
        if ((t2.getNanos() - t1.getNanos()) > 10000) {
            return false;
        }

        // Check location
        Location locationClaim = locClaim.getLocation();
        double latitudeClaim = 0;
        double longitudeClaim = 0;
        switch (locClaim.getLocation().getLocationCase()) {
            case LATLNG:
                LatLng latLngClaim = locationClaim.getLatLng();
                latitudeClaim = latLngClaim.getLatitude();
                longitudeClaim = latLngClaim.getLongitude();
            case POI, PROXIMITYTOPOI, OLC, LOCATION_NOT_SET:
                break;
        }

        LatLng latLngEvidence = getLatLng(locEndorsement.getEvidenceType(), locEndorsement.getEvidence());
        double latitudeEvidence = latLngEvidence.getLatitude();
        double longitudeEvidence = latLngEvidence.getLongitude();

        double distance = DistanceCalculator.haversineFormula(latitudeClaim, longitudeClaim,
                latitudeEvidence, longitudeEvidence);
        if (distance > 5) { // difference of 5 km. Is it too much?
            return false;
        }

        return true;
    }

    private void addVerifierDataToDB(LocationProof proof,
                                     List<LocationEndorsement> verifiedEndorsements) {
        // get endorsements
        List<LocationEndorsementData> endorsementData = new ArrayList<>();
        for (LocationEndorsement e : verifiedEndorsements) {
            LatLng latLng = getLatLng(e.getEvidenceType(), e.getEvidence());
            endorsementData.add(new LocationEndorsementData(e.getEndorsementId(),
                    e.getWitnessId(), e.getClaimId(), latLng.getLatitude(), latLng.getLongitude(),
                    e.getTime().getRelativeToEpoch().getTimeValue(), proof.getProofId()));
        }

        // get claim
        LocationClaim claim = proof.getLocClaim();
        Location loc = claim.getLocation();
        double latitudeClaim = 0;
        double longitudeClaim = 0;
        switch (claim.getLocation().getLocationCase()) {
            case LATLNG:
                LatLng latLngClaim = loc.getLatLng();
                latitudeClaim = latLngClaim.getLatitude();
                longitudeClaim = latLngClaim.getLongitude();
            case POI, PROXIMITYTOPOI, OLC, LOCATION_NOT_SET:
                break;
        }
        LocationClaimData claimData = new LocationClaimData(claim.getClaimId(), claim.getProverId(),
                latitudeClaim, longitudeClaim, claim.getTime().getRelativeToEpoch().getTimeValue(),
                proof.getProofId());

        // Build data
        LocationProofData data = new LocationProofData(proof.getProofId(),
                proof.getTime().getRelativeToEpoch().getTimeValue(), claimData, endorsementData);

        // Schedule database write
        databaseWriter.scheduleDBWrite(data);
    }

    /**
     * @param time - oneof (TIMESTAMP, INTERVAL, RELATIVETOEPOCH, EMPTY)
     * @return
     */
    private Timestamp getTimestamp(Time time) {
        Time.TimeCase timeCase = time.getTimeCase();
        Timestamp timestamp = null;
        switch (timeCase) {
            case TIMESTAMP:
                timestamp = time.getTimestamp();
            case INTERVAL:
                time.getInterval();
            case RELATIVETOEPOCH:
                time.getRelativeToEpoch();
            case TIME_NOT_SET, EMPTY:
                break;
        }
        return timestamp;
    }

    private LatLng getLatLng(String evidenceType, Any evidence) {
        LatLng latLng = null;
        String expectedType = "eu.surething_project.core.grpc.google.type.LatLng";
        try {
            if (evidenceType == expectedType) {
                System.err.println("Expected evidence of type LatLng, " +
                        "but provided was: " + evidenceType);
            }
            latLng = evidence.unpack(LatLng.class);

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}
