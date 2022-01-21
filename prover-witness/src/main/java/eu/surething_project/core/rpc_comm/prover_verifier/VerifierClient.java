package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.CertifyClaimGrpc;
import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.SignedLocationProof;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

public class VerifierClient {
    private final CertifyClaimGrpc.CertifyClaimBlockingStub blockingStub;
    
    public VerifierClient(ManagedChannel channel) {
        blockingStub = CertifyClaimGrpc.newBlockingStub(channel);
    }


    public LocationCertificate sendProofToVerifier(SignedLocationProof proof) {
        LocationCertificate certificate;
        try {
            certificate = blockingStub.checkLocationProof(proof);
        } catch (StatusRuntimeException e) {
            throw new EntityException(ErrorMessage.LOCATION_PROOF_SEND_ERROR);
        }
        return certificate;
    }

}