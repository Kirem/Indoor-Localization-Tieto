package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
import java.util.Map;

public class Relation {

    private long id;
    private ArrayList<Members> members;
    private Map<String, String> tags;

    public Relation(long id, ArrayList<Members> members, Map<String, String> tags) {

        this.id = id;
        this.members = members;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public ArrayList<Members> getMembers() {
        return members;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void addMember(Members member) {
        members.add(member);
    }
}