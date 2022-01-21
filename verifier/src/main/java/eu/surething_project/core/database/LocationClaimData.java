package eu.surething_project.core.database;

public class LocationClaimData {
    private String claimId;
    private String proverId;
    private double latitude;
    private double longitude;
    private long timeInMillis;
    private String proofId;

    public LocationClaimData(String claimId, String proverId, double latitude,
                             double longitude, long timeInMillis, String proofId) {
        this.claimId = claimId;
        this.proverId = proverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeInMillis = timeInMillis;
        this.proofId = proofId;
    }


}
