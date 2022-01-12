package eu.surething_project.core.crypto;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class CryptoHandler {

    private KeyStore ks;
    private String ksPassword;

    @Value("${client_repository}")
    private String clientRepository;

    public CryptoHandler(String keystoreName,
                         String keystorePassword)
            throws KeyStoreException, FileNotFoundException, CertificateException,
                NoSuchAlgorithmException, IOException {
        this.ks = KeyStore.getInstance("JCEKS");
        File keystoreFile = new File(clientRepository, keystoreName);
        this.ks = KeyStore.getInstance("JCEKS");
        this.ks.load(new FileInputStream(keystoreFile),
                keystorePassword.toCharArray());
        this.ksPassword = keystorePassword;
    }

    /**
     * Creates a nonce
     * @return
     */
    public byte[] createNonce() {
        SecureRandom sr = new SecureRandom();
        byte[] nonce = new byte[8];
        sr.nextBytes(nonce);
        return nonce;
    }

    /**
     * Signs a nonce using the Private Key and the SHA256withRSA Algorithm
     * @param nonce - The nonce to sign
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyStoreException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public byte[] signNonce(byte[] nonce) throws NoSuchAlgorithmException, UnrecoverableKeyException,
            KeyStoreException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA256withRSA");
        PrivateKey key = (PrivateKey) ks.getKey("prover",
                ksPassword.toCharArray());
        sig.initSign(key);
        sig.update(nonce);
        return sig.sign();
    }

    /**
     * This method sign the message to verify integrity
     * @param message
     * @return
     */
    public byte[] signMessage(byte[] message) {
        return null;
    }
}
