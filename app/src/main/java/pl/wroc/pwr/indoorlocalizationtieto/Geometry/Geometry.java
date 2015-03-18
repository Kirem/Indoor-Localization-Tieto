package pl.wroc.pwr.indoorlocalizationtieto.Geometry;

import java.util.List;

/**
 * Created by Mateusz on 2015-03-18.
 */
abstract class Geometry {
    public enum Type {
        POINT, LINE, LINESTRING, POLYGON, MULTIPOLYGON
    }

    public abstract float CalculateLength();
    public abstract Type getType();

}

class Point extends Geometry {
    protected float x,y;
    protected Type type;

    public Point(float px, float py) {
        x = px;
        y = py;
        type = Type.POINT;
    }

    @Override
    public float CalculateLength() {
        return 0;
    }

    @Override
    public Type getType() {
        return type;
    }
}

class Line extends Geometry {
    protected Point p1, p2;
    protected Type type;

    public Line(Point px, Point py) {
        p1 = px;
        p2 = py;
        type = Type.LINE;
    }

    @Override
    public float CalculateLength() {
        return (float)Math.sqrt(Math.pow(p2.x - p1.x,2) + Math.pow(p2.y - p1.y, 2));
    }

    @Override
    public Type getType() {
        return type;
    }
}

class LineString extends Geometry {
    protected List<Line> lines;
    protected Type type;

    public LineString(List<Line> linelist) {
        lines.clear();
        lines.addAll(linelist);
        type = Type.LINESTRING;
    }

    public LineString(Line[] linearr) {
        lines.clear();
        for (Line l : linearr) {
            lines.add(l);
        }
        type = Type.LINESTRING;
    }


    @Override
    public float CalculateLength() {
        float length =0;
        for(Line l : lines) {
            length += l.CalculateLength();
        }
        return length;
    }

    @Override
    public Type getType() {
        return type;
    }
}

class Polygon extends Geometry {
    protected List<Point> points;
    protected Type type;

    public Polygon(List<Point> pointslist) {
        points.clear();
        points.addAll(pointslist);
        type = Type.POLYGON;
    }

    public Polygon(Point[] pointsarr) {
        points.clear();
        points.
        for(Point p : pointsarr) {
            points.add(p);
        }
        type = Type.POLYGON;
    }

    @Override
    public float CalculateLength() {
        float length =0;
        Point p1, p2;
        for(int i=0; i<points.size(); i++) {
            p1 = points.get(i);
            if(i+1<points.size())
                p2 = points.get(i+1);
            else
                p2 = points.get(0);
            length += (float)Math.sqrt(Math.pow(p2.x - p1.x,2) + Math.pow(p2.y - p1.y, 2));
        }
        return length;
    }

    @Override
    public Type getType() {
        return type;
    }
}

class Multipolygon extends Geometry {
    protected List<Polygon> polygons;
    protected Type type;

    public Multipolygon(List<Polygon> polygonslist) {
        polygons.clear();
        polygons.addAll(polygonslist);
        type = Type.MULTIPOLYGON;
    }

    public Multipolygon(Polygon[] polygonsarr) {
        polygons.clear();
        for(Polygon p : polygonsarr) {
            polygons.add(p);
        }
        type = Type.MULTIPOLYGON;
    }

    @Override
    public float CalculateLength() {
        float length = 0;
        for(Polygon p : polygons) {
            length += p.CalculateLength();
        }
        return length;
    }

    @Override
    public Type getType() {
        return type;
    }
}

