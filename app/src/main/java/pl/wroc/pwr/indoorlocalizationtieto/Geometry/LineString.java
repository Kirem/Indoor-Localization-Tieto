package pl.wroc.pwr.indoorlocalizationtieto.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class LineString implements Geometry {
    private List<Point> points;

    public LineString(List<Point> pointsList) {
        points = new ArrayList<>();
        points.addAll(pointsList);
    }

    public LineString(Point[] pointsArr) {
        points = new ArrayList<>();
        for (Point p : pointsArr) {
            points.add(p);
        }
    }


    @Override
    public double calculateLength() {
        double length =0;
        for(int i=0; i<points.size()-1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i+1);

            length += Math.sqrt(Math.pow(p2.getX() - p1.getX(),2) + Math.pow(p2.getY() - p1.getY(), 2));
        }
        return length;
    }

    /**
     * @return list of points, that make up the LineString
     */
    public List<Point> getLineString() { return points; }
}