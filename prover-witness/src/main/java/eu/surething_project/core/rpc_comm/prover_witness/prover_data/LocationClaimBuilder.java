package eu.surething_project.core.rpc_comm.prover_witness.prover_data;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.grpc.*;
import eu.surething_project.core.grpc.google.type.LatLng;
import eu.surething_project.core.location_simulation.LatLongPair;
import eu.surething_project.core.location_simulation.LocationSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static com.google.protobuf.util.Timestamps.fromMillis;

public class LocationClaimBuilder {

    @Autowired
    private LocationSimulator locationSimulator;

    private LocationClaim claim;

    @Value("${prover.id}")
    private String proverID;

    public LocationClaimBuilder() {
        // TODO: Temporary
        LatLongPair latLongPair = locationSimulator
                .generateLatitudeLongitudeCoordinates(82.3, 85.4, 3);
        this.claim = buildLocationClaim(latLongPair);
    }

    public SignedLocationClaim buildSignedLocationClaim() {
        return SignedLocationClaim.newBuilder()
                .setClaim(this.claim)
                .setProverSignature(Signature.newBuilder()
                        .setValue(ByteString.EMPTY) // Placeholder
                        .setCryptoAlgo("SHA256WithRSA")
                        .setNonce(1) // Placeholder
                        .build())
                .build();
    }

    private LocationClaim buildLocationClaim(LatLongPair latLongPair) {
        LocationClaim locationClaim = LocationClaim.newBuilder()
                .setClaimId("1")
                .setProverId(proverID)
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
