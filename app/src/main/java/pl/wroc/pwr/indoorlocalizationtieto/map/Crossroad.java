package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

/**
 * Created by Mateusz on 2015-04-22.
 */
public class Crossroad extends MapObject{
    private ArrayList<Road> roads;

    public Crossroad(long id, ArrayList<Road> roadsList, Point crossPoint) {
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
