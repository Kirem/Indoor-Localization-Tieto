package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.Map;

public class Node {

    private long id;
    private double latitude;
    private double longitude;
    private Map<String, String> tags;

    public Node(long id, double latitude, double longitude, Map<String, String> tags) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
