package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;

public class Map {
    private static Map singleton = null;
    ArrayList<MapObject> objects;
    public Map() {
        objects = new ArrayList<>();
    }


    public static Map getInstance() {
        if(singleton==null){
            singleton = new Map();
        }
        return singleton;
    }
    public void addObject(MapObject object){
        objects.add(object);
    }

    public ArrayList<Geometry> getGeometries(){
        ArrayList<Geometry> geometries = new ArrayList<>();
        for(MapObject object:objects){
            geometries.addAll(object.getGeometries());
        }
        return geometries;
    }
}
