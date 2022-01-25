package eu.surething_project.core.database.data;

public class LocationClaimData {
    private String claimId;
    private String proverId;
    private double latitude;
    private double longitude;
    private double timeInSeconds;
    private String proofId;

    public LocationClaimData(String claimId, String proverId, double latitude,
                             double longitude, double timeInSeconds, String proofId) {
        this.claimId = claimId;
        this.proverId = proverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeInSeconds = timeInSeconds;
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

    public double getTimeInSeconds() {
        return timeInSeconds;
    }

    public String getProofId() {
        return proofId;
    }
}
