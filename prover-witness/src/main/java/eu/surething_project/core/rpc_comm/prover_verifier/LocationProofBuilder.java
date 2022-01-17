package eu.surething_project.core.rpc_comm.prover_verifier;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.grpc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

import static com.google.protobuf.util.Timestamps.fromMillis;

public class LocationProofBuilder {

    @Autowired
    private CryptoHandler cryptoHandler;

    public LocationProofBuilder(CryptoHandler cryptoHandler) {
        this.cryptoHandler = cryptoHandler;
    }

    public SignedLocationProof buildSignedLocationProof(SignedLocationClaim claim,
                                                        List<SignedLocationEndorsement> endorsementList)
            throws NoSuchAlgorithmException, SignatureException {
        LocationProof proof = buildLocationProof(claim, endorsementList);
        long nonce = cryptoHandler.createNonce();
        String cryptoAlg = claim.getProverSignature().getCryptoAlgo();
        byte[] proofSigned = cryptoHandler.signData(proof.toByteArray(), cryptoAlg);

        return SignedLocationProof.newBuilder()
                .setVerification(proof)
                .setProverSignature(Signature.newBuilder()
                        .setValue(ByteString.copyFrom(proofSigned))
                        .setCryptoAlgo(cryptoAlg)
                        .setNonce(nonce)
                        .build())
                .build();
    }

    private LocationProof buildLocationProof(SignedLocationClaim claim, List<SignedLocationEndorsement> endorsementList) {
        return LocationProof.newBuilder()
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