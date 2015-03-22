package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
import java.util.Map;

public class Way {

    private long id;
    private ArrayList<Long> nodesList;
    private Map<String, String> tags;

    public Way(long id, ArrayList<Long> nodesList, Map<String, String> tags) {
        this.id = id;
        this.nodesList = nodesList;
        this.tags = tags;

    }

    public long getId() {
        return id;
    }

    public ArrayList<Long> getNodesList() {
        return nodesList;
    }

    public Map<String, String> getTags() {
        return tags;
    }

}
