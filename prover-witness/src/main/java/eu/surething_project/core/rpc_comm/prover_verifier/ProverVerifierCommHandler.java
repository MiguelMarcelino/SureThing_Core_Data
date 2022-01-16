package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.grpc.SignedLocationEndorsement;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProverVerifierCommHandler {

    @Value("{verifier.grpc.address}")
    private String verifierGrpcAddress;

    @Value("{verifier.grpc.port}")
    private int verifierGrpcPort;

    private VerifierClient verifierClient;

    private ManagedChannel channel;

    public ProverVerifierCommHandler() {
        this.channel = buildChannel();
        this.verifierClient = new VerifierClient(channel);
    }

    public ManagedChannel buildChannel() {
        String target = verifierGrpcAddress + ":" + verifierGrpcPort;

        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
    }

    public void sendVerifierData(List<SignedLocationEndorsement> endorsementList) throws InterruptedException {
        try {
            this.verifierClient.sendClaimsToVerifier(endorsementList);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
