package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Line;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Multipolygon;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.MapObjectStyle;

public class GeometryRenderer implements Renderer, GeometryStyleMapper.StylesLoadedListener {
    GeometryStyleMapper mapper = null;
    List<MapObject> renderables;
    Paint defaultPaint;
    Paint workingPaint;

    public GeometryRenderer(ArrayList<MapObject> objects, Context context) {
        renderables = objects;
        defaultPaint = new Paint(); // provides initial state for every stylization
        workingPaint = new Paint(); // used in every draw* operation

        // mapper zawiera MultiMap<MapObject, MapObjectStyle>
        mapper = new GeometryStyleMapper(objects, this, context);
        // TODO consider getting rid of GeometryStyleMapper entirely. After all, it is just a MultiMap<MapObject, MapObjectStyle>
        // e.g.:
        // mymap = GeometryStyleFactory.build(objects, this, context, styleid);
    }

    @Override
    public void draw(Canvas canvas) {
       for (MapObject object : renderables) {
           drawObject(canvas, object, mapper.get(object));
       }
    }

    private void drawObject(Canvas canvas, MapObject object, List<MapObjectStyle> styles) {
        if (styles != null && styles.size() > 0) {
            // single geometry can have multiple styles
            // i.e. to be able to draw fill and stroke separately
            for (Style style : styles) {
                stylize(canvas, style);
                // TODO: consider refactoring to avoid multiple 'instanceof' where the result will be the same
                drawGeometry(canvas, object.getGeometry())
            }
        }
    }

    void stylize(Canvas canvas, MapObjectStyle style) {
         // reset workingPaint to default values (so every style start from the same state)
         workingPaint.set(defaultPaint);
         // do what it takes to make paint looking as style dictates
         style.stylize(workingPaint);
    }

    private void drawGeometry(Canvas canvas, Geometry geom) {
        if (geom instanceof Point) {
            drawPoint(canvas, (Point) geom);
        } else if (geom instanceof Line) {
            drawLine(canvas, (Line) geom);
        } else if (geom instanceof LineString) {
            drawLineString(canvas, (LineString) geom);
        } else if (geom instanceof Polygon) {
            drawPolygon(canvas, (Polygon) geom);
        } else if (geom instanceof Multipolygon) {
            drawMultiPolygon(canvas, (Multipolygon) geom);
        }
    }

    @Override
    public void setStyle(int id) {
        mapper.setStyle(id);
    }

    private void drawPoint(Canvas canvas, Point point) {
        canvas.drawPoint((float) point.getX(), (float) point.getY(), workingPaint);
    }

    private void drawLine(Canvas canvas, Point startingPoint, Point endingPoint) {
        canvas.drawLine((float) startingPoint.getX(), (float) startingPoint.getY(),
                (float) endingPoint.getX(), (float) endingPoint.getY(), workingPaint);
    }

    private void drawLine(Canvas canvas, Line line) {
        Point startingPoint = line.getP1();
        Point endingPoint = line.getP2();
        drawLine(canvas, startingPoint, endingPoint);
    }

    private void drawLineString(Canvas canvas, LineString lineString) {
        List<Point> points = lineString.getLineString();
        Point point;
        Point next;
        for (int i = 0; i < points.size() - 1; i++) {
            point = points.get(i);
            next = points.get(i + 1);
            drawLine(canvas, point, next, workingPaint);
        }
    }

    private void drawPolygon(Canvas canvas, Polygon polygon) {
        canvas.drawPath(getPath(polygon), workingPaint);
    }

    private void drawMultiPolygon(Canvas canvas, Multipolygon multipolygon) {
        List<Polygon> polygons = multipolygon.getPolygons();
        // FIXME multipolygon is XOR of multiple polygons. Right OR is done by this code
        for (Polygon polygon : polygons) {
            drawPolygon(canvas, polygon);
        }
    }

    private Path getPath(Polygon polygon) {
        List<Point> pointList = polygon.getPolygon();
        Point point;
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        Point first = pointList.get(0);
        path.moveTo((float)first.getX(), (float)first.getY());
        for (int i = 1; i < pointList.size(); i++) {
            point = pointList.get(i);
            path.lineTo((float) point.getX(), (float) point.getY());
        }
        path.lineTo((float)first.getX(), (float)first.getY());
        return path;
    }

    @Override
    public void onStyleLoaded() {

    }
}
