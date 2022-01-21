package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.location_simulation.Entity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

/**
 * Prover sends witness the SignedLocationClaim and receives a
 * SignedLocationEndorsement
 */
public class ProverWitnessCommHandler {

    private WitnessClient witnessClient;

    private ManagedChannel channel;

    private CryptoHandler cryptoHandler;

    public ProverWitnessCommHandler(CryptoHandler cryptoHandler, Entity entity) {
        this.channel = buildChannel(entity);
        this.witnessClient = new WitnessClient(channel);
        this.cryptoHandler = cryptoHandler;
    }

    public SignedLocationEndorsement sendWitnessData(SignedLocationClaim claim)
            throws InterruptedException, FileNotFoundException, CertificateException,
            NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        SignedLocationEndorsement endorsement;
        try {
            endorsement = this.witnessClient.sendSignedClaimToWitness(claim);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }

        return endorsement;
    }

    private ManagedChannel buildChannel(Entity entity) {
        String target = entity.getAddress() + ":" + entity.getPort();

        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
    }
}
