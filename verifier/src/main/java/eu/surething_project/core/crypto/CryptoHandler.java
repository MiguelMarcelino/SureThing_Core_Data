package eu.surething_project.core.crypto;

import eu.surething_project.core.config.PropertiesReader;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.grpc.EndorseClaimGrpc;

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
     * Signs data using the Private Key and the given cryptoAlgorithm
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
     *
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

    public File getCertFile() {
        File certFile = new File(entityStorage + "/" + entityId + "/" + securityStorage,
                entityId + ".crt");
        if(!certFile.exists()) {
            throw new VerifierException(ErrorMessage.ERROR_GETTING_CERTIFICATE);
        }
        return certFile;
    }

    public File getPrivateKeyFile() {
        File privKeyFile = new File(entityStorage + "/" + entityId + "/" + securityStorage,
                "key_" + entityId + ".pem");
        if(!privKeyFile.exists()) {
            throw new VerifierException(ErrorMessage.ERROR_GETTING_KEY_FILE);
        }

        return privKeyFile;
    }
}
