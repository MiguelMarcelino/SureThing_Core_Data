package eu.surething_project.core.rpc_comm.witness;

import eu.surething_project.core.crypto.CryptoHandler;
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

    private LocationClaimVerifier claimVerifier;

    public EndorseClaimService(CryptoHandler cryptoHandler) {
        this.claimVerifier = new LocationClaimVerifier(cryptoHandler);
    }

    /**
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void checkClaim(SignedLocationClaim request, StreamObserver<SignedLocationEndorsement> responseObserver) {
        try {
            responseObserver.onNext(claimVerifier.verifyLocationClaim(request));
        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new EntityException(ErrorMessage.ERROR_SIGNING_DATA, e);
        } catch (FileNotFoundException | NoSuchPaddingException | IllegalBlockSizeException |
                CertificateException | BadPaddingException | InvalidKeyException e) {
            throw new EntityException(ErrorMessage.ERROR_ENCRYPTING_DATA, e);
        }
        responseObserver.onCompleted();
    }



}
