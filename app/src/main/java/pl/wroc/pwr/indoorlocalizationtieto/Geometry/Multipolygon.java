package pl.wroc.pwr.indoorlocalizationtieto.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class Multipolygon implements Geometry {
    private List<Polygon> polygons;

    public Multipolygon(List<Polygon> polygonsList) {
        polygons = new ArrayList<>();
        polygons.addAll(polygonsList);
    }

    public Multipolygon(Polygon[] polygonsArr) {
        polygons = new ArrayList<>();
        for(Polygon p : polygonsArr) {
            polygons.add(p);
        }
    }

    @Override
    public double calculateLength() {
        double length = 0;
        for(Polygon p : polygons) {
            length += p.calculateLength();
        }
        return length;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }
}