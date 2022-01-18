package eu.surething_project.core;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.rpc_comm.witness.WitnessGrpcServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class EntityApplication {

    private static final Logger logger = LoggerFactory.getLogger(EntityApplication.class);

    @Value("{entity.storage}")
    private static String entityStorage;

    @Value("${entity.storage.security}")
    private static String securityStorage;

	private static final String CIPHER_ALGO = "AES/ECB/PKCS5Padding"; // TODO: Request as input

    // TODO:
    // - Receive and store endorsement (for later use when needed, maybe in a list or HashMap)
    // - Using nonce for freshness verification (probably before RPC begins)
    // - How to Broadcast using gRPC? (To multiple witnesses)
    public static void main(String[] args) {
        // Check the args
        checkArgs(args);

        // Read args
        final String entityId = args[0];
        String[] ipPort = args[1].split(":");
        final String witnessAddress = ipPort[0];
        final int witnessGrpcPort = Integer.parseInt(ipPort[1]);
        final String keystoreName = args[3];
        final String keystorePassword = args[4];

        // Create CryptoHandler
        CryptoHandler cryptoHandler;
        try {
            cryptoHandler = new CryptoHandler(entityId, keystoreName, keystorePassword);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        }

        /**********************************/
        // Keystore Properties
        System.setProperty("javax.net.ssl.keyStoreType", "JCEKS");
        System.setProperty("javax.net.ssl.keyStore",
                entityStorage + "/" + entityId + "/" + keystoreName);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
        /**********************************/

        // Create Witness server
        WitnessGrpcServerHandler serverHandler = new WitnessGrpcServerHandler(cryptoHandler);
        try {
            serverHandler.buildServer(witnessGrpcPort, entityId);
        } catch (InterruptedException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        }

		// apresentar menu
		printOptions();

        // Receive user input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print(">> ");
			String cmd = "";

			try {
				while (!br.ready()) {
					Thread.sleep(200);
				}
				cmd = br.readLine();
			} catch (IOException | InterruptedException e) {
				System.out.println("ERRO ao receber input. Tente novamente.");
			}

			String[] inputMsg = cmd.split("\"");
			String[] inputs = inputMsg[0].split(" ");

			if(inputs[0].equals("sendproof")) {
				// TODO
			}
		}
    }

    /**
     * Verifier the application arguments
     * @param args
     */
    private static void checkArgs(String[] args) {
        // ID, address:port, keystore_name, keystore_password
        if (args.length != 4)
            throw new EntityException(ErrorMessage.INVALID_ARGS_LENGTH);

        String[] ipPort = args[1].split(":");
        if (ipPort.length != 2) {
            logger.error("Invalid address");
            throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
        }

        if (!ipPort[0].equals("localhost")) {
            String[] ipValues = ipPort[0].split("[.]");
            // validate address
            if (ipValues.length != 4) {
                logger.error("Invalid address length: " + ipValues.length);
                throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
            }

            for (String value : ipValues) {
                int ipValue = Integer.parseInt(value);
                if (ipValue < 0 || ipValue > 255)
                    logger.error("Invalid IP Address Value: " + ipValue);
                throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
            }
        }

        // Validate Port
        int portValue = Integer.parseInt(ipPort[1]);
        if (portValue < 1024 || portValue > 65535) {
            logger.error("Invalid Port: " + portValue);
            throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
        }

        // Validate if KeyStore Exists
        String entityId = args[0];
        File truststoreFile = new File(entityStorage +
                entityId + securityStorage, args[3]);
        if (!truststoreFile.exists()) {
            logger.error("Keystore file was not found: " + args[3]);
            throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
        }
    }

    /**
     * Prints the outputs to the user
     */
	private static void printOptions() {
		System.out.println("Please enter one of the following commands:");
	}
}
