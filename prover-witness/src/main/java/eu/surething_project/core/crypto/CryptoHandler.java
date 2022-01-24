package eu.surething_project.core.crypto;

import eu.surething_project.core.config.PropertiesReader;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;

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
    private String ksPassword;
    private final String entityId;
    private String entityStorage;
    private String securityStorage;
    private String certificateRepository;
    private String entityExternalStorage;

    public CryptoHandler(String entityID, String keystoreName, String ksPassword, PropertiesReader prop)
            throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {
        // Read Properties
        entityStorage = prop.getProperty("entity.storage");
        securityStorage = prop.getProperty("entity.storage.security");
        certificateRepository = prop.getProperty("entity.storage.certificates");
        entityExternalStorage = prop.getProperty("entity.storage.external");

        // Setup keystore
        this.ks = KeyStore.getInstance("JCEKS");
        this.ksPassword = ksPassword;
        this.entityId = entityID;
        File keystoreFile = new File(entityStorage + "/" +
                entityId + "/" + securityStorage, keystoreName + ".jks");
        this.ks = KeyStore.getInstance("JCEKS");
        this.ks.load(new FileInputStream(keystoreFile),
                ksPassword.toCharArray());

    }

    /**
     * Creates a nonce
     *
     * @return
     */
    public long createNonce() {
        SecureRandom sr = new SecureRandom();
        long nonce = sr.nextLong();
        return nonce;
    }

    /**
     * Signs data using the Private Key and the given cryptoAlgorithm
     *
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
        PrivateKey key = (PrivateKey) ks.getKey(entityId,
                ksPassword.toCharArray());
        sig.initSign(key);
        sig.update(data);
        return sig.sign();
    }

    /**
     * @param data
     * @param signedData
     * @param cryptoAlgorithm
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws CertificateException
     * @throws FileNotFoundException
     */
    public boolean verifyData(byte[] data, byte[] signedData, String externalEntityId, String cryptoAlgorithm)
            throws NoSuchAlgorithmException, SignatureException, CertificateException,
            FileNotFoundException, InvalidKeyException {
        Signature sig = Signature.getInstance(cryptoAlgorithm);
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        File certFile = new File(entityStorage + "/" + entityId + "/" + entityExternalStorage + "/"
                + externalEntityId, externalEntityId + ".crt");
        Certificate cert = cf
                .generateCertificate(new FileInputStream(certFile));
        sig.initVerify(cert.getPublicKey());
        sig.update(data);
        return sig.verify(signedData);
    }

    public void verifyCertificate(String externalEntity) throws CertificateException,
            FileNotFoundException, NoSuchAlgorithmException, SignatureException, InvalidKeyException,
            NoSuchProviderException {
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        // Get root CA certificate
        File rootCACert = new File(entityStorage + "/" + entityId + "/"  + entityExternalStorage
                + "/root", "rootCA.crt");
        Certificate rootCert = cf
                .generateCertificate(new FileInputStream(rootCACert));

        // Get user certificate
        File certFile = new File(entityStorage + "/" + entityId + "/" +  entityExternalStorage + "/"
                + externalEntity, externalEntity + ".crt");
        Certificate cert = cf
                .generateCertificate(new FileInputStream(certFile));

        cert.verify(rootCert.getPublicKey());
    }

    /**
     * Encrypts data for sending
     *
     * @param data
     * @param certName
     * @param cryptoAlg
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws FileNotFoundException
     * @throws CertificateException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] encryptDataAssym(byte[] data, String certName, String cryptoAlg)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            FileNotFoundException, CertificateException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        File certFile = new File(entityStorage + entityId + certificateRepository,
                certName + "_certificate.cer");
        Certificate cert = cf
                .generateCertificate(new FileInputStream(certFile));

        Cipher c = Cipher.getInstance(cryptoAlg);
        c.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());
        return c.doFinal(data);
    }

    public File getCertFile() {
        File certFile = new File(entityStorage + "/" + entityId + "/" + securityStorage,
                entityId + ".crt");
        if(!certFile.exists()) {
            throw new EntityException(ErrorMessage.ERROR_GETTING_CERTIFICATE);
        }
        return certFile;
    }

    public File getRootCertificate() {
        File certFile = new File(entityStorage + "/" + entityId + "/" + entityExternalStorage +
                "/root", "rootCA.crt");
        if(!certFile.exists()) {
            throw new EntityException(ErrorMessage.ERROR_GETTING_CERTIFICATE);
        }
        return certFile;
    }

    public File getPrivateKeyFile() {
        File privKeyFile = new File(entityStorage + "/" + entityId + "/" + securityStorage,
                "key_" + entityId + ".pem");
        if(!privKeyFile.exists()) {
            throw new EntityException(ErrorMessage.ERROR_GETTING_KEY_FILE);
        }

        return privKeyFile;
    }

    public File getExternalCertificate(String witnessId) {
        File certFile = new File(entityStorage + "/" + entityId + "/" + entityExternalStorage + "/" +
                witnessId, witnessId + ".crt");
        System.out.println(certFile.getAbsolutePath());
        if(!certFile.exists()) {
            throw new EntityException(ErrorMessage.ERROR_GETTING_CERTIFICATE);
        }

        return certFile;
    }
}
