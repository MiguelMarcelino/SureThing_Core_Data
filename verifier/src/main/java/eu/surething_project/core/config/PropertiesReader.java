package eu.surething_project.core.config;

import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private static final String DEBUG_PROPERTY = System.getProperty("detailedDebugMode");

    private static final String EXECUTION_MODE = System.getProperty("externalRunMode");

    private static Properties properties;

    static {
        String propertiesFile;
        if (EXECUTION_MODE != null &&
                "true".equalsIgnoreCase(EXECUTION_MODE)) {
            propertiesFile = System.getProperty("user.dir") + "/application.properties";
        } else {
            propertiesFile = "src/main/java/eu/surething_project/core/application.properties";
        }

        properties = loadProperties(propertiesFile);
    }

    private static Properties loadProperties(String propertiesFile) {
        Properties prop;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(propertiesFile);
            prop = new Properties();
            prop.load(fis);
        } catch (FileNotFoundException e) {
            throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } catch (IOException e) {
            throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG);
            }
        }

        return prop;
    }

    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public static boolean getDebugProperty() {
        // Use with -DdetailedDebugMode=true
        return DEBUG_PROPERTY != null &&
                "true".equalsIgnoreCase(DEBUG_PROPERTY);
    }
}
