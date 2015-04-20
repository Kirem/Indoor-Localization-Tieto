package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Way extends OSMElement {

    private ArrayList<Node> nodesList;

    public Way(long id, ArrayList<Node> nodesList, Map<String, String> tags) {
        super("way", id, tags);
        this.nodesList = nodesList;
        for (Node node : this.nodesList) {
            node.addPartOf(this);
        }
    }

    public ArrayList<Node> getNodesList() {
        return nodesList;
    }
}
