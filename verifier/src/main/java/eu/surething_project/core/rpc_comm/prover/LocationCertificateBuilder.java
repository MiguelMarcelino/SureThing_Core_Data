package eu.surething_project.core.rpc_comm.prover;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.grpc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

import static com.google.protobuf.util.Timestamps.fromMillis;

@Service
public class LocationCertificateBuilder {

    @Value("verifier.id")
    private String verifierId;

    @Autowired
    private CryptoHandler cryptoHandler;

    public LocationCertificateBuilder() { }

    /**
     *
     * @param claimId
     * @param endorsementLst
     * @return
     */
    public LocationCertificate buildCertificate(String claimId, List<String> endorsementLst,
                                                long nonce, String cryptoAlg)
            throws NoSuchAlgorithmException, SignatureException {
        LocationVerification verification = buildLocVerification(claimId, endorsementLst);
        byte[] verificationSigned = cryptoHandler.signData(verification.toByteArray(), cryptoAlg);

        LocationCertificate locationCertificate = LocationCertificate.newBuilder()
                .setVerification(verification)
                .setVerifierSignature(Signature.newBuilder()
                        .setValue(ByteString.copyFrom(verificationSigned))
                        .setCryptoAlgo(cryptoAlg)
                        .setNonce(nonce)
                        .build())
                .build();
        return locationCertificate;
    }

    /**
     *
     * @param claimId
     * @param endorsementLst
     * @return
     */
    private LocationVerification buildLocVerification(String claimId,
                                                     List<String> endorsementLst) {
        return LocationVerification.newBuilder()
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
    }
}
