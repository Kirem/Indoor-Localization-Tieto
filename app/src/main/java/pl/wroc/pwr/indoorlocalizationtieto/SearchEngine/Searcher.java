package pl.wroc.pwr.indoorlocalizationtieto.SearchEngine;

import android.support.v4.util.Pair;
import android.util.Log;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;

/**
 * Created by PiroACC on 2015-05-17.
 */
public class Searcher {

    private static final String ROOM = "pokoj";
    private static final String BUILDING = "budynek";
    private static final String ROAD = "ulica";


    private Map map;
    private ArrayList<Pair<String, MapObject>> pairs;

    Searcher (Map map){
        this.map = map;
    }

    void createIndex(){
        for (MapObject mapObj : map.getObjects()){
            String name = mapObj.getName();
            if (name.contains(ROOM)) {
                pairs.add(new Pair (ROOM,mapObj));
            }else if (name.contains(BUILDING)) {
                pairs.add(new Pair (BUILDING,mapObj));
            }else if (name.contains(ROAD)) {
                pairs.add(new Pair (ROAD,mapObj));
            }
        }
    }
}
