package eu.surething_project.core.rpc_comm.witness;

import eu.surething_project.core.crypto.CryptoHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

public class WitnessGrpcServerHandler {

    private CryptoHandler cryptoHandler;

    public WitnessGrpcServerHandler(CryptoHandler cryptoHandler) {
        this.cryptoHandler = cryptoHandler;
    }

    public void buildServer(int port) throws InterruptedException {
        final WitnessGrpcServer server = new WitnessGrpcServer(port);
        try {
            server.start(this.cryptoHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.blockUntilShutdown();
    }
}
