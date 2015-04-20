package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

public class Crossing extends MapObject {
    private boolean isCrossing;
    private ArrayList<Road> roads;
    //stworzyc klase nadrzedna opisujaca obiekty pozwalajace na poruszanie sie?

    public Crossing(long id, Point p, boolean isCrossing) {
        super(id, p);
        roads = new ArrayList<>();
        this.isCrossing = isCrossing;
    }

    public Crossing(long id, ArrayList<Road> roadsList, Point crossPoint, boolean isCrossing) {
        super(id, crossPoint);
        roads = new ArrayList<>(roadsList);
        this.isCrossing = isCrossing;
    }

    public boolean isCrossing() {
        return isCrossing;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }


    public void addRoad(Road road) {
        roads.add(road);
    }


    public ArrayList<Geometry> getGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        for (MapObject object : roads)
            geometries.add(object.getObjectGeometry());
        return geometries;
    }
}
