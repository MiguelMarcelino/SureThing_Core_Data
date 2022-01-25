package eu.surething_project.core.database.data;

import java.util.List;

public class LocationProofData {
    private String proofId;
    private long timeInSeconds;
    private LocationClaimData claim;
    private List<LocationEndorsementData> endorsements;

    public LocationProofData(String proofId, long timeInSeconds) {
        this.proofId = proofId;
        this.timeInSeconds = timeInSeconds;
    }

    public LocationProofData(String proofId, long timeInSeconds, LocationClaimData claim,
                             List<LocationEndorsementData> endorsements) {
        this.proofId = proofId;
        this.timeInSeconds = timeInSeconds;
        this.claim = claim;
        this.endorsements = endorsements;
    }

    public String getProofId() {
        return proofId;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    public LocationClaimData getClaim() {
        return claim;
    }

    public List<LocationEndorsementData> getEndorsements() {
        return endorsements;
    }

    public void setClaim(LocationClaimData claim) {
        this.claim = claim;
    }

    public void setEndorsements(List<LocationEndorsementData> endorsements) {
        this.endorsements = endorsements;
    }
}
