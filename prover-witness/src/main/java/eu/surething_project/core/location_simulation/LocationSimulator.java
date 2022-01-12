package eu.surething_project.core.location_simulation;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LocationSimulator {

    /**
     * Generate a random latitude and longitude pair between two values,
     * that differ in an approximation
     * @return
     */
    public LatLongPair generate_latitude_longitude_coordinates(double lat_value, double long_value, int approx){
        Random rd = new Random();
        double lat_high =  lat_value < 90 ? (lat_value + approx) : lat_value;
        double lat_low = lat_value > 0 ? (lat_value - approx) : lat_value;
        double latitude = rd.nextDouble(lat_high - lat_low) + lat_low;

        double long_high =  long_value < 180 ? (long_value + approx) : long_value;
        double long_low = long_value > 0 ? (long_value - approx) : long_value;
        double longitude = rd.nextDouble(long_high - long_low) + long_low;
        return new LatLongPair(latitude, longitude);
    }
}
