package eu.surething_project.core.crypto;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;

import java.io.*;

public class CertificateAccess {

    public static byte[] getCertificateContentAsBytes(String path, String filename) {
        FileInputStream fis = null;
        File file;
        byte[] data;
        try {
            file = new File(path, filename + ".crt");
            if (!file.exists() || !file.isFile()) {
                throw new EntityException(ErrorMessage.ERROR_READING_CERTIFICATE);
            }
            fis = new FileInputStream(file);
            data = fis.readAllBytes();
        } catch (FileNotFoundException e) {
            throw new EntityException(ErrorMessage.ERROR_READING_CERTIFICATE, e);
        } catch (IOException e) {
            throw new EntityException(ErrorMessage.ERROR_READING_CERTIFICATE, e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new EntityException(ErrorMessage.ERROR_READING_CERTIFICATE, e);
                }
            }
        }
        return data;
    }

    /**
     *
     * @param path
     * @param filename
     * @param content
     * @return - true if the certificate was created
     */
    public static boolean createCertificateFile(String path, String filename, byte[] content) {
        FileOutputStream fos = null;
        File file;
        try{
            file = new File(path + "/" + filename, filename + ".crt");
            if (file.exists()) {
                return true;
            }

            // Create directories leading up to file
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

            fos = new FileOutputStream(file);
            fos.write(content);
        } catch (IOException e) {
            throw new EntityException(ErrorMessage.ERROR_CREATING_CERTIFICATE, e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new EntityException(ErrorMessage.ERROR_CREATING_CERTIFICATE, e);
                }
            }
        }
        return true;
    }
}
