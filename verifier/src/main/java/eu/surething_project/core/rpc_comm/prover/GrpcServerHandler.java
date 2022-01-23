package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.crypto.CryptoHandler;

import java.io.IOException;

public class GrpcServerHandler {


    public GrpcServerHandler() { }

    public void buildServer(int port, CertifyClaimService certifyClaimService)
            throws InterruptedException {
        final ProverGrpcServer server = new ProverGrpcServer(port);
        try {
            server.start(certifyClaimService);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.blockUntilShutdown();
    }
}
