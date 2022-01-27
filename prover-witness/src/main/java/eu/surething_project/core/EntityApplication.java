package eu.surething_project.core;

import eu.surething_project.core.config.AddressValidator;
import eu.surething_project.core.config.PropertiesReader;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.data.LocationDataHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.*;
import eu.surething_project.core.location_simulation.DistanceCalculator;
import eu.surething_project.core.location_simulation.Entity;
import eu.surething_project.core.location_simulation.EntityManager;
import eu.surething_project.core.location_simulation.LatLngPair;
import eu.surething_project.core.rpc_comm.prover.LocationClaimBuilder;
import eu.surething_project.core.rpc_comm.prover.LocationEndorsementVerifier;
import eu.surething_project.core.rpc_comm.prover.ProverWitnessCommHandler;
import eu.surething_project.core.rpc_comm.prover_verifier.LocationCertificateVerifier;
import eu.surething_project.core.rpc_comm.prover_verifier.LocationProofBuilder;
import eu.surething_project.core.rpc_comm.prover_verifier.ProverVerifierCommHandler;
import eu.surething_project.core.rpc_comm.witness.EndorseClaimService;
import eu.surething_project.core.rpc_comm.witness.WitnessGrpcServer;
import eu.surething_project.core.scheduling.TaskScheduler;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EntityApplication {

    private static final Logger logger = Logger.getLogger(EntityApplication.class.getName());

    private static final String CRYPTO_ALGO = "SHA256withRSA";

    public static void main(String[] args) {
        // Read properties
        logger.info("Working Directory = " + System.getProperty("user.dir"));
        String entityStorage = PropertiesReader.getProperty("entity.storage");
        String securityStorage = PropertiesReader.getProperty("entity.storage.security");

        // Check the args
        checkArgs(args, securityStorage, entityStorage);

        // Read args
        final String currentEntityId = args[0];
        final int witnessGrpcPort = Integer.parseInt(args[1]);
        final String keystoreName = args[2];
        final String keystorePassword = args[3];

        // Sets up entity Manager and reads entity data from a file
        EntityManager entityManager = new EntityManager();
        entityManager.readEntityFile("data/" + currentEntityId, "/entityData.txt");
        Entity currentEntity = entityManager.getCurrentEntity();
        currentEntity.setId(currentEntityId);

        // Schedule location updates (Simulation)
        TaskScheduler taskScheduler = new TaskScheduler(entityManager);
        taskScheduler.scheduleTasks();

        // Create CryptoHandler
        CryptoHandler cryptoHandler;
        try {
            cryptoHandler = new CryptoHandler(currentEntityId, keystoreName, keystorePassword);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        }

        // Get certificate Path
        String certificatePath = entityStorage + "/" + currentEntityId + "/" + securityStorage;

        // Get external Path
        String externalData = entityStorage + "/" + currentEntityId + "/external";

        // Create Witness server
        WitnessGrpcServer witnessGrpcServer = new WitnessGrpcServer(witnessGrpcPort,
                cryptoHandler);

        // Create Service
        EndorseClaimService endorseClaimService = new EndorseClaimService(cryptoHandler,
                certificatePath, externalData, currentEntityId, currentEntity);
        try {
            witnessGrpcServer.buildServer(endorseClaimService);
        } catch (InterruptedException e) {
            throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
        }

        // Prepare Builders
        LocationClaimBuilder claimBuilder = new LocationClaimBuilder(cryptoHandler,
                currentEntityId, certificatePath);
        LocationProofBuilder proofBuilder = new LocationProofBuilder(cryptoHandler,
                currentEntityId, certificatePath);

        // Prepare Channel Managers
        ProverWitnessCommHandler witnessCommHandler = new ProverWitnessCommHandler(cryptoHandler);
        ProverVerifierCommHandler verifierCommHandler = new ProverVerifierCommHandler(cryptoHandler);

        // Prepare Data verifiers
        LocationEndorsementVerifier endorsementVerifier = new LocationEndorsementVerifier(
                cryptoHandler, externalData);
        LocationCertificateVerifier certificateVerifier = new LocationCertificateVerifier(
                cryptoHandler, externalData);

        // Sets up Endorsement Handler to store endorsements
        LocationDataHandler locationDataHandler = new LocationDataHandler();

        // Show Menu
        printOptions();

        // Receive user input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("\n>> ");
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
                // FOR TESTING ONLY
                // send_claim address:port id
                // Example: send_claim localhost:8081 witness

                // Receive and validate input
                AddressValidator.validateAddress(inputs[1]);
                String[] ipValues = inputs[1].split(":");
                String address = ipValues[0];
                int port = Integer.parseInt(ipValues[1]);
                String witnessId = inputs[2];

                // create witness entity
                Entity entity = new Entity(witnessId, address, port,
                        null);

                // communicate with witness
                LocationClaim claim = claimBuilder.buildLocationClaim(currentEntity.getLatLngPair());
                byte[] signedData = signLocationClaim(claim, cryptoHandler);
                SignedLocationClaim signedClaim = buildSignedClaim(claimBuilder, claim, signedData);
                SignedLocationEndorsement endorsement = sendWitnessData(entity, signedClaim,
                        witnessCommHandler, endorsementVerifier);

                if (endorsement == null) {
                    logger.warning(ErrorMessage.LOCATION_CLAIM_SEND_ERROR.message);
                } else {
                    // Add endorsement to received endorsements list
                    locationDataHandler.addLocationEndorsement(claim, endorsement);
                    entityManager.updateEntityLocation(endorsement.getEndorsement());
                }

                // DEBUG
                if (PropertiesReader.getDebugProperty()) {
                    System.out.println("Received endorsement with ID: " +
                            endorsement.getEndorsement().getEndorsementId());
                }
            } else if (inputs[0].equals("broadcast_claim")) {
                // Get entities within range of current entity
                List<Entity> entities = entityManager.getEntitiesInRange(currentEntity.getLatLngPair());

                // build location claim to send to multiple witnesses
                LocationClaim claim = claimBuilder.buildLocationClaim(currentEntity.getLatLngPair());
                byte[] signedData = signLocationClaim(claim, cryptoHandler);
                for (Entity entity : entities) {
                    SignedLocationClaim signedClaim = buildSignedClaim(claimBuilder, claim, signedData);
                    SignedLocationEndorsement endorsement = sendWitnessData(entity, signedClaim,
                            witnessCommHandler, endorsementVerifier);

                    if (endorsement != null) {
                        // Add endorsement to received endorsements list
                        locationDataHandler.addLocationEndorsement(claim, endorsement);
                        entityManager.updateEntityLocation(endorsement.getEndorsement());

                        // DEBUG
                        if (PropertiesReader.getDebugProperty()) {
                            System.out.println("Received endorsement with ID: " +
                                    endorsement.getEndorsement().getEndorsementId());
                            // Get distance
                            LatLngPair latLngCurr = currentEntity.getLatLngPair();
                            LatLngPair latLngEntity = entity.getLatLngPair();
                            double distance = DistanceCalculator.haversineFormula(latLngCurr.getLatitude(),
                                    latLngCurr.getLongitude(), latLngEntity.getLatitude(),
                                    latLngEntity.getLongitude());
                            final double range = Double.parseDouble(PropertiesReader.getProperty("entity.range"));
                            System.out.print("Distance to witness: " + distance);
                        }
                    }
                }
            } else if (inputs[0].equals("send_proof_verifier")) {
                // send_proof_verifier address:port id
                // Example: send_proof_verifier localhost:8082
                // Receive and validate input
                AddressValidator.validateAddress(inputs[1]);
                String[] ipValues = inputs[1].split(":");
                String address = ipValues[0];
                int port = Integer.parseInt(ipValues[1]);

                List<String> historyIds = new ArrayList<>();

                for (String claimId : locationDataHandler.getCurrentClaims().keySet()) {
                    List<SignedLocationEndorsement> endorsementList =
                            locationDataHandler.getEndorsementList(claimId);
                    SignedLocationProof proof = buildLocationProof(
                            locationDataHandler.getClaim(claimId), endorsementList,
                            proofBuilder);
                    LocationCertificate certificate = sendLocationProof(address, port, proof,
                            verifierCommHandler, certificateVerifier);
                    if (certificate != null) {
                        locationDataHandler.addLocationCertificate(claimId, certificate);
                        historyIds.add(claimId);
                    }
                }

                // Add sent data to history
                historyIds.forEach(id -> locationDataHandler.sendToHistory(id));
            }
        }
    }

    public static byte[] signLocationClaim(LocationClaim claim, CryptoHandler cryptoHandler) {
        try {
            return cryptoHandler.signData(claim.toByteArray(), CRYPTO_ALGO);
        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new EntityException(ErrorMessage.ERROR_SIGNING_DATA, e);
        } catch (KeyStoreException | InvalidKeyException | UnrecoverableKeyException e) {
            throw new EntityException(ErrorMessage.ERROR_ENCRYPTING_DATA, e);
        }
    }

    private static SignedLocationClaim buildSignedClaim(LocationClaimBuilder builder,
                                                        LocationClaim locClaim, byte[] signedClaim) {
        // send a location claim from this entity
        SignedLocationClaim claim;
        try {
            claim = builder.buildSignedLocationClaim(CRYPTO_ALGO, locClaim, signedClaim);
        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new EntityException(ErrorMessage.ERROR_SIGNING_DATA, e);
        } catch (KeyStoreException | InvalidKeyException | UnrecoverableKeyException e) {
            throw new EntityException(ErrorMessage.ERROR_ENCRYPTING_DATA, e);
        }
        return claim;
    }

    /**
     * @param entity - The witness entity to send the data
     * @param claim  - The claim to send
     * @return
     */
    private static SignedLocationEndorsement sendWitnessData(Entity entity, SignedLocationClaim claim,
                                                             ProverWitnessCommHandler proverWitnessComm,
                                                             LocationEndorsementVerifier verifier) {
        SignedLocationEndorsement endorsement = null;
        try {
            endorsement = proverWitnessComm.sendWitnessData(claim, entity);

            // Verify endorsement freshness
            if (endorsement != null) {
                long nonce = claim.getProverSignature().getNonce();
                verifier.verifyEndorsement(nonce, endorsement);
            }
        } catch (InterruptedException e) {
            logger.warning(ErrorMessage.GRPC_CONNECTION_ERROR.message + ": " + e.getMessage());
        } catch (FileNotFoundException | CertificateException e) {
            throw new EntityException(ErrorMessage.ERROR_GETTING_CERTIFICATE, e);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException |
                NoSuchProviderException e) {
            throw new EntityException(ErrorMessage.ERROR_VERIFYING_DATA, e);
        }

        return endorsement;
    }

    private static SignedLocationProof buildLocationProof(
            LocationClaim claim, List<SignedLocationEndorsement> endorsementList,
            LocationProofBuilder proofBuilder) {
        SignedLocationProof proof;
        try {
            proof = proofBuilder.buildSignedLocationProof(claim, endorsementList, CRYPTO_ALGO);
        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new EntityException(ErrorMessage.ERROR_SIGNING_DATA, e);
        } catch (KeyStoreException | InvalidKeyException | UnrecoverableKeyException e) {
            throw new EntityException(ErrorMessage.ERROR_ENCRYPTING_DATA, e);
        }
        return proof;
    }

    /**
     * @param verifierAddress
     * @param verifierPort
     * @return
     */
    private static LocationCertificate sendLocationProof(String verifierAddress, int verifierPort,
                                                         SignedLocationProof proof,
                                                         ProverVerifierCommHandler commHandler,
                                                         LocationCertificateVerifier verifier) {
        LocationCertificate certificate = null;
        try {
            certificate = commHandler.sendDataToVerifier(proof, verifierAddress, verifierPort);
            // Verify Data
            if (certificate != null) {
                long nonce = proof.getProverSignature().getNonce();
                verifier.verifyCertificate(nonce, certificate);
            }
        } catch (InterruptedException e) {
            logger.warning(ErrorMessage.GRPC_CONNECTION_ERROR.message + ": " + e.getMessage());
        } catch (FileNotFoundException | CertificateException e) {
            throw new EntityException(ErrorMessage.ERROR_GETTING_CERTIFICATE, e);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException |
                NoSuchProviderException e) {
            throw new EntityException(ErrorMessage.ERROR_VERIFYING_DATA, e);
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
        if (args.length != 4) throw new EntityException(ErrorMessage.INVALID_ARGS_LENGTH);

        AddressValidator.validatePort(args[1]);

        // Validate if KeyStore Exists
        String entityId = args[0];
        File truststoreFile = new File(entityStorage + "/" + entityId + "/" +
                securityStorage, args[2] + ".jks");
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
        System.out.println("1. broadcast_claim - Broadcasts a claim to all witnesses in Range");
        System.out.println("2. send_proof_verifier - To send all the proof to the verifier.");
        System.out.println("   --> Example: send_proof_verifier localhost:8082");
    }
}
