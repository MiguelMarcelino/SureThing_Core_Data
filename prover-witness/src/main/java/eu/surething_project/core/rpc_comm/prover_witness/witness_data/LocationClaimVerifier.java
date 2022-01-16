package eu.surething_project.core.rpc_comm.prover_witness.witness_data;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import eu.surething_project.core.crypto.CryptoHandler;
import eu.surething_project.core.exceptions.EntityException;
import eu.surething_project.core.exceptions.ErrorMessage;
import eu.surething_project.core.grpc.*;
import eu.surething_project.core.grpc.google.type.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocationClaimVerifier {

    private LocationEndorsementBuilder endorsementBuilder;

    @Autowired
    private CryptoHandler cryptoHandler;

    public LocationClaimVerifier() {
        this.endorsementBuilder = new LocationEndorsementBuilder();
    }

    /**
     * Verifies if a location claim is sent from the entity claiming to be the prover
     * @param signedLocationClaim
     * @return
     */
    public SignedLocationEndorsement verifyLocationClaim(SignedLocationClaim signedLocationClaim)
            throws NoSuchAlgorithmException, SignatureException {
        // setup
        String cryptoAlg = "SHA256WithRSA";

        // Get signed data
        Signature signature = signedLocationClaim.getProverSignature();
        long nonce = signature.getNonce();
        byte[] signedClaim = signature.getValue().toByteArray();

        // Get LocationClaim data
        LocationClaim locClaim = signedLocationClaim.getClaim();

        // Verify signed data
        cryptoHandler.verifyData(locClaim.toByteArray(), signedClaim, "SHA256WithRSA");

        // Start Data content verification
        String claimId = locClaim.getClaimId();


        // Build Signed endorsement
        SignedLocationEndorsement signedEndorsement = this.endorsementBuilder
                .buildSignedLocationEndorsement(claimId, nonce, cryptoAlg);
        return signedEndorsement;
    }

    /**
     * It will be the verifier verifying this
     * @param signedLocationClaim
     */
    public void extra(SignedLocationClaim signedLocationClaim){
        // Get LocationClaim data
        LocationClaim locClaim = signedLocationClaim.getClaim();

        String proverId = locClaim.getProverId(); // proverId
        Location location = locClaim.getLocation();
        Location.LocationCase locCase = location.getLocationCase();
        LatLng latLng = null;
        switch (locCase) {
            case LATLNG -> latLng = location.getLatLng();
            case POI -> location.getPoi();
            case PROXIMITYTOPOI -> location.getProximityToPoI();
            // case LOCATION_NOT_SET:break;
        }

        // TODO: Check if prover is near the location of the current Witness

        // Time - oneof (TIMESTAMP, INTERVAL, RELATIVETOEPOCH, EMPTY)
        Time time = locClaim.getTime();
        Time.TimeCase timeCase = time.getTimeCase();
        Timestamp timestamp = null;
        switch(timeCase){
            case TIMESTAMP ->
                    timestamp = time.getTimestamp();
            case INTERVAL -> time.getInterval();
            case RELATIVETOEPOCH -> time.getRelativeToEpoch();
            case EMPTY -> time.getEmpty();  // check if TIME_NOT_SET will be enough instead of this case
            // case TIME_NOT_SET -> break;
        }

        // TODO: Check if timestamp is not old (?)

        String evidenceType = locClaim.getEvidenceType();
        Any evidence = locClaim.getEvidence();
        WiFiNetworksEvidence wifiNetworksEvidence = null;
        try {
            wifiNetworksEvidence = evidence.unpack(WiFiNetworksEvidence.class);
        }catch(InvalidProtocolBufferException e){
            throw new EntityException(ErrorMessage.INVALID_PROTOBUF_DATA);
        }

        List<WiFiNetworksEvidence.AP> aps = new ArrayList<>();
        String evidenceId = wifiNetworksEvidence.getId();  // evidenceId
        aps = wifiNetworksEvidence.getApsList();  // list of APs in WiFi evidence
        WiFiNetworksEvidence.AP ap = aps.get(0);  // First AP
        final double lat = latLng.getLatitude();  // Location latLng - Latitude
        final double lng = latLng.getLongitude(); // Location latLng - Longitude
        final Timestamp t = timestamp; // timestamp of location claim

        // TODO: Check network evidence (According to paper --> Maybe only required in Prover after receiving endorsement)
    }
}
