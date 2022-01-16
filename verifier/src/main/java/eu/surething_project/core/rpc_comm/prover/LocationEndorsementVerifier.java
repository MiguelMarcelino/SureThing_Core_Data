package eu.surething_project.core.rpc_comm.prover;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.RepeatedFieldBuilder;
import com.google.protobuf.Timestamp;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.grpc.*;
import eu.surething_project.core.grpc.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

import static com.google.protobuf.util.Timestamps.fromMillis;

public class LocationEndorsementVerifier {

    @Value("verifier.id")
    private String verifierId;

    @Autowired
    private CryptoHandler cryptoHandler;

    public void verifyLocationEndorsement(SignedLocationEndorsement signedLocationEndorsement)
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

        // Decrypt endorsement with Verifier private Key
        byte[] signedEndorsement = cryptoHandler.decryptDataAssym(encryptedEndorsement, "verifier");

        // Verify signed data
        cryptoHandler.verifyData(locEndorsement.toByteArray(), signedEndorsement, cryptoAlg);

        /////////////////////////////////////////
        // Verify Data
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

        // TODO: Check if timestamp is not old (?)

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
    }

    public LocationCertificate buildCertificate(String claimId, String endorsementIds) {
        LocationVerification  locationVerification  = LocationVerification.newBuilder()
                .setVerifierId (verifierId)
                .setClaimId (claimId)
                .addEndorsementIds(endorsementIds) // Change to repeated
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
