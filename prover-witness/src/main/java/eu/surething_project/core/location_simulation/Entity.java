package eu.surething_project.core.location_simulation;

public class Entity {

    private String address;
    private int port;
    private LatLongPair latLongPair;

    public Entity(int id, LatLongPair latLongPair) {
        this.latLongPair = latLongPair;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public LatLongPair getLatLongPair() {
        return latLongPair;
    }

    public void setLatLongPair(LatLongPair latLongPair) {
        this.latLongPair = latLongPair;
    }

    public String getCompleteAddress() {
        return address + ":" + port;
    }
}
