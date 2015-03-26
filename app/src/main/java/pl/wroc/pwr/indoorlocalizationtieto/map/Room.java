package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;

//TODO czy pokoj powinien miec informacje o pietrze na ktorym sie znajduje(dla windy np.)
public class Room extends MapObject {
    private boolean isCorridor;
    public Room(long id, Polygon roomShape, boolean isCorridor) {
        super(id, roomShape);
        this.isCorridor = isCorridor;
    }

    public boolean isCorridor() {
        return isCorridor;
    }

    public void setCorridor(boolean isCorridor) {
        this.isCorridor = isCorridor;
    }


    @Override
    public ArrayList<Geometry> getGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        geometries.add(this.getObjectGeometry());
        return geometries;
    }
}
