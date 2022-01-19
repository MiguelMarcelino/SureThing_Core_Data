package eu.surething_project.core;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.rpc_comm.prover.GrpcServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class VerifierApplication {
	private static final Logger logger = LoggerFactory.getLogger(VerifierApplication.class);

	@Value("{entity.storage}")
	private static String entityStorage;

	@Value("${entity.storage.security}")
	private static String securityStorage;

	public static void main(String[] args) {
		// Check the args
		checkArgs(args);

		// Read args
		final String entityId = args[0];
		String[] ipPort = args[1].split(":");
		final String verifierAddress = ipPort[0];
		final int verifierGrpcPort = Integer.parseInt(ipPort[1]);
		final String keystoreName = args[3];
		final String keystorePassword = args[4];

		// Create CryptoHandler
		CryptoHandler cryptoHandler;
		try {
			cryptoHandler = new CryptoHandler(entityId, keystoreName, keystorePassword);
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
	private static void checkArgs(String[] args) {
		// ID, address:port, keystore_name, keystore_password
		if (args.length != 4)
			throw new VerifierException(ErrorMessage.INVALID_ARGS_LENGTH);

		String[] ipPort = args[1].split(":");
		if (ipPort.length != 2) {
			logger.error("Invalid address");
			throw new VerifierException(ErrorMessage.INVALID_ARGS_DATA);
		}

		if (!ipPort[0].equals("localhost")) {
			String[] ipValues = ipPort[0].split("[.]");
			// validate address
			if (ipValues.length != 4) {
				logger.error("Invalid address length: " + ipValues.length);
				throw new VerifierException(ErrorMessage.INVALID_ARGS_DATA);
			}

			for (String value : ipValues) {
				int ipValue = Integer.parseInt(value);
				if (ipValue < 0 || ipValue > 255)
					logger.error("Invalid IP Address Value: " + ipValue);
				throw new VerifierException(ErrorMessage.INVALID_ARGS_DATA);
			}
		}

		// Validate Port
		int portValue = Integer.parseInt(ipPort[1]);
		if (portValue < 1024 || portValue > 65535) {
			logger.error("Invalid Port: " + portValue);
			throw new VerifierException(ErrorMessage.INVALID_ARGS_DATA);
		}

		// Validate if KeyStore Exists
		String entityId = args[0];
		File truststoreFile = new File(entityStorage +
				entityId + securityStorage, args[3]);
		if (!truststoreFile.exists()) {
			logger.error("Keystore file was not found: " + args[3]);
			throw new VerifierException(ErrorMessage.INVALID_ARGS_DATA);
		}
	}

}
