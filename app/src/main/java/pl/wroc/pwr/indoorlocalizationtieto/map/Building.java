package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;

public class Building extends MapObject{
    ArrayList<Level> levels;
//TODO jak zaprojektowac wyjscia z budynkow?

    public Building(long id, Polygon buildingShape) {
        super(id, buildingShape);
    }

    public Level getLevel(int position){
        if(levels.size() < position){
            return levels.get(position);
        }else{
            throw new IndexOutOfBoundsException("Position("+position+") out of Bounds(0-"+levels.size()+")");
        }
    }


    public ArrayList<Geometry> getGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        for(MapObject object:levels)
            geometries.add(object.getObjectGeometry());
        return geometries;
    }
}
