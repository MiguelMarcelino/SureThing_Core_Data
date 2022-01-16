package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.grpc.*;
import io.grpc.stub.StreamObserver;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Communicates with the Verifier and expects a LocationCertificate
 */
public class CertifyClaimService extends CertifyClaimGrpc.CertifyClaimImplBase {

    private LocationEndorsementVerifier endorsementVerifier;

    /**
     * This method sends a claim to the verifier
     *
     * @param responseObserver - Response
     * @return
     */
    @Override
    public StreamObserver<SignedLocationEndorsement> checkEndorsement(StreamObserver<LocationCertificate> responseObserver) {
        return new StreamObserver<>() {
            int endorsementCount;
            String claimId;
            List<String> endorseIds = new ArrayList<>();
            @Override
            public void onNext(SignedLocationEndorsement value) {
                try {
                    endorsementVerifier.verifyLocationEndorsement(value);
                    endorsementCount++;
                    LocationEndorsement endorsement = value.getEndorsement();
                    claimId = endorsement.getClaimId();
                    endorseIds.add(""); // TODO
                } catch (UnrecoverableKeyException e) {
                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
                } catch (NoSuchPaddingException e) {
                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
                } catch (IllegalBlockSizeException e) {
                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
                } catch (KeyStoreException e) {
                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
                } catch (NoSuchAlgorithmException e) {
                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
                } catch (BadPaddingException e) {
                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
                } catch (InvalidKeyException e) {
                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
                } catch (SignatureException e) {
                    throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
                }
            }

            @Override
            public void onError(Throwable t) {
                throw new VerifierException(ErrorMessage.LOCATION_ENDORSEMENT_CONN_ERROR);
            }

            @Override
            public void onCompleted() {
                if (endorsementCount > 0) {
                    // TODO: Use repeated field builder
                    String endorsementIds = ""; // TODO: There are no endorsement IDs
                    responseObserver.onNext(endorsementVerifier.buildCertificate(claimId, endorsementIds));
                }
                responseObserver.onCompleted();
            }
        };
    }

}
