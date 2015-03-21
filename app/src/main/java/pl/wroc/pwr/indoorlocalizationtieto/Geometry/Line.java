package pl.wroc.pwr.indoorlocalizationtieto.Geometry;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class Line implements Geometry {
    private Point p1, p2;

    public Line(Point px, Point py) {
        p1 = px;
        p2 = py;
    }

    @Override
    public double calculateLength() {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(),2) + Math.pow(p2.getY() - p1.getY(), 2));
    }

    public Point getP1() { return p1; }
    public Point getP2() { return p2; }
}