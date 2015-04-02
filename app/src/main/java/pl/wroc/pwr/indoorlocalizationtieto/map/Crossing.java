package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

public class Crossing extends MapObject {
    private ArrayList<Road> roads;
    //stworzyc klase nadrzedna opisujaca obiekty pozwalajace na poruszanie sie?

    public Crossing(long id, Point p) {
        super(id, p);
        roads = new ArrayList<>();
    }

    public Crossing(long id, ArrayList<Road> roadsList, Point crossPoint) {
        super(id, crossPoint);
        roads = new ArrayList<>(roadsList);
    }

    public ArrayList<Road> getRoads() {
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
