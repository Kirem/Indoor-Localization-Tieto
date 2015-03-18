package pl.wroc.pwr.indoorlocalizationtieto.Geometry;

import java.util.List;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class Multipolygon extends Geometry {
    private List<Polygon> polygons;

    public Multipolygon(List<Polygon> polygonslist) {
        polygons.addAll(polygonslist);
    }

    public Multipolygon(Polygon[] polygonsarr) {
        for(Polygon p : polygonsarr) {
            polygons.add(p);
        }
    }

    @Override
    public double CalculateLength() {
        float length = 0;
        for(Polygon p : polygons) {
            length += p.CalculateLength();
        }
        return length;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }
}