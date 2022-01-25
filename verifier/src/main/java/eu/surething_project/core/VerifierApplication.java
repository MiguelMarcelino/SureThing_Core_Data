package eu.surething_project.core;

import eu.surething_project.core.config.PropertiesReader;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.database.DatabaseAccessManagement;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.rpc_comm.prover.CertifyClaimService;
import eu.surething_project.core.rpc_comm.prover.ProverGrpcServer;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

public class VerifierApplication {
	private static final Logger logger = Logger.getLogger(VerifierApplication.class.getName());

	private static final String PROPERTIES = "src/main/java/eu/surething_project/core/application.properties";

	public static void main(String[] args) {
		// Read properties
		PropertiesReader prop = new PropertiesReader(PROPERTIES);
		String entityStorage = prop.getProperty("entity.storage");
		String securityStorage = prop.getProperty("entity.storage.security");

		// Check the args
		checkArgs(args, entityStorage, securityStorage);

		// Read args
		final String currentEntityId = args[0];
		final int verifierGrpcPort = Integer.parseInt(args[1]);
		final String keystoreName = args[2];
		final String keystorePassword = args[3];

		// Create CryptoHandler
		CryptoHandler cryptoHandler;
		try {
			cryptoHandler = new CryptoHandler(currentEntityId, keystoreName, keystorePassword, prop);
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
		}

		// Get certificate Path
		String certificatePath = entityStorage + "/" + currentEntityId + "/" + securityStorage;

		// Get external Path
		String externalData = entityStorage + "/" + currentEntityId + "/external";

		/**********************************/
		// Keystore Properties
//		System.setProperty("javax.net.ssl.keyStoreType", "JCEKS");
//		System.setProperty("javax.net.ssl.keyStore",
//				entityStorage + "/" + currentEntityId + "/" + securityStorage + "/" + keystoreName + ".jks");
//		System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
		/**********************************/

		// Prepare Database
		DatabaseAccessManagement dbAccessMgmt = new DatabaseAccessManagement();
//		AsyncDatabaseAccess databaseWriter = new AsyncDatabaseAccess(dbAccessMgmt);

		// Schedule services
//		TaskScheduler scheduler = new TaskScheduler(databaseWriter);
//		scheduler.scheduleTasks();

		// Start Verifier server
		ProverGrpcServer grpcServer = new ProverGrpcServer(verifierGrpcPort, cryptoHandler);

		// Create Certify Claim Service
		CertifyClaimService certifyClaimService = new CertifyClaimService(cryptoHandler,
				currentEntityId, externalData, certificatePath, dbAccessMgmt);
		try {
			grpcServer.buildServer(certifyClaimService);
		} catch (InterruptedException e) {
			throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
		}
	}

	/**
	 * Verifier the application arguments
	 * @param args
	 */
	private static void checkArgs(String[] args, String entityStorage, String securityStorage) {
		// ID, address:port, keystore_name, keystore_password
		if (args.length != 4)
			throw new VerifierException(ErrorMessage.INVALID_ARGS_LENGTH);

		// Validate Port
		int portValue = Integer.parseInt(args[1]);
		if (portValue < 1024 || portValue > 65535) {
			logger.severe("Invalid Port: " + portValue);
			throw new VerifierException(ErrorMessage.INVALID_ARGS_DATA);
		}

		// Validate if KeyStore Exists
		String entityId = args[0];
		File truststoreFile = new File(entityStorage + "/" + entityId + "/" +
				securityStorage, args[2] + ".jks");
		if (!truststoreFile.exists()) {
			logger.severe("Keystore file was not found: " + args[2]);
			throw new VerifierException(ErrorMessage.INVALID_ARGS_DATA);
		}
	}

}
