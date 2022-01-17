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

//    private final CertifyClaimGrpc.CertifyClaimStub asyncStub;
    
    public VerifierClient(ManagedChannel channel) {
        blockingStub = CertifyClaimGrpc.newBlockingStub(channel);
//        asyncStub = CertifyClaimGrpc.newStub(channel);
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

//    public SignedLocationEndorsement sendSignedClaimToWitness(SignedLocationClaim locationClaim) {
//        SignedLocationEndorsement signedLocationEndorsement;
//        try {
//
//            signedLocationEndorsement = blockingStub.checkClaim(locationClaim);
//        } catch (StatusRuntimeException e) {
//            throw new EntityException(ErrorMessage.LOCATION_CLAIM_SEND_ERROR);
//        }
//
//        return signedLocationEndorsement;
//    }

//    /**
//     * Sends data asynchronously to the Verifier
//     * @param locationEndorsements - Data to send
//     * @throws InterruptedException
//     */
//    public LocationCertificate sendEndorsementsToVerifier(List<SignedLocationEndorsement> locationEndorsements) throws InterruptedException {
//        ArrayList<LocationCertificate> locationVerifications = new ArrayList<>();
//        final CountDownLatch finishLatch = new CountDownLatch(1);
//        StreamObserver<LocationCertificate> responseObserver = new StreamObserver<LocationCertificate>() {
//            @Override
//            public void onNext(LocationCertificate value) {
//                locationVerifications.add(value);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                finishLatch.countDown();
//                logger.debug("Error in gRPC connection: sendClaimsToVerifier");
//            }
//
//            @Override
//            public void onCompleted() {
//                logger.info("Finished receiving LocationVerification");
//                finishLatch.countDown();
//            }
//        };
//
//        StreamObserver<SignedLocationEndorsement> requestObserver = asyncStub.checkEndorsement(responseObserver);
//        try {
//            for (SignedLocationEndorsement endorsement : locationEndorsements) {
//                requestObserver.onNext(endorsement);
//                if (finishLatch.getCount() == 0) {
//                    // RPC completed or errored before we finished sending.
//                    // Sending further requests won't error, but they will just be thrown away.
//                    throw new EntityException(ErrorMessage.LOCATION_ENDORSEMENT_SEND_ERROR);
//                }
//            }
//        } catch(RuntimeException e) {
//            requestObserver.onError(e);
//            throw new EntityException(ErrorMessage.LOCATION_ENDORSEMENT_SEND_ERROR);
//        }
//
//        // Mark the end of requests
//        requestObserver.onCompleted();
//
//        // Receiving happens asynchronously
//        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
//            logger.warn("recordRoute can not finish within 1 minutes");
//        }
//
//        return locationVerifications.get(0);
//    }

}