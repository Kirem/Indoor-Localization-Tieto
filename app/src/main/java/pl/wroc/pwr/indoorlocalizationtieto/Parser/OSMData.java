package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.HashMap;

/**
 * Created by PiroACC on 2015-04-15.
 */
public class OSMData {
    public HashMap<Long, Node> nodesList = new HashMap<>();
    public HashMap<Long, Way> waysList = new HashMap<>();
    public HashMap<Long, Relation> relationsList = new HashMap<>();
}
