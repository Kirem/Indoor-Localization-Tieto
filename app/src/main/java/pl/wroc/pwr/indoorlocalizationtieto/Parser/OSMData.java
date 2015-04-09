package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.HashMap;

/**
 * Created by PiroACC on 2015-04-15.
 */
public class OSMData {
    private HashMap<Long, Node> nodesMap = new HashMap<>();
    private HashMap<Long, Way> waysMap = new HashMap<>();
    private HashMap<Long, Relation> relationsMap = new HashMap<>();

    public void addNode(Long id, Node node) {
        this.nodesMap.put(id, node);
    }

    public void addWay(Long id, Way way) {
        this.waysMap.put(id, way);
    }

    public void addRelation(Long id, Relation relation) {
        this.relationsMap.put(id, relation);
    }

    public Node getNode(Long id) {
        return nodesMap.get(id);
    }

    public Way getWay(Long id) {
        return waysMap.get(id);
    }

    public Relation getRelation(Long id) {
        return relationsMap.get(id);
    }

    public HashMap<Long, Relation> getRelationsMap() {
        return relationsMap;
    }

    public HashMap<Long, Way> getWaysMap() {
        return waysMap;
    }

    public HashMap<Long, Node> getNodesMap() {
        return nodesMap;
    }
}
