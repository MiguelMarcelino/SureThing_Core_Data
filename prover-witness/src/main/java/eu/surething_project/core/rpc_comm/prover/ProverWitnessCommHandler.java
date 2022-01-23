package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.location_simulation.Entity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

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

    private CryptoHandler cryptoHandler;

    public ProverWitnessCommHandler(CryptoHandler cryptoHandler) {
        this.cryptoHandler = cryptoHandler;
    }

    public SignedLocationEndorsement sendWitnessData(SignedLocationClaim claim, Entity entity)
            throws InterruptedException, FileNotFoundException, CertificateException,
            NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        ManagedChannel channel = buildChannel(entity);
        WitnessClient witnessClient = new WitnessClient(channel);
        SignedLocationEndorsement endorsement;
        try {
            endorsement = witnessClient.sendSignedClaimToWitness(claim);
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
