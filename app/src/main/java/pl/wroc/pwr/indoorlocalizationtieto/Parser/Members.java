package pl.wroc.pwr.indoorlocalizationtieto.Parser;

public class Members {

    private String type;
    private long ref;
    private String role;

    public Members(String type, long ref, String role) {
        this.type = type;
        this.ref = ref;
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public long getRef() {
        return ref;
    }

    public String getRole() {
        return role;
    }
}