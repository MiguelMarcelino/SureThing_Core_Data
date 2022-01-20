package eu.surething_project.core.config;

import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Courtesy of: https://www.baeldung.com/java-accessing-maven-properties
 */
public class PropertiesReader {

    private static Properties properties;

    public PropertiesReader(String filename) {
        getProperties(filename);
    }

    private void getProperties(String fileName) {
        Properties prop = null;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(fileName);
            prop = new Properties();
            prop.load(fis);
        } catch (FileNotFoundException e) {
            throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG);
        } catch (IOException e) {
            throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG);
            }
        }

        properties = prop;
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
