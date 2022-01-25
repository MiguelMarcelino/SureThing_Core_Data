package eu.surething_project.core.rpc_comm.prover;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import eu.surething_project.core.crypto.CertificateAccess;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.database.DatabaseAccessManagement;
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

    private DatabaseAccessManagement dbAccessMgmt;

    public LocationProofVerifier(CryptoHandler cryptoHandler, String verifierId, String externalData,
                                 String certPath, DatabaseAccessManagement dbAccessMgmt) {
        this.certificateBuilder = new LocationCertificateBuilder(cryptoHandler, verifierId, certPath);
        this.cryptoHandler = cryptoHandler;
        this.externalData = externalData;
        this.dbAccessMgmt = dbAccessMgmt;
    }

    /**
     * @param locationProof
     * @return
     */
    public LocationCertificate verifyLocationProof(SignedLocationProof locationProof)
            throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException,
            KeyStoreException, NoSuchAlgorithmException, BadPaddingException, SignatureException,
            InvalidKeyException, FileNotFoundException, CertificateException, NoSuchProviderException {
        LocationProof proof = locationProof.getVerification();

        // Debugging
        System.out.println("Received proof with id: " + proof.getProofId());
        System.out.println("ID of attached Location claim: " + proof.getLocClaim().getClaimId());

        // Get list Endorsements
        List<SignedLocationEndorsement> endorsementList = proof.getLocationEndorsementsList();
        List<String> endorsementIds = new ArrayList<>();

        // Validated endorsement Data
        List<LocationEndorsement> approvedEndorsements = new ArrayList<>();

        // Get location claim
        LocationClaim claim = proof.getLocClaim();

        // Prover id
        String proverId = proof.getLocClaim().getProverId();

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

        // verify proof with last received proof
        boolean verifyEndorsements = true;
        LocationClaimData lastReceivedClaim = dbAccessMgmt.getGetClaimByProverId(proverId);
        if (lastReceivedClaim != null) {
            LatLng latLngClaim = getLatLngFromLocation(claim.getLocation());
            double latitudeClaim = latLngClaim.getLatitude();
            double longitudeClaim = latLngClaim.getLongitude();
            double distance = DistanceCalculator.haversineFormula(latitudeClaim, longitudeClaim,
                    lastReceivedClaim.getLatitude(), lastReceivedClaim.getLongitude());

            // Only verify endorsements if prover is in range of last location
            if (distance > 5) { // TODO: Consider time
                verifyEndorsements = false;
            }
        }

        if (verifyEndorsements) {
            for (SignedLocationEndorsement endorsement : endorsementList) {
                boolean isValid = validateLocationEndorsement(endorsement, claim);
                if (isValid) {
                    LocationEndorsement locEndorse = endorsement.getEndorsement();
                    approvedEndorsements.add(locEndorse);
                    endorsementIds.add(locEndorse.getEndorsementId());
                }
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
     * Validates the location endorsement
     *
     * @param signedLocationEndorsement
     */
    private boolean validateLocationEndorsement(SignedLocationEndorsement signedLocationEndorsement,
                                                LocationClaim claim)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException,
            FileNotFoundException, CertificateException, NoSuchProviderException {
        // Get signed data
        Signature signature = signedLocationEndorsement.getWitnessSignature();
        byte[] signedEndorsement = signature.getValue().toByteArray();
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

        // Verify validity (We assume that the verifier already has witness certificates)
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
        double t1 = getSeconds(locClaim.getTime());
        double t2 = getSeconds(locEndorsement.getTime());
        // Assuming endorse happens after claim
        if ((t2 - t1) > 20) {
            return false;
        }

        // Check location
        LatLng latLngClaim = getLatLngFromLocation(locClaim.getLocation());
        double latitudeClaim = latLngClaim.getLatitude();
        double longitudeClaim = latLngClaim.getLongitude();

        LatLng latLngEvidence = getLatLngFromEvidence(locEndorsement.getEvidenceType(), locEndorsement.getEvidence());
        double latitudeEvidence = latLngEvidence.getLatitude();
        double longitudeEvidence = latLngEvidence.getLongitude();

        double distance = DistanceCalculator.haversineFormula(latitudeClaim, longitudeClaim,
                latitudeEvidence, longitudeEvidence);
        // difference of more than 200 meters results in proof rejection
        if (distance > 0.2) {
            return false;
        }

        return true;
    }

    /**
     * Adds verified data to database
     *
     * @param proof
     * @param verifiedEndorsements
     */
    private void addVerifierDataToDB(LocationProof proof,
                                     List<LocationEndorsement> verifiedEndorsements) {
        // get endorsements
        List<LocationEndorsementData> endorsementData = new ArrayList<>();
        for (LocationEndorsement e : verifiedEndorsements) {
            LatLng latLng = getLatLngFromEvidence(e.getEvidenceType(), e.getEvidence());
            endorsementData.add(new LocationEndorsementData(e.getEndorsementId(),
                    e.getWitnessId(), e.getClaimId(), latLng.getLatitude(), latLng.getLongitude(),
                    e.getTime().getRelativeToEpoch().getTimeValue(), proof.getProofId()));
        }

        // get claim
        LocationClaim claim = proof.getLocClaim();
        LatLng latLngClaim = getLatLngFromLocation(claim.getLocation());
        double latitudeClaim = latLngClaim.getLatitude();
        double longitudeClaim = latLngClaim.getLongitude();
        LocationClaimData claimData = new LocationClaimData(claim.getClaimId(), claim.getProverId(),
                latitudeClaim, longitudeClaim, claim.getTime().getRelativeToEpoch().getTimeValue(),
                proof.getProofId());

        // Build data
        LocationProofData data = new LocationProofData(proof.getProofId(),
                proof.getTime().getRelativeToEpoch().getTimeValue(), claimData, endorsementData);

        // Schedule database write
        dbAccessMgmt.addProofData(data);
    }

    /**
     * @param time - oneof (TIMESTAMP, INTERVAL, RELATIVETOEPOCH, EMPTY)
     * @return
     */
    private double getSeconds(Time time) {
        Time.TimeCase timeCase = time.getTimeCase();
        double seconds = 0.0;
        Timestamp timestamp = null;
        switch (timeCase) {
            case TIMESTAMP:
                seconds = time.getTimestamp().getSeconds();
                break;
            case INTERVAL:
                seconds = time.getInterval().getEnd().getSeconds() -
                        time.getInterval().getBegin().getSeconds();
                break;
            case RELATIVETOEPOCH:
                // seconds relative to epoch, which is January 1, 1970
                seconds = time.getRelativeToEpoch().getTimeValue();
                break;
            case TIME_NOT_SET, EMPTY:
                break;
        }
        return seconds;
    }

    /**
     * Gets LatLng from evidence
     *
     * @param evidenceType
     * @param evidence
     * @return
     */
    private LatLng getLatLngFromEvidence(String evidenceType, Any evidence) {
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

    /**
     * Gets LatLng from Location object
     *
     * @param location
     * @return
     */
    private LatLng getLatLngFromLocation(Location location) {
        LatLng latLng = null;
        switch (location.getLocationCase()) {
            case LATLNG:
                latLng = location.getLatLng();
                break;
            case POI, PROXIMITYTOPOI, OLC, LOCATION_NOT_SET:
                break;
        }
        return latLng;
    }
}
