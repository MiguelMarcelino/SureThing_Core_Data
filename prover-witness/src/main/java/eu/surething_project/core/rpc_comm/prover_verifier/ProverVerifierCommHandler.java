package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.grpc.LocationCertificate;
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

    /**
     * Sends the LocationEndorsement List to the Verifier
     * @param endorsementList
     * @throws InterruptedException
     */
    public LocationCertificate sendDataToVerifier(List<SignedLocationEndorsement> endorsementList) throws InterruptedException {
        LocationCertificate certificate = null;
        try {
            certificate = this.verifierClient.sendEndorsementsToVerifier(endorsementList);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
        // TODO: What about the nonce here?
//        LocationCertificateVerifier.verifyCertificate();
        return certificate;
    }

    /**
     * Builds a channel to communicate with the verifier
     * @return - a new Channel
     */
    private ManagedChannel buildChannel() {
        String target = verifierGrpcAddress + ":" + verifierGrpcPort;

        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
    }
}
