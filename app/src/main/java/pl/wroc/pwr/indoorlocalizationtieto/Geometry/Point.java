package pl.wroc.pwr.indoorlocalizationtieto.Geometry;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class Point extends Geometry{
    private double x,y;

    public Point(double px, double py) {
        x = px;
            y = py;
    }

    @Override
    public double calculateLength() {
            return 0.0;
        }

    public double getX() {
            return x;
        }
    public double getY() {
            return y;
        }
}
