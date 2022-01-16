package eu.surething_project.core.crypto;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class CryptoHandler {

    private KeyStore ks;

    @Value("verifier.keystore.password")
    private String ksPassword;

    @Value("${verifier.keystore.repository}")
    private String clientKeystoreRepo;

    public CryptoHandler(String keystoreName,
                         String keystorePassword)
            throws KeyStoreException, FileNotFoundException, CertificateException,
            NoSuchAlgorithmException, IOException {
        this.ks = KeyStore.getInstance("JCEKS");
        File keystoreFile = new File(clientKeystoreRepo, keystoreName);
        this.ks = KeyStore.getInstance("JCEKS");
        this.ks.load(new FileInputStream(keystoreFile),
                keystorePassword.toCharArray());
        this.ksPassword = keystorePassword;
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
        sig.update(data);
        return sig.sign();
    }

    public boolean verifyData(byte[] data, byte[] signedData, String cryptoAlgorithm) throws NoSuchAlgorithmException, SignatureException {
        Signature sig = Signature.getInstance(cryptoAlgorithm);
        sig.update(data);
        return sig.verify(signedData);
    }

    /**
     * Decrypts data with the private key
     * @param encryptedData
     * @param alias
     * @return
     * @throws UnrecoverableKeyException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] decryptDataAssym(byte[] encryptedData, String alias) throws UnrecoverableKeyException,
            KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        PrivateKey key = (PrivateKey) ks.getKey(alias,
                ksPassword.toCharArray());
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.WRAP_MODE, key);
        return c.doFinal(encryptedData);
    }
}
