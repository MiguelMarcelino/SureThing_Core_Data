package eu.surething_project.core.rpc_comm.witness;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.crypto.CertificateAccess;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.*;
import eu.surething_project.core.grpc.google.type.LatLng;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.UUID;

import static com.google.protobuf.util.Timestamps.fromMillis;

public class LocationEndorsementBuilder {

    private String witnessId;

    private CryptoHandler cryptoHandler;

    private String certPath;

    public LocationEndorsementBuilder(CryptoHandler cryptoHandler, String witnessId, String certPath) {
        this.cryptoHandler = cryptoHandler;
        this.witnessId = witnessId;
        this.certPath = certPath;
    }

    public SignedLocationEndorsement buildSignedLocationEndorsement(String claimId, long nonce,
                                                                    String cryptoAlg)
            throws NoSuchAlgorithmException, SignatureException,
            InvalidKeyException, UnrecoverableKeyException, KeyStoreException {
        UUID uuid = UUID.randomUUID();
        LocationEndorsement endorsement = buildLocationEndorsement(claimId, uuid.toString());
        byte[] endorsementSigned = cryptoHandler.signData(endorsement.toByteArray(), cryptoAlg);

        // Get certificate data
        byte[] certificate = CertificateAccess.getCertificateContentAsBytes(certPath, witnessId);

        SignedLocationEndorsement locationEndorsement = SignedLocationEndorsement.newBuilder()
                .setEndorsement(endorsement)
                .setWitnessSignature(Signature.newBuilder()
                        .setValue(ByteString.copyFrom(endorsementSigned))
                        .setCryptoAlgo(cryptoAlg)
                        .setNonce(nonce)
                        .setCertificateData(ByteString.copyFrom(certificate))
                        .build())
                .build();

        return locationEndorsement;
    }

    private LocationEndorsement buildLocationEndorsement(String claimId, String endorsementId) {
        //	create location endorsement
        LocationEndorsement locationEndorsement = LocationEndorsement.newBuilder()
                .setEndorsementId(endorsementId)
                .setWitnessId(witnessId)
                .setClaimId(claimId)
                .setTime(Time.newBuilder()
                        .setTimestamp(fromMillis(TimeHandler.getCurrentTimeInMillis()))
                        .build())
                .setEvidenceType("eu.surething_project.core.grpc.google.type.LatLng")
                .setEvidence(Any.pack(LatLng.newBuilder()
                        .setLatitude(82.5)
                        .setLongitude(83.4)
                        .build()))
                .build();

        return locationEndorsement;
    }
}
