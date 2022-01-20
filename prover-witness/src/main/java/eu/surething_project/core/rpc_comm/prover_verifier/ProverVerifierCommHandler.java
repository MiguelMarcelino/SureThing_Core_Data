package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.SignedLocationProof;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class ProverVerifierCommHandler {

    private String verifierGrpcAddress;

    private int verifierGrpcPort;

    private VerifierClient verifierClient;

    private ManagedChannel channel;

    public ProverVerifierCommHandler(String verifierGrpcAddress, int verifierGrpcPort) {
        this.channel = buildChannel();
        this.verifierClient = new VerifierClient(channel);
        this.verifierGrpcAddress = verifierGrpcAddress;
        this.verifierGrpcPort = verifierGrpcPort;
    }

    /**
     * Sends the LocationEndorsement List to the Verifier
     * @param proof - The proof to send
     * @return
     * @throws InterruptedException
     */
    public LocationCertificate sendDataToVerifier(SignedLocationProof proof)
            throws InterruptedException {
        LocationCertificate certificate;
        try {
            certificate = this.verifierClient.sendProofToVerifier(proof);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }

        // Verify Freshness
        long nonce = proof.getProverSignature().getNonce();
        LocationCertificateVerifier.verifyCertificate(nonce, certificate);

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
