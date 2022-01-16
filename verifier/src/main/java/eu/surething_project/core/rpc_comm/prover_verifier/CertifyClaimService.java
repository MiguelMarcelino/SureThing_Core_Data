package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.grpc.*;
import io.grpc.stub.StreamObserver;

/**
 * Communicates with the Verifier and expects a LocationCertificate
 */
public class CertifyClaimService extends CertifyClaimGrpc.CertifyClaimImplBase { //extends CertifyClaimGrpc.CertifyClaimImplBase

    /**
     * This method sends a claim to the verifier
     *
     * @param responseObserver - Response
     * @return
     */
    @Override
    public StreamObserver<SignedLocationEndorsement> checkEndorsement(StreamObserver<LocationCertificate> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(SignedLocationEndorsement value) {
                /// TODO: Is there anything to do here?
                // Perhaps receive and verify each
            }

            @Override
            public void onError(Throwable t) {
                throw new VerifierException(ErrorMessage.LOCATION_ENDORSEMENT_CONN_ERROR);
            }

            @Override
            public void onCompleted() {
                // TODO: Send Location Verification
                responseObserver.onCompleted();
            }
        };
    }


}