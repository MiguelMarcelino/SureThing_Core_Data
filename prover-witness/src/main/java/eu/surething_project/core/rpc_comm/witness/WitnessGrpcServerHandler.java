package eu.surething_project.core.rpc_comm.witness;

import eu.surething_project.core.crypto.CryptoHandler;

import java.io.IOException;

public class WitnessGrpcServerHandler {

    private CryptoHandler cryptoHandler;

    private WitnessGrpcServer server;

    public WitnessGrpcServerHandler(CryptoHandler cryptoHandler) {
        this.cryptoHandler = cryptoHandler;
    }

    public void buildServer(int port, EndorseClaimService endorseClaimService) throws InterruptedException {
        server = new WitnessGrpcServer(port);
        try {
            server.start(endorseClaimService);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        server.blockUntilShutdown();
    }

    public void stopServer() throws InterruptedException {
        server.stop();
    }
}
