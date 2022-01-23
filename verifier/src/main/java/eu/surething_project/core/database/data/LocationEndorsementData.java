package eu.surething_project.core.database.data;

public class LocationEndorsementData {

    private String endorsementId;
    private String witnessId;
    private String claimId;
    private double latitude;
    private double longitude;
    private long timeInMillis;
    private String proofId;

    public LocationEndorsementData(String endorsementId, String witnessId, String claimId,
                                   double latitude, double longitude, long timeInMillis,
                                   String proofId) {
        this.endorsementId = endorsementId;
        this.witnessId = witnessId;
        this.claimId = claimId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeInMillis = timeInMillis;
        this.proofId = proofId;
    }

    public String getEndorsementId() {
        return endorsementId;
    }

    public String getWitnessId() {
        return witnessId;
    }

    public String getClaimId() {
        return claimId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public String getProofId() {
        return proofId;
    }
}
