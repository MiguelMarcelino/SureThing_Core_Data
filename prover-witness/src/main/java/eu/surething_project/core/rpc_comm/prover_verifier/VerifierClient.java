package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.CertifyClaimGrpc;
import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.SignedLocationProof;
import eu.surething_project.core.rpc_comm.prover.WitnessClient;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.logging.Logger;

public class VerifierClient {

    private static final Logger logger = Logger.getLogger(VerifierClient.class.getName());

    private final CertifyClaimGrpc.CertifyClaimBlockingStub blockingStub;
    
    public VerifierClient(ManagedChannel channel) {
        blockingStub = CertifyClaimGrpc.newBlockingStub(channel);
    }

    /**
     * Sends Location proof to verifier
     * @param proof - result from Verifier
     * @return
     */
    public LocationCertificate sendProofToVerifier(SignedLocationProof proof) {
        LocationCertificate certificate = null;
        try {
            certificate = blockingStub.checkLocationProof(proof);
        } catch (StatusRuntimeException e) {
            logger.warning(ErrorMessage.LOCATION_PROOF_SEND_ERROR.message
                    + ": " + e.getMessage());
        }
        return certificate;
    }

}