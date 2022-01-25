package eu.surething_project.core.rpc_comm.prover;

import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.location_simulation.Entity;
import io.grpc.*;

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

