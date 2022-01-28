package eu.surething_project.core.rpc_comm.prover;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import eu.surething_project.core.config.PropertiesReader;
import eu.surething_project.core.config.TimeHandler;
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
import java.util.stream.Collectors;

public class LocationProofVerifier {

    private final CryptoHandler cryptoHandler;

    private static int minEndorsementApproval;

    private LocationCertificateBuilder certificateBuilder;

    private String externalData;

    private DatabaseAccessManagement dbAccessMgmt;

    private double range = Double.parseDouble(PropertiesReader.getProperty("entity.range"));

    public LocationProofVerifier(CryptoHandler cryptoHandler, String verifierId, String externalData,
                                 String certPath, DatabaseAccessManagement dbAccessMgmt) {
        this.certificateBuilder = new LocationCertificateBuilder(cryptoHandler, verifierId, certPath);
        this.cryptoHandler = cryptoHandler;
        this.externalData = externalData;
        this.dbAccessMgmt = dbAccessMgmt;
        this.minEndorsementApproval = Integer.parseInt(
                PropertiesReader.getProperty("entity.min_endorsement_approval"));
    }

    /**
     * Verifies the received Location Proof. It verifies signature validity,
     * both of LocationProof and the endorsements. It also verifies data validity.
     *
     * @param locationProof
     * @return
     */
    public LocationCertificate verifyLocationProof(SignedLocationProof locationProof)
            throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException,
            KeyStoreException, NoSuchAlgorithmException, BadPaddingException, SignatureException,
            InvalidKeyException, FileNotFoundException, CertificateException, NoSuchProviderException {
        LocationProof proof = locationProof.getVerification();

        // DEBUG
        if (PropertiesReader.getDebugProperty()) {
            System.out.println("------------------");
            System.out.println("ID of Location Proof: " + proof.getProofId());
            System.out.println("ID of attached Location claim: " + proof.getLocClaim().getClaimId());
            System.out.println("IDs of attached Location Endorsements: " +
                    proof.getLocationEndorsementsList().stream()
                            .map(x -> x.getEndorsement().getEndorsementId())
                            .collect(Collectors.toList()));
        }

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
        LocationClaimData lastReceivedClaim = dbAccessMgmt.getLastClaimByProverId(proverId);
        if (lastReceivedClaim != null) {
            // get associated proof
            LocationProofData lastReceivedProof = dbAccessMgmt.getProofById(lastReceivedClaim.getProofId());

            // Get latitude and longitude
            LatLng latLngClaim = getLatLngFromLocation(claim.getLocation());
            double latitudeClaim = latLngClaim.getLatitude();
            double longitudeClaim = latLngClaim.getLongitude();
            double distance = DistanceCalculator.haversineFormula(latitudeClaim, longitudeClaim,
                    lastReceivedClaim.getLatitude(), lastReceivedClaim.getLongitude());

            // Get time from corresponding proof
            double lastProofTime = lastReceivedProof.getTimeInMillis();
            double currentTime = TimeHandler.getCurrentTimeInMillis();
            double timeDiff = currentTime - lastProofTime;

            // DEBUG
            if (PropertiesReader.getDebugProperty()) {
                System.out.println("Distance between current Proof and last proof: "
                        + distance);
                System.out.println("Time difference between current Proof and last proof: "
                        + timeDiff);
            }

            // Only verify endorsements if prover is in range of last location
            // In less than 30 seconds the prover cannot cover a distance larger than 4 km
            if (timeDiff < 30000 && distance > 4) {
                verifyEndorsements = false;
            }
        }

        if (verifyEndorsements) {
            for (SignedLocationEndorsement endorsement : endorsementList) {
                boolean isValid = validateLocationData(endorsement, claim);
                if (isValid) {
                    LocationEndorsement locEndorse = endorsement.getEndorsement();
                    approvedEndorsements.add(locEndorse);
                    endorsementIds.add(locEndorse.getEndorsementId());
                }
            }
        }

        // DEBUG
        if (PropertiesReader.getDebugProperty()) {
            System.out.println("IDs of approved Endorsements: " + endorsementIds);
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
    private boolean validateLocationData(SignedLocationEndorsement signedLocationEndorsement,
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
        boolean certFileExists = CertificateAccess.checkFileExists(externalData, witnessId);
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
     * Checks the validity of the provided Location data
     *
     * @param locEndorsement
     * @param locClaim
     */
    private boolean checkData(LocationEndorsement locEndorsement, LocationClaim locClaim) {
        // Check if timestamp is not old when compared to locClaim
        double t1 = getMilliseconds(locClaim.getTime());
        double t2 = getMilliseconds(locEndorsement.getTime());

        // Assuming endorse happens after claim
        // If endorse happens 20 seconds after claim, assume connection between
        // Prover and Witness is not stable
        if ((t2 - t1) > 20000) {
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

        // DEBUG
        if (PropertiesReader.getDebugProperty()) {
            System.out.println("Timestamp claim: " + t1);
            System.out.println("Timestamp endorsement: " + t2);
            System.out.println("Difference in milliseconds between endorsement and claim: "
                    + (t2 - t1));
            System.out.println("Distance between Claim and Endorsement: "
                    + distance);
            if (distance >= range)
                System.out.print(" --> Rejected");
        }

        // difference of more than 200 meters results in proof rejection
        if (distance >= range) {
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
                latitudeClaim, longitudeClaim, getMilliseconds(claim.getTime()),
                proof.getProofId());

        // Build data
        LocationProofData data = new LocationProofData(proof.getProofId(),
                TimeHandler.getCurrentTimeInMillis(), claimData, endorsementData);

        // Schedule database write
        dbAccessMgmt.addProofData(data);
    }

    /**
     * Get time in seconds
     *
     * @param time - oneof (TIMESTAMP, INTERVAL, RELATIVETOEPOCH, EMPTY)
     * @return
     */
    private long getMilliseconds(Time time) {
        Time.TimeCase timeCase = time.getTimeCase();
        long milliseconds = 0;
        Timestamp timestamp = null;
        switch (timeCase) {
            case TIMESTAMP:
                milliseconds = time.getTimestamp().getNanos() * 1000000;
                break;
            case INTERVAL:
                milliseconds = time.getInterval().getEnd().getNanos() * 1000000 -
                        time.getInterval().getBegin().getNanos() * 1000000;
                break;
            case RELATIVETOEPOCH:
                // seconds relative to epoch, which is January 1, 1970
                milliseconds = time.getRelativeToEpoch().getTimeValue();
                break;
            case TIME_NOT_SET:
                break;
            case EMPTY:
            	break;
        }
        return milliseconds;
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
            case POI:
                break;
            case PROXIMITYTOPOI:
            	break;
            case OLC:
            	break;
            case LOCATION_NOT_SET:
            	break;
            
        }
        return latLng;
    }
}
