package eu.surething_project.core.config;

import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private static final String PROPERTIES = "src/main/java/eu/surething_project/core/application.properties";

    private static final String DEBUG_PROPERTY = System.getProperty("detailedDebugMode");

    private static Properties properties;

    static {
        properties = loadProperties();
    }

    private static Properties loadProperties() {
        Properties prop;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(PROPERTIES);
            prop = new Properties();
            prop.load(fis);
        } catch (FileNotFoundException e) {
            throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } catch (IOException e) {
            throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } finally {
            try {
                if(fis!=null) {
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
