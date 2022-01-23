package eu.surething_project.core.rpc_comm.prover_verifier;

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

public class LocationProofBuilder {

    private CryptoHandler cryptoHandler;

    private String certPath;

    private String currentEntityId;

    public LocationProofBuilder(CryptoHandler cryptoHandler, String currentEntityId, String certPath) {
        this.cryptoHandler = cryptoHandler;
        this.currentEntityId = currentEntityId;
        this.certPath = certPath;
    }

    public SignedLocationProof buildSignedLocationProof(LocationClaim claim,
                                                        List<SignedLocationEndorsement> endorsementList,
                                                        String cryptoAlg)
            throws NoSuchAlgorithmException, SignatureException, UnrecoverableKeyException,
            KeyStoreException, InvalidKeyException {
        String uuid = UUID.randomUUID().toString();

        LocationProof proof = buildLocationProof(claim, endorsementList, uuid);
        long nonce = cryptoHandler.createNonce();
        byte[] proofSigned = cryptoHandler.signData(proof.toByteArray(), cryptoAlg);

        // Get certificate data
        byte[] certificate = CertificateAccess.getCertificateContentAsBytes(certPath, currentEntityId);

        return SignedLocationProof.newBuilder()
                .setVerification(proof)
                .setProverSignature(Signature.newBuilder()
                        .setValue(ByteString.copyFrom(proofSigned))
                        .setCryptoAlgo(cryptoAlg)
                        .setNonce(nonce)
                        .setCertificateData(ByteString.copyFrom(certificate))
                        .build())
                .build();
    }

    private LocationProof buildLocationProof(LocationClaim claim,
                                             List<SignedLocationEndorsement> endorsementList, String proofId) {
        return LocationProof.newBuilder()
                .setProofId(proofId)
                .setLocClaim(claim)
                .addAllLocationEndorsements(endorsementList)
                .setTime(Time.newBuilder()
                        .setTimestamp(fromMillis(TimeHandler.getCurrentTimeInMillis()))
                        .build())
                .setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
                .setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
                        .setId("ABC")
                        .addAps(WiFiNetworksEvidence.AP.newBuilder()
                                .setSsid("ssid-A")
                                .setRssi("-89")
                                .build())
                        .build()))
                .build();
    }
}
