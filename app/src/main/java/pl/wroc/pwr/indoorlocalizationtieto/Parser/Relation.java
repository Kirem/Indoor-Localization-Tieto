package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Relation extends OSMElement {

    private List<Pair<String, OSMElement>> members;

    public Relation(String TYPE, long id, Map<String, String> tags, List<Pair<String, OSMElement>> members) {
        super(TYPE, id, tags);
        this.members = members;
        for (Pair<String, OSMElement> osmElementPair : this.members) {
            osmElementPair.second.addPartOf(this);
        }
    }
}