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

    /**
     * Builds signed location claim to send to Witness
     *
     * @param cryptoAlg
     * @param claim
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws UnrecoverableKeyException
     * @throws KeyStoreException
     * @throws InvalidKeyException
     */
    public SignedLocationClaim buildSignedLocationClaim(String cryptoAlg, LocationClaim claim,
                                                        byte[] endorsementSigned)
            throws NoSuchAlgorithmException, SignatureException, UnrecoverableKeyException,
            KeyStoreException, InvalidKeyException {
        // Get certificate data
        byte[] certificate = CertificateAccess.getCertificateContentAsBytes(certPath, proverId);

        long nonce = cryptoHandler.createNonce();
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

    /**
     * Create Location claim
     *
     * @param latLongPair
     * @return
     */
    public LocationClaim buildLocationClaim(LatLngPair latLongPair) {
        UUID uuid = UUID.randomUUID();
        LocationClaim locationClaim = LocationClaim.newBuilder()
                .setClaimId(uuid.toString())
                .setProverId(proverId)
                .setLocation(Location.newBuilder()
                        .setLatLng(LatLng.newBuilder()
                                .setLatitude(latLongPair.getLatitude())
                                .setLongitude(latLongPair.getLongitude())
                                .build())
                        .build())
                .setTime(Time.newBuilder()
                        .setRelativeToEpoch(EpochTime.newBuilder()
                                .setTimeValue(TimeHandler.getCurrentTimeInMillis())
                                .build())
                        .build())
                .build();


        return locationClaim;
    }
}
