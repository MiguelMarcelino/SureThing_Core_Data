package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.crypto.CryptoHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GrpcServerHandler {

    private CryptoHandler cryptoHandler;

    public GrpcServerHandler(CryptoHandler cryptoHandler) {
        this.cryptoHandler = cryptoHandler;
    }

    public void buildServer(int port, String verifierId) throws InterruptedException {
        final ProverGrpcServer server = new ProverGrpcServer(port);
        try {
            server.start(this.cryptoHandler, verifierId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.blockUntilShutdown();
    }
}
