package eu.surething_project.core.location_simulation;

public class LatLngPair {
    private double latitude;
    private double longitude;

    public LatLngPair(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
