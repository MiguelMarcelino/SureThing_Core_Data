package eu.surething_project.core.data;

import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.LocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;
import eu.surething_project.core.location_simulation.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationDataHandler {

    private HashMap<String, List<SignedLocationEndorsement>> currentEndorsements;
    private HashMap<String, LocationClaim> currentClaims;

    private List<Pair<String, List<SignedLocationEndorsement>>> historyEndorsements;
    private List<LocationClaim> historyClaims;

    private HashMap<String, LocationCertificate> certificates;

    public LocationDataHandler() {
        currentClaims = new HashMap<>();
        currentEndorsements = new HashMap<>();
        historyClaims = new ArrayList<>();
        historyEndorsements = new ArrayList<>();
        this.certificates = new HashMap<>();
    }

    public HashMap<String, LocationClaim> getCurrentClaims() {
        return currentClaims;
    }

    public LocationClaim getClaim(String claimId) {
        return currentClaims.get(claimId);
    }

    public void addLocationEndorsement(LocationClaim claim, SignedLocationEndorsement locationEndorsement) {
        String claimId = claim.getClaimId();
        if (!this.currentEndorsements.containsKey(claimId)) {
            this.currentEndorsements.put(claimId, new ArrayList<>());
        }
        this.currentEndorsements.get(claimId).add(locationEndorsement);
        this.currentClaims.put(claimId, claim);
    }

    public void addLocationCertificate(String claimId, LocationCertificate certificate) {
        this.certificates.put(claimId, certificate);
    }

    public List<SignedLocationEndorsement> getEndorsementList(String claimId) {
        return this.currentEndorsements.get(claimId);
    }

    /**
     * Not sure if needed
     *
     * @param claimId
     */
    public void sendToHistory(String claimId) {
        if (this.currentClaims.containsKey(claimId)) {
            this.historyEndorsements.add(new Pair<>(claimId, this.currentEndorsements.get(claimId)));
            this.historyClaims.add(this.currentClaims.get(claimId));
            this.currentEndorsements.remove(claimId);
            this.currentClaims.remove(claimId);
        }
    }
}
