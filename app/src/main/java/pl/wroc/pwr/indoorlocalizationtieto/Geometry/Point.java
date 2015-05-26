package pl.wroc.pwr.indoorlocalizationtieto.Geometry;

/**
 * Created by Mateusz on 2015-03-18.
 */
public class Point implements Geometry {
    private double x,y; //latitude, longitude

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.x, x) != 0) return false;
        if (Double.compare(point.y, y) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));

        if(x<=0) result++;
        else result--;

        return result;
    }

    public double distanceTo(Point point) {
        double theta = this.getY() - point.getY();
        double dist = Math.sin(deg2rad(this.getX())) * Math.sin(deg2rad(point.getX())) + Math.cos(deg2rad(this.getX())) * Math.cos(deg2rad(point.getX())) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344; //kilometers
        dist = dist * 1000; //meters
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
