package eu.surething_project.core.rpc_comm.witness;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.cert.CertificateException;

import static com.google.protobuf.util.Timestamps.fromMillis;

public class LocationEndorsementBuilder {

    private String witnessId;

    private CryptoHandler cryptoHandler;

    public LocationEndorsementBuilder(CryptoHandler cryptoHandler, String witnessId) {
        this.cryptoHandler = cryptoHandler;
        this.witnessId = witnessId;
    }

    public SignedLocationEndorsement buildSignedLocationEndorsement(String claimId, long nonce, String cryptoAlg)
            throws NoSuchAlgorithmException, SignatureException, FileNotFoundException,
            NoSuchPaddingException, IllegalBlockSizeException, CertificateException, BadPaddingException,
            InvalidKeyException, UnrecoverableKeyException, KeyStoreException {
        LocationEndorsement endorsement = buildLocationEndorsement(claimId);
        byte[] endorsementSigned = cryptoHandler.signData(endorsement.toByteArray(), cryptoAlg);
        SignedLocationEndorsement locationEndorsement = SignedLocationEndorsement.newBuilder()
                .setEndorsement(endorsement)
                .setWitnessSignature(Signature.newBuilder()
                        .setValue(ByteString.copyFrom(endorsementSigned))
                        .setCryptoAlgo(cryptoAlg)
                        .setNonce(nonce)
                        .build())
                .build();

        // TODO: Send encrypted endorsement
        byte[] encryptedEndorsement = cryptoHandler.encryptDataAssym(locationEndorsement.toByteArray(),
                claimId, cryptoAlg);

        return locationEndorsement;
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
