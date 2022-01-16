package eu.surething_project.core.rpc_comm.prover_witness;

import com.google.protobuf.Any;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.grpc.*;
import eu.surething_project.core.grpc.google.type.LatLng;
import eu.surething_project.core.location_simulation.LatLongPair;
import eu.surething_project.core.location_simulation.LocationSimulator;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static com.google.protobuf.util.Timestamps.fromMillis;

/**
 * Communicates with witness and expects a LocationEndorsement
 */
public class EndorseClaimService extends EndorseClaimGrpc.EndorseClaimImplBase {

    @Autowired
    private LocationSimulator locationSimulator;

    @Value("${prover.id}")
    private String proverID;

    @Value("${witness.id}")
    private String witnessId;

    /**
     *
     * @param request
     * @param done
     */
    @Override
    public void sendClaimToWitness(SignedLocationClaim request, StreamObserver<SignedLocationEndorsement> done) {
        LocationClaim locationClaim = buildLocationClaim();

        // serialize location claim
        byte [] locationClaimSerialized = locationClaim.toByteArray();

        // TODO:
        // - Receive and store endorsement (for later use when needed, maybe in a list or HashMap)
        // - Using nonce for freshness verification (probably before RPC begins)
        // - Find out how Witness sends the Endorsement (Needs to have some method to
        //   receive the claim an send the endorsement, right?)
    }

    /**
     *
     * @param request
     * @param done
     */
    @Override
    public void sendClaimToWitnessNoSigning(LocationClaim request, StreamObserver<LocationEndorsement> done) {
        LocationClaim locationClaim = buildLocationClaim();

        // serialize location claim
        byte [] locationClaimSerialized = locationClaim.toByteArray();
    }

    public LocationClaim buildLocationClaim() {
        // TODO: temporary
        LatLongPair latLongPair = locationSimulator
                .generate_latitude_longitude_coordinates(82.3, 85.4, 3);
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

    public LocationEndorsement buildLocationEndorsement() {
        //	create location endorsement
        LocationEndorsement locationEndorsement = LocationEndorsement.newBuilder()
                .setWitnessId(witnessId)
                .setClaimId ("1")
                .setTime(Time.newBuilder()
                        .setTimestamp(fromMillis(TimeHandler.getCurrentTimeInMillis()))
                        .build())
                .setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
                .setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
                        .setId("DEF")
                        .addAps(WiFiNetworksEvidence.AP.newBuilder()
                                .setSsid("ssid-B")
                                .setRssi("-90")
                                .build())
                        .build()))
                .build();

        return locationEndorsement;
    }
}
