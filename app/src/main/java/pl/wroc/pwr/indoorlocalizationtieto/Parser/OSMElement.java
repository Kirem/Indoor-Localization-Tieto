package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PiroACC on 2015-04-17.
 */
public class OSMElement {
    private final String TYPE;
    private long id;
    private Map<String, String> tags;
    private List<OSMElement> partOf = new ArrayList<>();

    protected OSMElement(String TYPE, long id, Map<String, String> tags) {
        this.TYPE = TYPE;
        this.id = id;
        this.tags = tags;
    }

    protected String getType() {
        return this.TYPE;
    }

    protected void addPartOf(OSMElement osmElement) {
        this.partOf.add(osmElement);
    }


}