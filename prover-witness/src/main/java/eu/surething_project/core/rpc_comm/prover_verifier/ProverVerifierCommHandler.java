package eu.surething_project.core.rpc_comm.prover_verifier;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.SignedLocationProof;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

public class ProverVerifierCommHandler {

    private VerifierClient verifierClient;

    private ManagedChannel channel;

    private CryptoHandler cryptoHandler;

    public ProverVerifierCommHandler(CryptoHandler cryptoHandler, String address, int port) {
        this.channel = buildChannel(address, port);
        this.verifierClient = new VerifierClient(channel);
        this.cryptoHandler = cryptoHandler;
    }

    /**
     * Sends the LocationEndorsement List to the Verifier
     *
     * @param proof - The proof to send
     * @return
     * @throws InterruptedException
     */
    public LocationCertificate sendDataToVerifier(SignedLocationProof proof)
            throws InterruptedException, FileNotFoundException, CertificateException,
            NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        LocationCertificate certificate;
        try {
            certificate = this.verifierClient.sendProofToVerifier(proof);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }

        // Verify Freshness
        long nonce = proof.getProverSignature().getNonce();
        LocationCertificateVerifier.verifyCertificate(cryptoHandler, nonce, certificate);

        return certificate;
    }

    /**
     * Builds a channel to communicate with the verifier
     *
     * @return - a new Channel
     */
    private ManagedChannel buildChannel(String address, int port) {
        String target = address + ":" + port;

        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
    }
}
