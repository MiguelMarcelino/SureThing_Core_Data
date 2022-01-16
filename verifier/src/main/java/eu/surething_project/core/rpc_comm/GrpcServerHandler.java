package eu.surething_project.core.rpc_comm;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GrpcServerHandler {

    public void buildServer() throws InterruptedException {
        final GrpcServer server = new GrpcServer();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.blockUntilShutdown();
    }
}
