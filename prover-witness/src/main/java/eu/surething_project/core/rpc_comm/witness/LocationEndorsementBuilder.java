package eu.surething_project.core.rpc_comm.witness;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.grpc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;

import static com.google.protobuf.util.Timestamps.fromMillis;

public class LocationEndorsementBuilder {

    @Value("${witness.id}")
    private String witnessId;

    @Autowired
    private CryptoHandler cryptoHandler;

    public LocationEndorsementBuilder() {}

    public SignedLocationEndorsement buildSignedLocationEndorsement(String claimId, long nonce, String cryptoAlg)
            throws NoSuchAlgorithmException, SignatureException, FileNotFoundException,
            NoSuchPaddingException, IllegalBlockSizeException, CertificateException, BadPaddingException,
            InvalidKeyException {
        LocationEndorsement endorsement = buildLocationEndorsement(claimId);
        byte[] endorsementSigned = cryptoHandler.signData(endorsement.toByteArray(), cryptoAlg);
        byte[] encryptedEndorsement = cryptoHandler.encryptDataAssym(endorsementSigned, "verifier");
        return SignedLocationEndorsement.newBuilder()
                .setEndorsement(endorsement)
                .setWitnessSignature(Signature.newBuilder()
                        .setValue(ByteString.copyFrom(encryptedEndorsement)) // Temporary
                        .setCryptoAlgo(cryptoAlg)
                        .setNonce(nonce)
                        .build())
                .build();
    }

    private LocationEndorsement buildLocationEndorsement(String claimId) {
        //	create location endorsement
        LocationEndorsement locationEndorsement = LocationEndorsement.newBuilder()
                .setWitnessId(witnessId)
                .setClaimId (claimId)
                .setTime(Time.newBuilder()
                        .setTimestamp(fromMillis(TimeHandler.getCurrentTimeInMillis()))
                        .build())
                .setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
                .setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder() // TODO: Check Evidence
                        .setId("DEF")
                        .addAps(WiFiNetworksEvidence.AP.newBuilder()
                                .setSsid("ssid-B")
                                .setRssi("-90")
                                .build())
                        .build()))
                .build();

        return locationEndorsement;
    }
}
