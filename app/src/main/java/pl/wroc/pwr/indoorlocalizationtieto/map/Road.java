package pl.wroc.pwr.indoorlocalizationtieto.map;


import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;

public class Road extends MapObject{

    //TODO z czego sklada sie droga?
    public Road(long id, LineString lineString) {
        super(id, lineString);
    }


    @Override
    public ArrayList<Geometry> getGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        geometries.add(this.getObjectGeometry());
        return geometries;
    }
}
