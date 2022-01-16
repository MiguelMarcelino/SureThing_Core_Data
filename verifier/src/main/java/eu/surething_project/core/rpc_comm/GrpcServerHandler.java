package eu.surething_project.core.rpc_comm;

public class GrpcServerHandler {

    public void buildServer() throws InterruptedException {
        final GrpcServer server = new GrpcServer();
        server.start();
        server.blockUntilShutdown();
    }
}
