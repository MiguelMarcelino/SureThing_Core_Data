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
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class CryptoHandler {

    private KeyStore ks;

    @Value("verifier.keystore.password")
    private String ksPassword;

    @Value("verifier.keystore.name")
    private String ksName;

    @Value("${verifier.keystore.privKeyAlias}")
    private String privKeyAlias;

    @Value("${verifier.keystore.repository}")
    private String clientKeystoreRepo;

    @Value("${verifier.certificate.repository}")
    private String certificateRepository;

    public CryptoHandler()
            throws KeyStoreException, FileNotFoundException, CertificateException,
            NoSuchAlgorithmException, IOException {
        this.ks = KeyStore.getInstance("JCEKS");
        File keystoreFile = new File(clientKeystoreRepo, ksName);
        this.ks = KeyStore.getInstance("JCEKS");
        this.ks.load(new FileInputStream(keystoreFile),
                ksPassword.toCharArray());
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
    public byte[] signData(byte[] data, String cryptoAlgorithm)
            throws NoSuchAlgorithmException, SignatureException, UnrecoverableKeyException,
            KeyStoreException, InvalidKeyException {
        Signature sig = Signature.getInstance(cryptoAlgorithm);
        PrivateKey key = (PrivateKey) ks.getKey(privKeyAlias,
                ksPassword.toCharArray());
        sig.initSign(key);
        sig.update(data);
        return sig.sign();
    }

    /**
     *
     * @param data
     * @param signedData
     * @param certName
     * @param cryptoAlgorithm
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws CertificateException
     * @throws FileNotFoundException
     */
    public boolean verifyData(byte[] data, byte[] signedData, String certName, String cryptoAlgorithm)
            throws NoSuchAlgorithmException, SignatureException, CertificateException,
            FileNotFoundException {
        Signature sig = Signature.getInstance(cryptoAlgorithm);
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        File certFile = new File(certificateRepository,
                certName + "_certificate.cer");
        Certificate cert = cf
                .generateCertificate(new FileInputStream(certFile));
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
    public byte[] decryptDataAssym(byte[] encryptedData, String alias, String cryptoAlg) throws UnrecoverableKeyException,
            KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        PrivateKey key = (PrivateKey) ks.getKey(alias,
                ksPassword.toCharArray());
        Cipher c = Cipher.getInstance(cryptoAlg);
        c.init(Cipher.DECRYPT_MODE, key);
        return c.doFinal(encryptedData);
    }
}
