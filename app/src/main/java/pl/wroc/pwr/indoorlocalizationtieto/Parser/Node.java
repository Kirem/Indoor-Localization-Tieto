package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.Map;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

public class Node {

    private long id;
    private Point point;
    private Map<String, String> tags;

    public Node(long id, Point point, Map<String, String> tags) {
        this.id = id;
        this.point = point;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public Point getPoint() {
        return point;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
