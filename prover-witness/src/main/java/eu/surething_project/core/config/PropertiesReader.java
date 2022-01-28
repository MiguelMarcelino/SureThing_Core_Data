package eu.surething_project.core.config;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;

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

    /**
     * Loads properties file
     *
     * @return - loaded properties
     */
    private static Properties loadProperties(String propertiesFile) {
        Properties prop;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(propertiesFile);
            prop = new Properties();
            prop.load(fis);
        } catch (FileNotFoundException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } catch (IOException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        } finally {
            try {
                if (fis != null) {
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
     *
     * @param propertyName
     * @return - value of property
     */
    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    /**
     * Gets value of System debug property
     * Use with: -DdetailedDebugMode=true
     *
     * @return - value of debug property
     */
    public static boolean getDebugProperty() {
        return DEBUG_PROPERTY != null &&
                "true".equalsIgnoreCase(DEBUG_PROPERTY);
    }
}
