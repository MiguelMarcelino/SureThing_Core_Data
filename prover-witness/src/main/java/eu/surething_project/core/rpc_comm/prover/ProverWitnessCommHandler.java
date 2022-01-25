package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.location_simulation.Entity;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    /**
     * Sends Signed claim to Witness
     *
     * @param claim
     * @param entity
     * @return
     * @throws InterruptedException
     * @throws FileNotFoundException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     */
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

    /**
     * Builds gRPC channel with mTLS to communicate with Witness
     *
     * @param entity - current entity data
     * @return - new communication channel
     */
    private ManagedChannel buildChannel(Entity entity) {
        File certFile = cryptoHandler.getCertFile();
        File keyFile = cryptoHandler.getPrivateKeyFile();
        File rootCACert = cryptoHandler.getRootCertificate();
        TlsChannelCredentials.Builder tlsBuilder = TlsChannelCredentials.newBuilder();
        try {
            tlsBuilder.keyManager(certFile, keyFile);
            tlsBuilder.trustManager(rootCACert);
        } catch (IOException e) {
            throw new EntityException(ErrorMessage.ERROR_CREATING_CHANNEL);
        }
        return Grpc.newChannelBuilderForAddress(
                        entity.getAddress(), entity.getPort(), tlsBuilder.build())
                .build();
    }
}

