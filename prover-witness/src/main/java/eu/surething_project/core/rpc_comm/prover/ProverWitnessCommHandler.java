package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.location_simulation.Entity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * Prover sends witness the SignedLocationClaim and receives a
 * SignedLocationEndorsement
 */
public class ProverWitnessCommHandler {

    @Autowired
    private WitnessClient witnessClient;

    private ManagedChannel channel;

    public ProverWitnessCommHandler(Entity entity) {
        this.channel = buildChannel(entity);
    }

    public SignedLocationEndorsement sendWitnessData(SignedLocationClaim claim)
            throws InterruptedException {
        SignedLocationEndorsement endorsement;
        try {
            endorsement = this.witnessClient.sendSignedClaimToWitness(claim);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }

        // Verify endorsement freshness
        long nonce = claim.getProverSignature().getNonce();
        LocationEndorsementVerifier.verifyEndorsement(nonce, endorsement);

        return endorsement;
    }

    private ManagedChannel buildChannel(Entity entity) {
        String target = entity.getAddress() + ":" + entity.getPort();

        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
    }
}
