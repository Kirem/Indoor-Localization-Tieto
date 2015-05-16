package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
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

    protected long getId() {
        return id;
    }

    protected void addPartOf(OSMElement osmElement) {
        this.partOf.add(osmElement);
    }

    protected List<OSMElement> getPartOf() {
        return this.partOf;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public String getTagValue(String tag) {
        return this.getTags().get(tag);
    }

    protected boolean checkTag(String key, String value) {
        return this.getTags().containsKey(key) && this.getTags().get(key).equals(value);
    }

    protected boolean checkTagFromArray(String key, String[] values) {
        for (String value : values) {
            if (this.checkTag(key, value)) {
                return true;
            }
        }
        return false;
    }
}
