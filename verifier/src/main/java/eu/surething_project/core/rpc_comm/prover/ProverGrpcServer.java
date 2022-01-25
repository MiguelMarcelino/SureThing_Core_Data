package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.crypto.CryptoHandler;
import io.grpc.Grpc;
import io.grpc.Server;
import io.grpc.TlsServerCredentials;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ProverGrpcServer {
    private static final Logger logger = Logger.getLogger(ProverGrpcServer.class.getName());

    private Server server;

    private int serverPort;

    private CryptoHandler cryptoHandler;

    public ProverGrpcServer(int port, CryptoHandler cryptoHandler) {
        this.serverPort = port;
        this.cryptoHandler = cryptoHandler;
    }

    /**
     * Build gRPC server
     *
     * @param certifyClaimService
     * @throws InterruptedException
     */
    public void buildServer(CertifyClaimService certifyClaimService)
            throws InterruptedException {
        try {
            start(certifyClaimService);
        } catch (IOException e) {
            e.printStackTrace();
        }
        blockUntilShutdown();
    }

    /**
     * Starts gRPC server
     *
     * @param certifyClaimService
     * @throws IOException
     */
    private void start(CertifyClaimService certifyClaimService) throws IOException {
        File certChainFile = cryptoHandler.getCertFile();
        File privateKeyFile = cryptoHandler.getPrivateKeyFile();
        TlsServerCredentials.Builder tlsBuilder = TlsServerCredentials.newBuilder()
                .keyManager(certChainFile, privateKeyFile);
        tlsBuilder.trustManager(cryptoHandler.getRootCertificate());
        tlsBuilder.clientAuth(TlsServerCredentials.ClientAuth.REQUIRE);

        this.server = Grpc.newServerBuilderForPort(serverPort, tlsBuilder.build())
                .addService(certifyClaimService)
                .build()
                .start();

        logger.info("Grpc Server started, listening on " + serverPort);
        addShutdownHook();
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Adds shutdown hook for graceful shutdown
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    ProverGrpcServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    /**
     * Stops gRPC server
     *
     * @throws InterruptedException
     */
    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }
}
