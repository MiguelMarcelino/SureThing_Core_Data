package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.SignedLocationProof;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

/**
 * Handles communication between prover and verifier
 */
public class ProverVerifierCommHandler {

    private CryptoHandler cryptoHandler;

    public ProverVerifierCommHandler(CryptoHandler cryptoHandler) {
        this.cryptoHandler = cryptoHandler;
    }

    /**
     * Sends the LocationProof to the Verifier and receives a certificate
     *
     * @param proof - The proof to send
     * @return - returned data from verifier
     * @throws InterruptedException
     */
    public LocationCertificate sendDataToVerifier(SignedLocationProof proof,
                                                  String address, int port)
            throws InterruptedException, FileNotFoundException, CertificateException,
            NoSuchAlgorithmException, SignatureException, InvalidKeyException,
            NoSuchProviderException {
        ManagedChannel channel = buildChannel(address, port);
        VerifierClient verifierClient = new VerifierClient(channel);
        LocationCertificate certificate;
        try {
            certificate = verifierClient.sendProofToVerifier(proof);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }

        return certificate;
    }

    /**
     * Builds gRPC channel with mTLS to communicate with Verifier
     *
     * @param address - channel address
     * @param port    - channel port
     * @return
     */
    private ManagedChannel buildChannel(String address, int port) {
        File certFile = cryptoHandler.getCertFile();
        File keyFile = cryptoHandler.getPrivateKeyFile();
        File rootCACert = cryptoHandler.getRootCertificate();
        TlsChannelCredentials.Builder tlsBuilder = TlsChannelCredentials.newBuilder();
        try {
            tlsBuilder.keyManager(certFile, keyFile);
            tlsBuilder.trustManager(rootCACert);
        } catch (IOException e) {
            throw new EntityException(ErrorMessage.ERROR_CREATING_CHANNEL);
        }

        return Grpc.newChannelBuilderForAddress(
                        address, port, tlsBuilder.build())
                .build();
    }
}
