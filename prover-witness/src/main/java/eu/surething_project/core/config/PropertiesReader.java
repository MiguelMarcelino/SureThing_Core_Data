package eu.surething_project.core.config;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private static final String DEBUG_PROPERTY = System.getProperty("detailedDebugMode");

    private static final String PROPERTIES = "src/main/java/eu/surething_project/core/application.properties";

    private static Properties properties;

    static {
        properties = loadProperties();
    }

    /**
     * Loads properties
     * @return - loaded properties
     */
    private static Properties loadProperties() {
        Properties prop;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(PROPERTIES);
            prop = new Properties();
            prop.load(fis);
        } catch (FileNotFoundException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } catch (IOException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } finally {
            try {
                if(fis!=null) {
                    fis.close();
                }
            } catch (IOException e) {
                throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG);
            }
        }

        return prop;
    }

    /**
     * Gets property with given name
     * @param propertyName
     * @return - value of property
     */
    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    /**
     * Gets value of System debug property
     * @return - value of debug property
     */
    public static boolean getDebugProperty() {
        // Use with -DdetailedDebugMode=true
        return DEBUG_PROPERTY != null &&
                "true".equalsIgnoreCase(DEBUG_PROPERTY);
    }
}
