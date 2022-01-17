package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
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
    public LocationCertificate sendDataToVerifier(SignedLocationClaim claim,
                                                  List<SignedLocationEndorsement> endorsementList)
            throws InterruptedException {
        LocationCertificate certificate;
        try {
            // certificate = this.verifierClient.sendEndorsementsToVerifier(endorsementList);
            certificate = this.verifierClient.sendProofToVerifier(claim, endorsementList);
        } catch (NoSuchAlgorithmException | SignatureException e) {
            throw new EntityException(ErrorMessage.ERROR_SIGNING_DATA);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }

        long nonce = certificate.getVerifierSignature().getNonce();
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
