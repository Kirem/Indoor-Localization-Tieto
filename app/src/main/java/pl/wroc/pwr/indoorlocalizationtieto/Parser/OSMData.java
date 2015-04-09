package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.HashMap;

/**
 * Created by PiroACC on 2015-04-15.
 */
public class OSMData {
    private HashMap<Long, Node> nodesList = new HashMap<>();
    private HashMap<Long, Way> waysList = new HashMap<>();
    private HashMap<Long, Relation> relationsList = new HashMap<>();

    public void addNode(Long id, Node node) {
        this.nodesList.put(id, node);
    }

    public void addWay(Long id, Way way) {
        this.waysList.put(id, way);
    }

    public Node getNode(Long id) {
        return nodesList.get(id);
    }

    //TODO dorobic relacje ?
}
