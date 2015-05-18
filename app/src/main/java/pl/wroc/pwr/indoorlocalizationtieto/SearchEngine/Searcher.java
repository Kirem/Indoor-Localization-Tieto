package pl.wroc.pwr.indoorlocalizationtieto.SearchEngine;

import android.util.Log;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;

/**
 * Created by PiroACC on 2015-05-17.
 */
public class Searcher {
    private static Searcher instance = null;

    public static Searcher getInstance() {
        if (instance == null) {
            instance = new Searcher();
        }
        return instance;
    }

    static public ArrayList<MapObject> findElementsByName(String name, Map map){
        ArrayList<MapObject> elementsWithPhrase = new ArrayList<>();

        for(MapObject mapObj : map.getObjects()){
            if (mapObj.getName() != null && mapObj.getName().contains(name)){
                elementsWithPhrase.add(mapObj);
                Log.i("Objects found: : ", "Found" + mapObj.getName());
            }
        }
        return elementsWithPhrase;
    }
}
