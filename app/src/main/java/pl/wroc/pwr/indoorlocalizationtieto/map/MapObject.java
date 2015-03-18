package pl.wroc.pwr.indoorlocalizationtieto.map;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
public class MapObject {
    private Geometry objectGeometry;
    public MapObject(Geometry objectGeometry) {
        this.objectGeometry = objectGeometry;
    }

    public Geometry getGeometry() {
        return objectGeometry;
    }
}
