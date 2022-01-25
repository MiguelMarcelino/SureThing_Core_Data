package eu.surething_project.core.rpc_comm.prover;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.crypto.CertificateAccess;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.*;

import java.security.*;
import java.util.List;
import java.util.UUID;

import static com.google.protobuf.util.Timestamps.fromMillis;

public class LocationCertificateBuilder {

    private String verifierId;

    private CryptoHandler cryptoHandler;

    private String certPath;

    public LocationCertificateBuilder(CryptoHandler cryptoHandler, String verifierId, String certPath) {
        this.cryptoHandler = cryptoHandler;
        this.verifierId = verifierId;
        this.certPath = certPath;
    }

    /**
     * Builds Location Certificate to send to prover
     *
     * @param claimId
     * @param endorsementLst
     * @return
     */
    public LocationCertificate buildCertificate(String claimId, List<String> endorsementLst,
                                                long nonce, String cryptoAlg)
            throws NoSuchAlgorithmException, SignatureException, UnrecoverableKeyException,
            KeyStoreException, InvalidKeyException {
        UUID uuid = UUID.randomUUID();
        LocationVerification verification = buildLocVerification(claimId, endorsementLst, uuid.toString());
        byte[] verificationSigned = cryptoHandler.signData(verification.toByteArray(), cryptoAlg);

        // Get certificate data
        byte[] certificate = CertificateAccess.getCertificateContentAsBytes(certPath, verifierId);

        LocationCertificate locationCertificate = LocationCertificate.newBuilder()
                .setVerification(verification)
                .setVerifierSignature(Signature.newBuilder()
                        .setValue(ByteString.copyFrom(verificationSigned))
                        .setCryptoAlgo(cryptoAlg)
                        .setNonce(nonce)
                        .setCertificateData(ByteString.copyFrom(certificate))
                        .build())
                .build();
        return locationCertificate;
    }

    /**
     * Builds location verification
     *
     * @param claimId
     * @param endorsementLst
     * @return
     */
    private LocationVerification buildLocVerification(String claimId, List<String> endorsementLst,
                                                      String verificationId) {
        return LocationVerification.newBuilder()
                .setVerificationId(verificationId)
                .setVerifierId(verifierId)
                .setClaimId(claimId)
                .addAllEndorsementIds(endorsementLst)
                .setTime(Time.newBuilder()
                        .setRelativeToEpoch(EpochTime.newBuilder()
                                .setTimeValue(TimeHandler.getCurrentTimeInMillis())
                                .build())
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
    }
}
