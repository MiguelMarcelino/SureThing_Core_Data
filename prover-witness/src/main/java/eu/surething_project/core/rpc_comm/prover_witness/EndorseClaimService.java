package eu.surething_project.core.rpc_comm.prover_witness;

import eu.surething_project.core.grpc.EndorseClaimGrpc;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.rpc_comm.prover_witness.witness_data.LocationClaimVerifier;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Witness service to endorse claim from a given prover
 */
public class EndorseClaimService extends EndorseClaimGrpc.EndorseClaimImplBase {

    @Autowired
    private LocationClaimVerifier claimVerifier;

    /**
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void checkClaim(SignedLocationClaim request, StreamObserver<SignedLocationEndorsement> responseObserver) {
        responseObserver.onNext(claimVerifier.verifyLocationClaim(request));
        responseObserver.onCompleted();
    }



}
