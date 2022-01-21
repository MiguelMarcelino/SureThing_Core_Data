package eu.surething_project.core;

import eu.surething_project.core.config.PropertiesReader;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.rpc_comm.prover.GrpcServerHandler;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

public class VerifierApplication {
	private static final Logger logger = Logger.getLogger(VerifierApplication.class.getName());

	private static final String PROPERTIES = "application.properties";

	public static void main(String[] args) {
		// Read properties
		PropertiesReader prop = new PropertiesReader(PROPERTIES);
		String entityStorage = prop.getProperty("entity.storage");
		String securityStorage = prop.getProperty("entity.storage.security");

		// Check the args
		checkArgs(args, entityStorage, securityStorage);

		// Read args
		final String entityId = args[0];
		final int verifierGrpcPort = Integer.parseInt(args[1]);
		final String keystoreName = args[2];
		final String keystorePassword = args[3];

		// Create CryptoHandler
		CryptoHandler cryptoHandler;
		try {
			cryptoHandler = new CryptoHandler(entityId, keystoreName, keystorePassword, prop);
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
		}

		/**********************************/
		// Keystore Properties
		System.setProperty("javax.net.ssl.keyStoreType", "JCEKS");
		System.setProperty("javax.net.ssl.keyStore",
				entityStorage + "/" + entityId + "/" + keystoreName);
		System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
		/**********************************/

		// Start Verifier server
		GrpcServerHandler grpcServerHandler = new GrpcServerHandler(cryptoHandler);
		try {
			grpcServerHandler.buildServer(verifierGrpcPort, entityId);
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
		File truststoreFile = new File(entityStorage +
				entityId + securityStorage, args[3]);
		if (!truststoreFile.exists()) {
			logger.severe("Keystore file was not found: " + args[3]);
			throw new VerifierException(ErrorMessage.INVALID_ARGS_DATA);
		}
	}

}
