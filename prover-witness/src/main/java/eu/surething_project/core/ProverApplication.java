package eu.surething_project.core;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.rpc_comm.witness.WitnessGrpcServerHandler;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class ProverApplication {

	// TODO:
	// - Receive and store endorsement (for later use when needed, maybe in a list or HashMap)
	// - Using nonce for freshness verification (probably before RPC begins)
	public static void main(String[] args) {
		// Read args
		int witnessGrpcPort = 0;

		// Create CryptoHandler
		CryptoHandler cryptoHandler;
		try {
			cryptoHandler = new CryptoHandler();
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
		}

		// Create Witness server
		WitnessGrpcServerHandler serverHandler = new WitnessGrpcServerHandler(cryptoHandler);
		try {
			serverHandler.buildServer(witnessGrpcPort);
		} catch (InterruptedException e) {
			throw new EntityException(ErrorMessage.DEFAULT_EXCEPTION_MSG, e);
		}

		// Receive user input

	}

}
