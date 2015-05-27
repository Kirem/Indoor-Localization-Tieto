package pl.wroc.pwr.indoorlocalizationtieto.SearchEngine;

import android.support.v4.util.Pair;
import android.util.Log;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by PiroACC on 2015-05-17.
 */
public class Searcher {
    private static final String room = "pok�j";

    private Map map;
    private ArrayList<Pair<String, MapObject>> pairs;

    Searcher (Map map){
        this.map = map;
        this.createIndex();
    }

    public ArrayList<MapObject> getElements (String query){
        ArrayList <MapObject> results = new ArrayList<>();
        //TODO zrobi� :D (wszelkie sugestie mile widziane, jaki� ma�y pomys� mam)
        return results;
    }

    private void createIndex(){
        String name;
        for (MapObject mapObj : map.getObjects()){
            name = mapObj.getName();
            if (mapObj instanceof Room){
                pairs.add(new Pair (room + " " + name, mapObj) );
            }
            //TODO reszta element�w po akceptracji
        }
    }
}
