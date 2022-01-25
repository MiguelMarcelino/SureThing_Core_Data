package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.crypto.CertificateAccess;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.LocationVerification;
import eu.surething_project.core.grpc.Signature;

import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;

public class LocationCertificateVerifier {

    private CryptoHandler cryptoHandler;

    private String externalData;

    public LocationCertificateVerifier(CryptoHandler cryptoHandler, String externalData) {
        this.cryptoHandler = cryptoHandler;
        this.externalData = externalData;
    }

    /**
     * Verifies Certificate Validity
     * @param sentNonce
     * @param locationCertificate
     * @throws FileNotFoundException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws NoSuchProviderException
     */
    public void verifyCertificate(long sentNonce, LocationCertificate locationCertificate)
            throws FileNotFoundException, CertificateException, NoSuchAlgorithmException,
            SignatureException, InvalidKeyException, NoSuchProviderException {
        // Get signed data
        Signature signature = locationCertificate.getVerifierSignature();
        String cryptoAlg = signature.getCryptoAlgo();
        String verifierId = locationCertificate.getVerification().getVerifierId();
        byte[] certData = signature.getCertificateData().toByteArray();
        byte[] signedVerification = signature.getValue().toByteArray();

        // Get verification
        LocationVerification verification = locationCertificate.getVerification();

        // Create Certificate if necessary
        boolean certFileCreate = CertificateAccess.createCertificateFile(externalData, verifierId, certData);

        // create Certificate and verify validity
        cryptoHandler.verifyCertificate(verifierId);

        cryptoHandler.verifyData(verification.toByteArray(), signedVerification, verifierId, cryptoAlg);

        long nonce = signature.getNonce();
        if (nonce != sentNonce) {
            throw new EntityException(ErrorMessage.NONCE_MATCH_ERROR);
        }
    }
}
