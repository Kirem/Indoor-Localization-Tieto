package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
import java.util.Map;

//TODO CALA KLASA DO PRZEROBIENIA


public class Relation {

    private long id;
    private ArrayList<Member> members;
    private Map<String, String> tags;

    public Relation(long id, ArrayList<Member> members, Map<String, String> tags) {

        this.id = id;
        this.members = members;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void addMember(Member member) {
        members.add(member);
    }
}