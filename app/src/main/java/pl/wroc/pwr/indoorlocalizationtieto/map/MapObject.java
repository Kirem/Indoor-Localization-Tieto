package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;

public abstract class MapObject {
    private Geometry objectGeometry;
    private String name;
    MapObject(Geometry geometry) {
        objectGeometry = geometry;
    }

    public Geometry getObjectGeometry() {
        return objectGeometry;
    }

    public void setObjectGeometry(Geometry objectGeometry) {
        this.objectGeometry = objectGeometry;
    }

    abstract ArrayList<Geometry> getGeometries();
}
