package eu.surething_project.core.rpc_comm.prover;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.grpc.*;
import eu.surething_project.core.grpc.Signature;
import eu.surething_project.core.grpc.google.type.LatLng;
import eu.surething_project.core.location_simulation.LatLongPair;
import eu.surething_project.core.location_simulation.LocationSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.security.*;

import static com.google.protobuf.util.Timestamps.fromMillis;

public class LocationClaimBuilder {

    @Autowired
    private LocationSimulator locationSimulator;

    private String proverId;

    private CryptoHandler cryptoHandler;

    public LocationClaimBuilder(CryptoHandler cryptoHandler, String proverId) {
        this.cryptoHandler = cryptoHandler;
        this.proverId = proverId;
    }

    public SignedLocationClaim buildSignedLocationClaim(String claimId, String cryptoAlg)
            throws NoSuchAlgorithmException, SignatureException, UnrecoverableKeyException,
            KeyStoreException, InvalidKeyException {
        LatLongPair latLongPair = locationSimulator
                .generateLatitudeLongitudeCoordinates(82.3, 85.4, 3);
        LocationClaim claim = buildLocationClaim(latLongPair, claimId);

        long nonce = cryptoHandler.createNonce();
        byte[] endorsementSigned = cryptoHandler.signData(claim.toByteArray(), cryptoAlg);
        return SignedLocationClaim.newBuilder()
                .setClaim(claim)
                .setProverSignature(Signature.newBuilder()
                        .setValue(ByteString.copyFrom(endorsementSigned))
                        .setCryptoAlgo(cryptoAlg)
                        .setNonce(nonce)
                        .build())
                .build();
    }

    private LocationClaim buildLocationClaim(LatLongPair latLongPair, String claimId) {
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
                .setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
                .setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
                        .setId("ABC")
                        .addAps(WiFiNetworksEvidence.AP.newBuilder()
                                .setSsid("ssid-A")
                                .setRssi("-89")
                                .build())
                        .build()))
                .build();


        return locationClaim;
    }
}
