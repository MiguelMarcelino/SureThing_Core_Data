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
 * Communicates with the Prover and sends a LocationCertificate
 * if his location is approved
 */
public class CertifyClaimService extends CertifyClaimGrpc.CertifyClaimImplBase {

    @Autowired
    private LocationProofVerifier endorsementVerifier;

    @Override
    public void checkLocationProof(SignedLocationProof locationProof,
                                   StreamObserver<LocationCertificate> responseObserver) {
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


}
