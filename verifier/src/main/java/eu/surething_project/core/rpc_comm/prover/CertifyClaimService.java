package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.grpc.CertifyClaimGrpc;
import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.SignedLocationProof;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

/**
 * Communicates with the Verifier and expects a LocationCertificate
 */
public class CertifyClaimService extends CertifyClaimGrpc.CertifyClaimImplBase {

    @Autowired
    private LocationProofVerifier endorsementVerifier;

    @Override
    public void checkLocationProof(SignedLocationProof locationProof, StreamObserver<LocationCertificate> responseObserver) {
        try {
            responseObserver.onNext(endorsementVerifier.verifyLocationProof(locationProof));
        } catch (UnrecoverableKeyException | NoSuchPaddingException | BadPaddingException |
                InvalidKeyException | IllegalBlockSizeException | KeyStoreException e) {
            throw new VerifierException(ErrorMessage.ERROR_ENCRYPTING_DATA, e);
        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new VerifierException(ErrorMessage.ERROR_SIGNING_DATA, e);
        }
        responseObserver.onCompleted();
    }

    //    /**
//     * This method sends a claim to the verifier
//     *
//     * @param responseObserver - Response
//     * @return
//     */
//    @Override
//    public StreamObserver<SignedLocationEndorsement> checkEndorsement(StreamObserver<LocationCertificate> responseObserver) {
//        return new StreamObserver<>() {
//            int endorsementCount;
//            String claimId;
//            List<String> endorseIds = new ArrayList<>();
//            @Override
//            public void onNext(SignedLocationEndorsement value) {
//                try {
//                    endorsementVerifier.verifyLocationEndorsement(value);
//                    endorsementCount++;
//                    LocationEndorsement endorsement = value.getEndorsement();
//                    claimId = endorsement.getClaimId();
//                    endorseIds.add(""); // TODO
//                } catch (UnrecoverableKeyException e) {
//                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
//                } catch (NoSuchPaddingException e) {
//                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
//                } catch (IllegalBlockSizeException e) {
//                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
//                } catch (KeyStoreException e) {
//                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
//                } catch (NoSuchAlgorithmException e) {
//                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
//                } catch (BadPaddingException e) {
//                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
//                } catch (InvalidKeyException e) {
//                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
//                } catch (SignatureException e) {
//                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
//                }
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                throw new VerifierException(ErrorMessage.LOCATION_ENDORSEMENT_CONN_ERROR);
//            }
//
//            @Override
//            public void onCompleted() {
//                if (endorsementCount > 0) {
//                    responseObserver.onNext(endorsementVerifier.buildCertificate(claimId, endorseIds));
//                }
//                responseObserver.onCompleted();
//            }
//        };
//    }


}
