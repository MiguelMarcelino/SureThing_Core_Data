package eu.surething_project.core;

import eu.surething_project.core.config.AddressValidator;
import eu.surething_project.core.config.PropertiesReader;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.location_simulation.EndorsementHandler;
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
import java.util.List;
import java.util.logging.Logger;

public class EntityApplication {

    private static final Logger logger = Logger.getLogger(EntityApplication.class.getName());

    private static final String CRYPTO_ALGO = "SHA256withRSA"; // TODO: Request as input

    private static final String PROPERTIES = "src/main/java/eu/surething_project/core/application.properties";

    private static CryptoHandler cryptoHandler;

    // TODO:
    // - Receive and store endorsement (for later use when needed, maybe in a list or HashMap)
    // - Using nonce for freshness verification (probably before RPC begins)
    // - How to Broadcast using gRPC? (To multiple witnesses)
    public static void main(String[] args) {
        // Read properties
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        PropertiesReader prop = new PropertiesReader(PROPERTIES);
        String entityStorage = prop.getProperty("entity.storage");
        String securityStorage = prop.getProperty("entity.storage.security");

        // Check the args
        checkArgs(args, securityStorage, entityStorage);

        // Read args
        final String entityId = args[0];
        final int witnessGrpcPort = Integer.parseInt(args[1]);
        final String keystoreName = args[2];
        final String keystorePassword = args[3];

        // Create CryptoHandler
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
        entityManager.readEntityFile("data/" + entityId, "/entityData.txt");

        // Sets up Endorsement Handler to store endorsements
        EndorsementHandler endorsementHandler = new EndorsementHandler();

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
                System.out.println("Error receiving message");
            }

            String[] inputMsg = cmd.split("\"");
            String[] inputs = inputMsg[0].split(" ");

            if (inputs[0].equals("send_proof")) {
                // send_proof address:port id
                // Example: send_proof localhost:8081 witness

                // Receive and validate input
                AddressValidator.validateAddress(inputs[1]);
                String[] ipValues = inputs[1].split(":");
                String address = ipValues[0];
                int port = Integer.parseInt(ipValues[1]);
                String wId = inputs[2];

                // create witness entity
                Entity entity = new Entity(wId, address, port, new LatLongPair(82.5, 83.4));

                // communicate with witness
                SignedLocationEndorsement endorsement = sendWitnessData(entity, entityId);

                // Add endorsement to received endorsements list
                endorsementHandler.addLocationEndorsement(endorsement);

                // Just testing
                System.out.println(endorsement.getEndorsement().getClaimId());
            } else if (inputs[0].equals("broadcast_proof")) {
                // Get entities within range of current entity
                // entityManager.getEntitiesInRange(new LatLongPair(82.5, 83.4));
                List<Entity> entities = entityManager.getEntities(); // testing
                for (Entity entity : entities) {
                    // communicate with witness
                    SignedLocationEndorsement endorsement = sendWitnessData(entity, entityId);

                    // Add endorsement to received endorsements list
                    endorsementHandler.addLocationEndorsement(endorsement);

                    // Just testing
                    System.out.println(endorsement.getEndorsement().getClaimId());
                }
            } else if(inputs[0].equals("send_proof_verifier")) {

            }
        }
    }

    private static SignedLocationEndorsement sendWitnessData(Entity entity, String entityId) {
        // communicate with witness
        ProverWitnessCommHandler proverWitnessComm = new ProverWitnessCommHandler(entity);

        // send a location claim from this entity
        LocationClaimBuilder builder = new LocationClaimBuilder(cryptoHandler, entityId);

        SignedLocationEndorsement endorsement = null;
        try {
            SignedLocationClaim claim = builder.buildSignedLocationClaim(CRYPTO_ALGO);
            endorsement = proverWitnessComm.sendWitnessData(claim);

        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new EntityException(ErrorMessage.ERROR_SIGNING_DATA, e);
        } catch (KeyStoreException | InvalidKeyException | UnrecoverableKeyException e) {
            throw new EntityException(ErrorMessage.ERROR_ENCRYPTING_DATA, e);
        } catch (InterruptedException e) {
            throw new EntityException(ErrorMessage.GRPC_CONNECTION_ERROR);
        }

        return endorsement;
    }

    /**
     * Verifier the application arguments
     *
     * @param args
     */
    private static void checkArgs(String[] args, String securityStorage, String entityStorage) {
        // ID, address:port, keystore_name, keystore_password
        if (args.length != 4)
            throw new EntityException(ErrorMessage.INVALID_ARGS_LENGTH);

        AddressValidator.validatePort(args[1]);

        // Validate if KeyStore Exists
        String entityId = args[0];
        File truststoreFile = new File(entityStorage + "/" +
                entityId + "/" + securityStorage, args[2] + ".jks");
        System.out.println(truststoreFile.getAbsolutePath());
        if (!truststoreFile.exists()) {
            logger.severe("Keystore file was not found: " + args[2]);
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
