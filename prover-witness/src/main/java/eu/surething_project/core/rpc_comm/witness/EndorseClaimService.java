package eu.surething_project.core.rpc_comm.witness;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.EndorseClaimGrpc;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.location_simulation.Entity;
import io.grpc.stub.StreamObserver;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Witness service to endorse claim from a given prover
 */
public class EndorseClaimService extends EndorseClaimGrpc.EndorseClaimImplBase {

    private LocationClaimVerifier claimVerifier;

    public EndorseClaimService(CryptoHandler cryptoHandler, String certPath, String externalData,
                               String witnessId, Entity entity) {
        this.claimVerifier = new LocationClaimVerifier(cryptoHandler, certPath, externalData,
                witnessId, entity);
    }

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void checkClaim(SignedLocationClaim request, StreamObserver<SignedLocationEndorsement> responseObserver) {
        try {
            responseObserver.onNext(claimVerifier.verifyLocationClaim(request));
        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new EntityException(ErrorMessage.ERROR_SIGNING_DATA, e);
        } catch (FileNotFoundException | CertificateException | InvalidKeyException e) {
            throw new EntityException(ErrorMessage.ERROR_ENCRYPTING_DATA, e);
        } catch (UnrecoverableKeyException | KeyStoreException e) {
            throw new EntityException(ErrorMessage.ERROR_GETTING_KEYSTORE_KEY, e);
        } catch (NoSuchProviderException e) {
            throw new EntityException(ErrorMessage.ERROR_GETTING_CERTIFICATE, e);
        }
        responseObserver.onCompleted();
    }


}
