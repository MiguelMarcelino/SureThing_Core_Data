package eu.surething_project.core.location_simulation;

public class DistanceCalculator {

    /**
     * Calculates distance in kilometers between latitude and longitude
     * of two entities using haversine formula
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return - distance in kilometers between two entities
     */
    public static double haversineFormula(double lat1, double lng1,
                                          double lat2, double lng2) {
        // Earth radius
        double r = 6373.0;

        double dLat = lat1 - lat2;
        double dLng = lng1 - lng2;
        double a = Math.pow(Math.sin(dLat) / 2, 2) + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dLng) / 2, 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double distance = r * c;

        return distance;
    }
}
