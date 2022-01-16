package eu.surething_project.core.rpc_comm.Verifier;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class VerifierClient {
    private static final Logger logger = LoggerFactory.getLogger(VerifierClient.class);

    private final CertifyClaimGrpc.CertifyClaimBlockingStub blockingStub;

    private final CertifyClaimGrpc.CertifyClaimStub asyncStub;
    
    public VerifierClient(ManagedChannel channel) {
        blockingStub = CertifyClaimGrpc.newBlockingStub(channel);
        asyncStub = CertifyClaimGrpc.newStub(channel);
    }

    public void sendClaimsToVerifier(List<SignedLocationEndorsement> locationEndorsements) throws InterruptedException {
        ArrayList<LocationCertificate> locationVerifications = new ArrayList<>();
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<LocationCertificate> responseObserver = new StreamObserver<LocationCertificate>() {
            @Override
            public void onNext(LocationCertificate value) {
                locationVerifications.add(value);
            }

            @Override
            public void onError(Throwable t) {
                finishLatch.countDown();
                logger.debug("Error in gRPC connection: sendClaimsToVerifier");
            }

            @Override
            public void onCompleted() {
                logger.info("Finished receiving LocationVerification");
                finishLatch.countDown();
            }
        };

        StreamObserver<SignedLocationEndorsement> requestObserver = asyncStub.sendClaimToVerifier(responseObserver);
        try {
            for (SignedLocationEndorsement endorsement : locationEndorsements) {
                requestObserver.onNext(endorsement);
                if (finishLatch.getCount() == 0) {
                    // RPC completed or errored before we finished sending.
                    // Sending further requests won't error, but they will just be thrown away.
                    return;
                }
            }
        } catch(RuntimeException e) {
            requestObserver.onError(e);
            throw new EntityException(ErrorMessage.LOCATION_ENDORSEMENT_SEND_ERROR);
        }

        // Mark the end of requests
        requestObserver.onCompleted();

        // Receiving happens asynchronously
        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            logger.warn("recordRoute can not finish within 1 minutes");
        }
    }

}