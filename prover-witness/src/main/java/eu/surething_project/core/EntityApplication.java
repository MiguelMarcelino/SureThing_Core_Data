package eu.surething_project.core;

import eu.surething_project.core.config.PropertiesReader;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.location_simulation.Entity;
import eu.surething_project.core.location_simulation.EntityManager;
import eu.surething_project.core.location_simulation.LatLongPair;
import eu.surething_project.core.rpc_comm.prover.LocationClaimBuilder;
import eu.surething_project.core.rpc_comm.prover.ProverWitnessCommHandler;
import eu.surething_project.core.rpc_comm.witness.WitnessGrpcServerHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.UUID;
import java.util.logging.Logger;

public class EntityApplication {

    private static final Logger logger = Logger.getLogger(EntityApplication.class.getName());

	private static final String CRYPTO_ALGO = "AES/ECB/PKCS5Padding"; // TODO: Request as input

    private static final String PROPERTIES = "application.properties";

    // TODO:
    // - Receive and store endorsement (for later use when needed, maybe in a list or HashMap)
    // - Using nonce for freshness verification (probably before RPC begins)
    // - How to Broadcast using gRPC? (To multiple witnesses)
    public static void main(String[] args) {
        // Read properties
        PropertiesReader prop = new PropertiesReader(PROPERTIES);
        String entityStorage = prop.getProperty("entity.storage");
        String securityStorage = prop.getProperty("entity.storage.security");

        // Check the args
        checkArgs(args, securityStorage, entityStorage);

        // Read args
        final String entityId = args[0];
        String[] ipPort = args[1].split(":");
        final String witnessAddress = ipPort[0];
        final int witnessGrpcPort = Integer.parseInt(ipPort[1]);
        final String keystoreName = args[2];
        final String keystorePassword = args[3];

        // Create CryptoHandler
        CryptoHandler cryptoHandler;
        try {
            cryptoHandler = new CryptoHandler(entityId, keystoreName, keystorePassword, prop);
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

        // Sets up entity Manager and reads entity data from a file
        EntityManager entityManager = new EntityManager();
        entityManager.readEntityFile("/data/entityData.txt");

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

			if(inputs[0].equals("send_proof")) {
                // send_proof address:port id
                // Example: send_proof localhost8081 witness
                validateAddress(inputs);
                String[] ipValues = ipPort[1].split("[.]");
                String address = ipValues[0];
                int port = Integer.parseInt(ipValues[1]);
                String wId = inputs[2];
                Entity entity = new Entity(wId, address, port, new LatLongPair(82.5, 83.4));

                ProverWitnessCommHandler proverWitnessComm = new ProverWitnessCommHandler(entity);

                // send a location claim from this entity
                LocationClaimBuilder builder = new LocationClaimBuilder(cryptoHandler, entityId);
                UUID uuid = UUID.randomUUID();
                SignedLocationEndorsement endorsement = null;
                try {
                    SignedLocationClaim claim = builder.buildSignedLocationClaim(uuid.toString(), CRYPTO_ALGO);
                    endorsement = proverWitnessComm.sendWitnessData(claim);
                } catch (NoSuchAlgorithmException | SignatureException e) {
                    throw new EntityException(ErrorMessage.ERROR_SIGNING_DATA, e);
                }  catch (KeyStoreException | InvalidKeyException | UnrecoverableKeyException e) {
                    throw new EntityException(ErrorMessage.ERROR_ENCRYPTING_DATA, e);
                } catch (InterruptedException e) {
                    throw new EntityException(ErrorMessage.GRPC_CONNECTION_ERROR);
                }

                // Just testing (later requires storing)
                System.out.println(endorsement.getEndorsement().getClaimId());
            } else if(inputs[0].equals("broadcast_proof")) {
                // Get entities within range of current entity
                entityManager.getEntitiesInRange(new LatLongPair(82.5, 83.4));
            }
		}
    }

    /**
     * Verifier the application arguments
     * @param args
     */
    private static void checkArgs(String[] args, String securityStorage, String entityStorage) {
        // ID, address:port, keystore_name, keystore_password
        if (args.length != 4)
            throw new EntityException(ErrorMessage.INVALID_ARGS_LENGTH);

        validateAddress(args);

        // Validate if KeyStore Exists
        String entityId = args[0];
        File truststoreFile = new File(entityStorage +
                entityId + securityStorage, args[3]);
        if (!truststoreFile.exists()) {
            logger.severe("Keystore file was not found: " + args[3]);
            throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
        }
    }

    private static void validateAddress(String[] args) {
        String[] ipPort = args[1].split(":");
        if (ipPort.length != 2) {
            logger.severe("Invalid address");
            throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
        }

        if (!ipPort[0].equals("localhost")) {
            String[] ipValues = ipPort[0].split("[.]");
            // validate address
            if (ipValues.length != 4) {
                logger.severe("Invalid address length: " + ipValues.length);
                throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
            }

            for (String value : ipValues) {
                int ipValue = Integer.parseInt(value);
                if (ipValue < 0 || ipValue > 255)
                    logger.severe("Invalid IP Address Value: " + ipValue);
                throw new EntityException(ErrorMessage.INVALID_ARGS_DATA);
            }
        }

        // Validate Port
        int portValue = Integer.parseInt(ipPort[1]);
        if (portValue < 1024 || portValue > 65535) {
            logger.severe("Invalid Port: " + portValue);
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
