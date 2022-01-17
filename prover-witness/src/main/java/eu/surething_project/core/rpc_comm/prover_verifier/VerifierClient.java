package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

public class VerifierClient {
    private final CertifyClaimGrpc.CertifyClaimBlockingStub blockingStub;

    @Autowired
    private LocationProofBuilder locationProofBuilder;

    
    public VerifierClient(ManagedChannel channel) {
        blockingStub = CertifyClaimGrpc.newBlockingStub(channel);
    }


    public LocationCertificate sendProofToVerifier(SignedLocationClaim claim,
                                                   List<SignedLocationEndorsement> locationEndorsements)
            throws NoSuchAlgorithmException, SignatureException {
        SignedLocationProof proof = locationProofBuilder.buildSignedLocationProof(claim, locationEndorsements);
        LocationCertificate certificate;
        try {
            certificate = blockingStub.checkLocationProof(proof);
        } catch (StatusRuntimeException e) {
            throw new EntityException(ErrorMessage.LOCATION_PROOF_SEND_ERROR);
        }
        return certificate;
    }

}