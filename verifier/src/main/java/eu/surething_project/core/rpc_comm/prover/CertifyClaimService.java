package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.database.AsyncDatabaseWriter;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.grpc.CertifyClaimGrpc;
import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.SignedLocationProof;
import io.grpc.stub.StreamObserver;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Communicates with the Prover and sends a LocationCertificate
 * if his location is approved
 */
public class CertifyClaimService extends CertifyClaimGrpc.CertifyClaimImplBase {

    private LocationProofVerifier endorsementVerifier;

    public CertifyClaimService(CryptoHandler cryptoHandler, String verifierId,
                               String externalData, String certPath, AsyncDatabaseWriter databaseWriter) {
        endorsementVerifier = new LocationProofVerifier(cryptoHandler, verifierId, externalData,
                certPath, databaseWriter);
    }

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
        } catch (FileNotFoundException | CertificateException | NoSuchProviderException e) {
            throw new VerifierException(ErrorMessage.ERROR_GETTING_CERTIFICATE, e);
        }
        responseObserver.onCompleted();
    }


}
