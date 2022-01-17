package eu.surething_project.core.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

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

    @Value("client.keystore.password")
    private String ksPassword;

    @Value("client.keystore.name")
    private String ksName;

    @Value("${client.keystore.repository}")
    private String clientKeystoreRepo;

    @Value("${client.certificate.repository}")
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
        sig.update(data);
        return sig.sign();
    }

    public boolean verifyData(byte[] data, byte[] signedData, String cryptoAlgorithm) throws NoSuchAlgorithmException, SignatureException {
        Signature sig = Signature.getInstance(cryptoAlgorithm);
        sig.update(data);
        return sig.verify(signedData);
    }

    /**
     * Encrypts data for sending
     * @param data
     * @param alias
     * @return
     * @throws UnrecoverableKeyException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     */
    public byte[] encryptDataAssym(byte[] data, String alias)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            FileNotFoundException, CertificateException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        File certFile = new File(certificateRepository,
                alias + "_certificate.cer");
        Certificate cert = cf
                .generateCertificate(new FileInputStream(certFile));

        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.WRAP_MODE, cert.getPublicKey());
        return c.doFinal(data);
    }

}
