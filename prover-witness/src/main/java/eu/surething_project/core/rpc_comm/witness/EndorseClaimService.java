package eu.surething_project.core.rpc_comm.witness;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.EndorseClaimGrpc;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;

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
        try {
            responseObserver.onNext(claimVerifier.verifyLocationClaim(request));
        } catch (NoSuchAlgorithmException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } catch (SignatureException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } catch (FileNotFoundException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } catch (NoSuchPaddingException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } catch (IllegalBlockSizeException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } catch (CertificateException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } catch (BadPaddingException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } catch (InvalidKeyException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        }
        responseObserver.onCompleted();
    }



}
