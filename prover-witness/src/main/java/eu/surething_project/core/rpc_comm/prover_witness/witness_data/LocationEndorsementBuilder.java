package eu.surething_project.core.rpc_comm.prover_witness.witness_data;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import eu.surething_project.core.config.TimeHandler;
import eu.surething_project.core.grpc.*;
import org.springframework.beans.factory.annotation.Value;

import static com.google.protobuf.util.Timestamps.fromMillis;

public class LocationEndorsementBuilder {

    @Value("${witness.id}")
    private String witnessId;

    private LocationEndorsement endorsement;

    public LocationEndorsementBuilder() {
        this.endorsement = buildLocationEndorsement();
    }

    public SignedLocationEndorsement buildSignedLocationEndorsement() {
        return SignedLocationEndorsement.newBuilder()
                .setEndorsement(this.endorsement)
                .setWitnessSignature(Signature.newBuilder()
                        .setValue(ByteString.EMPTY) // Temporary
                        .setCryptoAlgo("SHA256WithRSA")
                        .setNonce(1) // Temporary
                        .build())
                .build();
    }

    private LocationEndorsement buildLocationEndorsement() {
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
