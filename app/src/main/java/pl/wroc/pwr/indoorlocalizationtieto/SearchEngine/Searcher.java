package pl.wroc.pwr.indoorlocalizationtieto.SearchEngine;

import android.support.v4.util.Pair;
import java.util.ArrayList;
import java.util.regex.Pattern;

import pl.wroc.pwr.indoorlocalizationtieto.map.Building;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by PiroACC on 2015-05-17.
 */
public class Searcher {
    private static final String ROOM = "pokój";
    private static final String BUILDING = "budynek";
    private static final String ROAD = "ulica";

    private Map map;
    private ArrayList<Pair<String, MapObject>> pairs = new ArrayList<>();

    public Searcher (Map map){
        this.map = map;
        this.createIndex();
    }

    /**
     *
     * @param query
     * @return list of objects which contains phrase from query
     */
    public ArrayList<MapObject> findElementsWithinQuery (String query){
        ArrayList <MapObject> results = new ArrayList<>();
        for (Pair<String,MapObject> element: pairs){
//            if (element.first.contains(query)){
                if(Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE).matcher(element.first).find()){
                results.add(element.second);
            }
        }
        return results;
    }

    private void createIndex(){
        String name;
        for (MapObject mapObj : map.getObjects()){
            name = mapObj.getName();
            if (mapObj instanceof Room){
                pairs.add(new Pair (ROOM + " " + name, mapObj) );
            }else if (mapObj instanceof Building){
                pairs.add(new Pair (BUILDING + " " + name, mapObj));
            }else if (mapObj instanceof Road){
                pairs.add(new Pair (ROAD + " " + name , mapObj));
            }
        }
    }
}
