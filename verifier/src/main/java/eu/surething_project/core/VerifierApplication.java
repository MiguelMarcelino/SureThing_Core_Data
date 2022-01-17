package eu.surething_project.core;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.exceptions.VerifierException;
import eu.surething_project.core.rpc_comm.prover.GrpcServerHandler;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class VerifierApplication {

	public static void main(String[] args) {
		// Read args
		int verifierPort = 0;

		// Create CryptoHandler
		final String CIPHER_ALGO = "AES/ECB/PKCS5Padding";
		CryptoHandler cryptoHandler;
		try {
			cryptoHandler = new CryptoHandler();
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
		}

		// Start Verifier server
		GrpcServerHandler grpcServerHandler = new GrpcServerHandler(cryptoHandler);
		try {
			grpcServerHandler.buildServer(verifierPort);
		} catch (InterruptedException e) {
			throw new VerifierException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
		}

		// Receive user input
	}

}
