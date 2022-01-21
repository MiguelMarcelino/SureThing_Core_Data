package eu.surething_project.core;

import eu.surething_project.core.config.AddressValidator;
import eu.surething_project.core.config.PropertiesReader;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.grpc.SignedLocationProof;
import eu.surething_project.core.location_simulation.*;
import eu.surething_project.core.rpc_comm.prover.LocationClaimBuilder;
import eu.surething_project.core.rpc_comm.prover.ProverWitnessCommHandler;
import eu.surething_project.core.rpc_comm.prover_verifier.LocationProofBuilder;
import eu.surething_project.core.rpc_comm.prover_verifier.ProverVerifierCommHandler;
import eu.surething_project.core.rpc_comm.witness.WitnessGrpcServerHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
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
        final String currentEntityId = args[0];
        final int witnessGrpcPort = Integer.parseInt(args[1]);
        final String keystoreName = args[2];
        final String keystorePassword = args[3];

        // Create CryptoHandler
        try {
            cryptoHandler = new CryptoHandler(currentEntityId, keystoreName, keystorePassword, prop);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        }

        /**********************************/
        // Keystore Properties
        System.setProperty("javax.net.ssl.keyStoreType", "JCEKS");
        System.setProperty("javax.net.ssl.keyStore",
                entityStorage + "/" + currentEntityId + "/" + keystoreName);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
        /**********************************/

        // Create Witness server
        WitnessGrpcServerHandler serverHandler = new WitnessGrpcServerHandler(cryptoHandler);
        try {
            serverHandler.buildServer(witnessGrpcPort, currentEntityId);
        } catch (InterruptedException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        }

        // Sets up entity Manager and reads entity data from a file
        EntityManager entityManager = new EntityManager();
        entityManager.readEntityFile("data/" + currentEntityId, "/entityData.txt");

        // Sets up Endorsement Handler to store endorsements
        LocationDataHandler locationDataHandler = new LocationDataHandler();

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

            if (inputs[0].equals("send_claim")) {
                // send_claim address:port id
                // Example: send_claim localhost:8081 witness

                // Receive and validate input
                AddressValidator.validateAddress(inputs[1]);
                String[] ipValues = inputs[1].split(":");
                String address = ipValues[0];
                int port = Integer.parseInt(ipValues[1]);
                String wId = inputs[2];

                // create witness entity
                Entity entity = new Entity(wId, address, port, new LatLongPair(82.5, 83.4));

                // communicate with witness
                SignedLocationClaim claim = buildLocationClaim(currentEntityId);
                SignedLocationEndorsement endorsement = sendWitnessData(entity, claim);

                // Add endorsement to received endorsements list
                locationDataHandler.addLocationEndorsement(claim, endorsement);

                // Just testing
                System.out.println(endorsement.getEndorsement().getClaimId());
            } else if (inputs[0].equals("broadcast_proof")) {
                // Get entities within range of current entity
                // entityManager.getEntitiesInRange(new LatLongPair(82.5, 83.4));
                List<Entity> entities = entityManager.getEntities(); // testing

                // build location claim to send to multiple witnesses
                SignedLocationClaim claim = buildLocationClaim(currentEntityId);
                for (Entity entity : entities) {
                    SignedLocationEndorsement endorsement = sendWitnessData(entity, claim);

                    // Add endorsement to received endorsements list
                    locationDataHandler.addLocationEndorsement(claim, endorsement);

                    // Just testing
                    System.out.println(endorsement.getEndorsement().getClaimId());
                }
            } else if(inputs[0].equals("send_proof_verifier")) {
                // send_proof_verifier address:port id
                // Example: send_proof_verifier localhost:8082 verifier
                // Receive and validate input
                AddressValidator.validateAddress(inputs[1]);
                String[] ipValues = inputs[1].split(":");
                String address = ipValues[0];
                int port = Integer.parseInt(ipValues[1]);
                String pId = inputs[2];

                for (String claimId : locationDataHandler.getClaims().keySet()) {
                    List<SignedLocationEndorsement> endorsementList = locationDataHandler.getEndorsementList(claimId);
                    SignedLocationProof proof = buildLocationProof(
                            locationDataHandler.getClaim(claimId), endorsementList);
                    LocationCertificate certificate = sendLocationProof(address, port, proof);
                    locationDataHandler.addLocationCertificate(claimId, certificate);
                }

            }
        }
    }

    private static SignedLocationClaim buildLocationClaim(String currentEntityId) {
        // send a location claim from this entity
        LocationClaimBuilder builder = new LocationClaimBuilder(cryptoHandler, currentEntityId);
        SignedLocationClaim claim = null;
        try {
            claim = builder.buildSignedLocationClaim(CRYPTO_ALGO);
        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new EntityException(ErrorMessage.ERROR_SIGNING_DATA, e);
        } catch (KeyStoreException | InvalidKeyException | UnrecoverableKeyException e) {
            throw new EntityException(ErrorMessage.ERROR_ENCRYPTING_DATA, e);
        }
        return claim;
    }

    /**
     *
     * @param entity - The witness entity to send the data
     * @param claim - The claim to send
     * @return
     */
    private static SignedLocationEndorsement sendWitnessData(
            Entity entity, SignedLocationClaim claim) {
        // communicate with witness
        ProverWitnessCommHandler proverWitnessComm = new ProverWitnessCommHandler(entity);

        SignedLocationEndorsement endorsement;
        try {
            endorsement = proverWitnessComm.sendWitnessData(claim);
        } catch (InterruptedException e) {
            throw new EntityException(ErrorMessage.GRPC_CONNECTION_ERROR);
        }

        return endorsement;
    }

    private static SignedLocationProof buildLocationProof(SignedLocationClaim claim,
                                                          List<SignedLocationEndorsement> endorsementList) {
        LocationProofBuilder builder = new LocationProofBuilder(cryptoHandler);

        SignedLocationProof proof;
        try {
            proof = builder.buildSignedLocationProof(claim, endorsementList);
        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new EntityException(ErrorMessage.ERROR_SIGNING_DATA, e);
        } catch (KeyStoreException | InvalidKeyException | UnrecoverableKeyException e) {
            throw new EntityException(ErrorMessage.ERROR_ENCRYPTING_DATA, e);
        }
        return proof;
    }

    /**
     *
     * @param verifierAddress
     * @param verifierPort
     * @return
     */
    private static LocationCertificate sendLocationProof(String verifierAddress, int verifierPort,
                                                         SignedLocationProof proof) {
        ProverVerifierCommHandler commHandler = new ProverVerifierCommHandler(verifierAddress, verifierPort);

        LocationCertificate certificate;
        try {
            certificate = commHandler.sendDataToVerifier(proof);
        } catch (InterruptedException e) {
            throw new EntityException(ErrorMessage.GRPC_CONNECTION_ERROR);
        }
        return certificate;
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
