package eu.surething_project.core.rpc_comm;

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
    public StreamObserver<SignedLocationEndorsement> sendClaimToVerifier(StreamObserver<LocationCertificate> responseObserver) {
        return new StreamObserver<SignedLocationEndorsement>() {
            @Override
            public void onNext(SignedLocationEndorsement value) {
                /// TODO: Is there anything to do here?
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

    @Override
    public StreamObserver<LocationEndorsement> sendClaimToVerifierNoSigning(StreamObserver<LocationVerification> responseObserver) {
        return new StreamObserver<LocationEndorsement>() {
            @Override
            public void onNext(LocationEndorsement value) {
                // TODO: Is there anything to do here?
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
