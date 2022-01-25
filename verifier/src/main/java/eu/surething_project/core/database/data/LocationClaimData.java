package eu.surething_project.core.database.data;

public class LocationClaimData {
    private String claimId;
    private String proverId;
    private double latitude;
    private double longitude;
    private long timeInSeconds;
    private String proofId;

    public LocationClaimData(String claimId, String proverId, double latitude,
                             double longitude, long timeInMillis, String proofId) {
        this.claimId = claimId;
        this.proverId = proverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeInSeconds = timeInMillis;
        this.proofId = proofId;
    }

    public String getClaimId() {
        return claimId;
    }

    public String getProverId() {
        return proverId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimeInMillis() {
        return timeInSeconds;
    }

    public String getProofId() {
        return proofId;
    }
}
