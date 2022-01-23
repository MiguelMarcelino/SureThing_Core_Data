package eu.surething_project.core.rpc_comm.prover;

import com.google.protobuf.ByteString;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.crypto.CertificateAccess;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.*;
import eu.surething_project.core.grpc.google.type.LatLng;
import eu.surething_project.core.location_simulation.LatLngPair;
import eu.surething_project.core.location_simulation.LocationSimulator;

import java.security.*;
import java.util.UUID;

import static com.google.protobuf.util.Timestamps.fromMillis;

public class LocationClaimBuilder {

    private CryptoHandler cryptoHandler;
    private LocationSimulator locationSimulator;
    private String proverId;
    private String certPath;

    public LocationClaimBuilder(CryptoHandler cryptoHandler, String proverId, String certPath) {
        this.cryptoHandler = cryptoHandler;
        this.proverId = proverId;
        this.locationSimulator = new LocationSimulator();
        this.certPath = certPath;
    }

    public SignedLocationClaim buildSignedLocationClaim(String cryptoAlg, LatLngPair latLngPair)
            throws NoSuchAlgorithmException, SignatureException, UnrecoverableKeyException,
            KeyStoreException, InvalidKeyException {
        UUID uuid = UUID.randomUUID();
        LocationClaim claim = buildLocationClaim(latLngPair, uuid.toString());

        // Get certificate data
        byte[] certificate = CertificateAccess.getCertificateContentAsBytes(certPath, proverId);

        long nonce = cryptoHandler.createNonce();
        byte[] endorsementSigned = cryptoHandler.signData(claim.toByteArray(), cryptoAlg);
        return SignedLocationClaim.newBuilder()
                .setClaim(claim)
                .setProverSignature(Signature.newBuilder()
                        .setValue(ByteString.copyFrom(endorsementSigned))
                        .setCryptoAlgo(cryptoAlg)
                        .setNonce(nonce)
                        .setCertificateData(ByteString.copyFrom(certificate))
                        .build())
                .build();
    }

    private LocationClaim buildLocationClaim(LatLngPair latLongPair, String claimId) {
        LocationClaim locationClaim = LocationClaim.newBuilder()
                .setClaimId(claimId)
                .setProverId(proverId)
                .setLocation(Location.newBuilder()
                        .setLatLng(LatLng.newBuilder()
                                .setLatitude(latLongPair.getLatitude())
                                .setLongitude(latLongPair.getLongitude())
                                .build())
                        .build())
                .setTime(Time.newBuilder()
                        .setTimestamp(fromMillis(TimeHandler.getCurrentTimeInMillis()))
                        .build())
                .build();


        return locationClaim;
    }
}
