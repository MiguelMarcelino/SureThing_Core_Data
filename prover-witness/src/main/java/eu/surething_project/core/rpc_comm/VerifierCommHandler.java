package eu.surething_project.core.rpc_comm;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

public class VerifierCommHandler {

    @Value("{verifier.grpc.address}")
    private String verifierGrpcAddress;

    @Value("{verifier.grpc.port}")
    private int verifierGrpcPort;

    private ManagedChannel channel;

    public VerifierCommHandler() {
        startGrpcClient();
    }

    public void startGrpcClient() {
        String target = verifierGrpcAddress + ":" + verifierGrpcPort;

        this.channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
    }

    public void sendVerifierData() throws InterruptedException {
        try {
            VerifierClient client = new VerifierClient(channel);
            client.sendClaimToVerifier();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
