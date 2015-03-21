package pl.wroc.pwr.indoorlocalizationtieto.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class Polygon implements Geometry {
    protected List<Point> points;

    public Polygon(List<Point> pointsList) {
        points = new ArrayList<>();
        points.addAll(pointsList);
    }

    public Polygon(Point[] pointsArr) {
        points = new ArrayList<>();
        for(Point p : pointsArr) {
            points.add(p);
        }
    }

    @Override
    public double calculateLength() {
        double length = 0;
        Point p1, p2;
        for(int i=0; i<points.size(); i++) {
            p1 = points.get(i);
            if(i+1<points.size())
                p2 = points.get(i+1);
            else
                p2 = points.get(0);
            length += Math.sqrt(Math.pow(p2.getX() - p1.getX(),2) + Math.pow(p2.getY() - p1.getY(), 2));
        }
        return length;
    }

    /**
     * @return list of points, that make up the Polygon
     */
    public List<Point> getPolygon() {
        return points;
    }
}