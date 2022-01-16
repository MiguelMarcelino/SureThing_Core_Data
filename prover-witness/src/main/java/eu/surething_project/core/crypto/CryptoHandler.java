package eu.surething_project.core.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Service
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
    public long createNonce() {
        SecureRandom sr = new SecureRandom();
        long nonce = sr.nextLong();
        return nonce;
    }

    /**
     * Signs a nonce using the Private Key and the given cryptoAlgorithm
     * @param data - The data to sign
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyStoreException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public byte[] signData(byte[] data, String cryptoAlgorithm) throws NoSuchAlgorithmException, SignatureException {
        Signature sig = Signature.getInstance(cryptoAlgorithm);
//        PrivateKey key = (PrivateKey) ks.getKey("prover",
//                ksPassword.toCharArray());
//        sig.initSign(key);
        sig.update(data);
        return sig.sign();
    }

    public boolean verifyData(byte[] data, byte[] signedData, String cryptoAlgorithm) throws NoSuchAlgorithmException, SignatureException {
        Signature sig = Signature.getInstance(cryptoAlgorithm);
        sig.update(data);
        return sig.verify(signedData);
    }

}
