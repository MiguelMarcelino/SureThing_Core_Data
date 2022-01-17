package eu.surething_project.core.rpc_comm.prover;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

import static com.google.protobuf.util.Timestamps.fromMillis;

@Service
public class LocationProofVerifier {

    @Value("verifier.id")
    private String verifierId;

    @Value("verifier.min_endorsement_approval")
    private int minEndorsementApproval;

    @Autowired
    private CryptoHandler cryptoHandler;

    /**
     * @param locationProof
     * @return
     */
    public LocationCertificate verifyLocationProof(SignedLocationProof locationProof)
            throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException,
            KeyStoreException, NoSuchAlgorithmException, BadPaddingException, SignatureException,
            InvalidKeyException {
        List<SignedLocationEndorsement> endorsementList = locationProof.getVerification().getLocationEndorsementsList();
        List<String> endorsementIds = new ArrayList<>();
        SignedLocationClaim signedClaim = locationProof.getVerification().getLocClaim();
        LocationClaim claim = signedClaim.getClaim();
        for (SignedLocationEndorsement endorsement : endorsementList) {
            boolean isValid = validateLocationEndorsement(endorsement, claim);
            if (isValid) {
                endorsementIds.add(endorsement.getEndorsement().getClaimId()); // TODO: There is no Endorsement ID
            }
        }

        // Verify if there are enough endorsements
        LocationCertificate certificate = endorsementList.size() >= minEndorsementApproval ?
                buildCertificate(claim.getClaimId(), endorsementIds) :
                buildCertificate(claim.getClaimId(), new ArrayList<>());

        return certificate;
    }

    /**
     * @param signedLocationEndorsement
     */
    private boolean validateLocationEndorsement(SignedLocationEndorsement signedLocationEndorsement,
                                                LocationClaim claim)
            throws UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException,
            KeyStoreException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException,
            SignatureException {
        // Get signed data
        Signature signature = signedLocationEndorsement.getWitnessSignature();
        long nonce = signature.getNonce();
        byte[] encryptedEndorsement = signature.getValue().toByteArray();
        String cryptoAlg = signature.getCryptoAlgo();

        // Get Location Endorsement Data
        LocationEndorsement locEndorsement = signedLocationEndorsement.getEndorsement();

        boolean isValid;

        // Decrypt endorsement with Verifier private Key
        byte[] signedEndorsement = cryptoHandler.decryptDataAssym(encryptedEndorsement, "verifier");

        // Verify signed data
        isValid = cryptoHandler.verifyData(locEndorsement.toByteArray(), signedEndorsement, cryptoAlg);

        // Only check data contents after it has been verified
        if(isValid) {
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

    /**
     * @param claimId
     * @param endorsementLst
     * @return
     */
    private LocationCertificate buildCertificate(String claimId, List<String> endorsementLst) {
        LocationVerification locationVerification = LocationVerification.newBuilder()
                .setVerifierId(verifierId)
                .setClaimId(claimId)
                .addAllEndorsementIds(endorsementLst)
                .setTime(Time.newBuilder()
                        .setTimestamp(fromMillis(TimeHandler.getCurrentTimeInMillis()))
                        .build())
                .setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
                .setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
                        .setId("GHI")
                        .addAps(WiFiNetworksEvidence.AP.newBuilder()
                                .setSsid("ssid-C")
                                .setRssi("-70")
                                .build())
                        .build()))
                .build();

        LocationCertificate locationCertificate = LocationCertificate.newBuilder()
                .setVerification(locationVerification)
                .build();
        return locationCertificate;
    }

}
