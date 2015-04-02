package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

public class Crossing extends MapObject {
    private ArrayList<MapObject> roads;
    //stworzyc klase nadrzedna opisujaca obiekty pozwalajace na poruszanie sie?

    public Crossing(long id, Point p) {
        super(id, p);
        roads = new ArrayList<>();
    }

    public Crossing(long id, ArrayList<MapObject> r, Point p) {
        super(id, p);
        roads = new ArrayList<>(r);
    }

    public ArrayList<MapObject> getRoads() {
        return roads;
    }


    public void addRoad(Road road){
        roads.add(road);
    }


    public ArrayList<Geometry> getGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        for(MapObject object:roads)
            geometries.add(object.getObjectGeometry());
        return geometries;
    }
}
