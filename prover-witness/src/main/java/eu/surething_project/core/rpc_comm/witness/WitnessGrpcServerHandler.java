package eu.surething_project.core.rpc_comm.witness;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WitnessGrpcServerHandler {

    public WitnessGrpcServerHandler() {}

    public void buildServer() throws InterruptedException {
        final WitnessGrpcServer server = new WitnessGrpcServer();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.blockUntilShutdown();
    }
}
