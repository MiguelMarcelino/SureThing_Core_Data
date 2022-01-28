package eu.surething_project.core.config;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;

import java.util.logging.Logger;

public class AddressValidator {
    private static final Logger logger = Logger.getLogger(AddressValidator.class.getName());

    /**
     * Validates an IP address
     *
     * @param address
     */
    public static void validateAddress(String address) {
        String[] ipPort = address.split(":");
        if (ipPort.length != 2) {
            logger.severe("Invalid address");
            throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
        }

        if (!ipPort[0].equals("localhost")) {
            String[] ipValues = ipPort[0].split("\\.");
            // validate address
            if (ipValues.length != 4) {
                logger.severe("Invalid address length: " + ipValues.length);
                throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
            }

            for (String value : ipValues) {
                int ipValue = Integer.parseInt(value);
                if (ipValue < 0 || ipValue > 255) {
                    logger.severe("Invalid IP Address Value: " + ipValue);
                    throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
                }
            }
        }

        // Validate Port
        validatePort(ipPort[1]);
    }

    /**
     * Validates a port
     *
     * @param port
     */
    public static void validatePort(String port) {
        int portValue = Integer.parseInt(port);
        if (portValue < 1024 || portValue > 65535) {
            logger.severe("Invalid Port: " + portValue);
            throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
        }
    }
}
