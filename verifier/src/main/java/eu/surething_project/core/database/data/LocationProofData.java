package eu.surething_project.core.database.data;

import java.util.List;

public class LocationProofData {
    private String proofId;
    private long timeInMillis;
    private LocationClaimData claim;
    private List<LocationEndorsementData> endorsements;

    public LocationProofData(String proofId, long timeInMillis, LocationClaimData claim,
                             List<LocationEndorsementData> endorsements) {
        this.proofId = proofId;
        this.timeInMillis = timeInMillis;
        this.claim = claim;
        this.endorsements = endorsements;
    }

    public String getProofId() {
        return proofId;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public LocationClaimData getClaim() {
        return claim;
    }

    public List<LocationEndorsementData> getEndorsements() {
        return endorsements;
    }
}
