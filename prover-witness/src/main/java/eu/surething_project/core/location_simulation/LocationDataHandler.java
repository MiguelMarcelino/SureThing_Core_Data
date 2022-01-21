package eu.surething_project.core.location_simulation;

import eu.surething_project.core.grpc.LocationCertificate;
import eu.surething_project.core.grpc.SignedLocationClaim;
import eu.surething_project.core.grpc.SignedLocationEndorsement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationDataHandler {

    private HashMap<String, List<SignedLocationEndorsement>> endorsements;
    private HashMap<String, SignedLocationClaim> claims;
    private HashMap<String, LocationCertificate> certificates;

    public LocationDataHandler() {
        endorsements = new HashMap<>();
    }

    public HashMap<String, SignedLocationClaim> getClaims() {
        return claims;
    }

    public SignedLocationClaim getClaim(String claimId) {
        return claims.get(claimId);
    }

    public void addLocationEndorsement(SignedLocationClaim claim, SignedLocationEndorsement locationEndorsement) {
        String claimId = claim.getClaim().getClaimId();
        if(!this.endorsements.containsKey(claimId)) {
            this.endorsements.put(claimId, new ArrayList<>());
        }
        this.endorsements.get(claimId).add(locationEndorsement);
        this.claims.put(claimId, claim);
    }

    public void addLocationCertificate(String claimId, LocationCertificate certificate) {
        this.certificates.put(claimId, certificate);
    }

    public List<SignedLocationEndorsement> getEndorsementList(String claimId) {
        return this.endorsements.get(claimId);
    }

    /**
     * Not sure if needed
     * @param claimId
     */
    private void removeEndorsementList(String claimId) {
        if(this.claims.containsKey(claimId)) {
            this.endorsements.remove(claimId);
            this.claims.remove(claimId);
        }
    }
}
