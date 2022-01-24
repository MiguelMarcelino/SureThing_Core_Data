package eu.surething_project.core.location_simulation;

public class Entity {

    private String id;
    private String address;
    private int port;
    private LatLngPair latLongPair;

    public Entity(String id, String address, int port, LatLngPair latLongPair) {
        this.id = id;
        this.latLongPair = latLongPair;
        this.address = address;
        this.port = port;
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

    public LatLngPair getLatLngPair() {
        return latLongPair;
    }

    public void setLatLongPair(LatLngPair latLongPair) {
        this.latLongPair = latLongPair;
    }

    public String getCompleteAddress() {
        return address + ":" + port;
    }

    public String getId() {
        return id;
    }

    public void setId(String currentEntityId) {
        this.id = currentEntityId;
    }
}
