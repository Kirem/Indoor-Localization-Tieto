package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Multipolygon;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;

public class Building extends MapObject{
    ArrayList<Level> levels;
//TODO jak zaprojektowac wyjscia z budynkow?

    public Building(long id, Multipolygon buildingShape) {
        super(id, buildingShape);
    }

    public Building(long id, Multipolygon buildingShape, ArrayList<Level> l) {
        super(id, buildingShape);
        levels = new ArrayList<>(l);
    }

    public Level getLevel(float floor){
        for(Level lev : levels) {
            if(lev.getCurrentLevel() == floor){
                return lev;
            }
        }
        return null;
    }

    public ArrayList<Geometry> getGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        for(MapObject object:levels)
            geometries.add(object.getObjectGeometry());
        return geometries;
    }
}
