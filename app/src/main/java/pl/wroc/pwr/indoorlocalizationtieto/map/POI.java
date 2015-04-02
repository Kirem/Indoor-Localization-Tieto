package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

public class POI extends MapObject {

    public POI(long id, Point p) {
        super(id, p);

    }

    @Override
    public ArrayList<Geometry> getGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        geometries.add(this.getObjectGeometry());
        return geometries;
    }
}
