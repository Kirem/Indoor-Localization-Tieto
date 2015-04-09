package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.Map;


public class Node extends OSMElement {

    private double latitude;
    private double longitude;

    public Node(long id, double latitude, double longitude, Map<String, String> tags) {
        super("node", id, tags);
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
