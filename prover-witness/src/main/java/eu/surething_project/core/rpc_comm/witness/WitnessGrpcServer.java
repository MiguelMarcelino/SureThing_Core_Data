package eu.surething_project.core.rpc_comm.witness;


import eu.surething_project.core.crypto.CryptoHandler;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WitnessGrpcServer {
    private static final Logger logger = LoggerFactory.getLogger(WitnessGrpcServer.class.getName());

    private Server server;

    private int serverPort;

    public WitnessGrpcServer(int port){
        this.serverPort = port;
    }

    public void start(CryptoHandler cryptoHandler) throws IOException {
        this.server = ServerBuilder.forPort(serverPort)
                .addService(new EndorseClaimService(cryptoHandler))
                .build()
                .start();

        logger.info("Grpc Server started, listening on " + serverPort);
        addShutdownHook();
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    WitnessGrpcServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }
}