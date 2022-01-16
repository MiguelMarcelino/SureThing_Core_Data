package eu.surething_project.core.rpc_comm.prover;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GrpcServerHandler {

    public void buildServer() throws InterruptedException {
        final ProverGrpcServer server = new ProverGrpcServer();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.blockUntilShutdown();
    }
}
