package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Line;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Multipolygon;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.MapObjectStyle;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.StyleManager;

public class GeometryRenderer implements Renderer {
    ArrayList<MapObject> renderedMapObjects;
    StyleManager styleManager;
    private Paint defaultPaint;
    private Paint activePaint;
    private float zoomLevel;
    private MapObjectPointCalculator positionCalculator;
    Map<String, String> options;
    List<MapObjectStyle> styles;
    String objectClass;
    String objectType;

    public GeometryRenderer(ArrayList<MapObject> objects, Context context) {
        renderedMapObjects = new ArrayList<>(objects);
        styleManager = new StyleManager(context);
        activePaint = new Paint();
        defaultPaint = new Paint();
        zoomLevel = 1;
    }

    public void setMapObjects(ArrayList<MapObject> mapObjects) {
        renderedMapObjects.clear();
        renderedMapObjects.addAll(mapObjects);
    }

    @Override
    public void setStyle(int id) {
        styleManager.loadStyle(id);
    }

    @Override
    public void setZoomLevel(float zoomLevel) {
        if (zoomLevel < 1) {
            this.zoomLevel = 1;
        } else if (zoomLevel > 3) {
            this.zoomLevel = 3;
        } else {
            this.zoomLevel = zoomLevel;
        }
    }

    @Override
    public void draw(Canvas canvas, PointF offset) {

        canvas.save();
        canvas.translate(offset.x * zoomLevel, offset.y * zoomLevel);
        for (MapObject object : renderedMapObjects) {
            options = object.getOptions();
            objectClass = options.get(MapObject.OBJECT_CLASS);
            objectType = options.get(MapObject.OBJECT_TYPE);
            if (objectClass == null || objectType == null) continue;
//            Log.i("objects", "class: " + objectClass + " type: " + objectType);
            styles = styleManager.getStyleSetForData((int) zoomLevel, objectClass, objectType);
            drawObject(canvas, object, styles);
        }
        canvas.restore();
    }

    private void drawObject(Canvas canvas, MapObject object, List<MapObjectStyle> styles) {
        if (styles != null && styles.size() > 0) {
            for (MapObjectStyle style : styles) {
                activePaint.set(defaultPaint);
                style.stylise(activePaint);
                if (object.getObjectGeometry() != null)
                    drawGeometry(canvas, object.getObjectGeometry());
            }
        }
    }

    private void drawGeometry(Canvas canvas, Geometry geometry) {
        if (geometry instanceof Point) {
            drawPoint(canvas, (Point) geometry);
        } else if (geometry instanceof Line) {
            drawLine(canvas, (Line) geometry);
        } else if (geometry instanceof LineString) {
            drawLineString(canvas, (LineString) geometry);
        } else if (geometry instanceof Polygon) {
            drawPolygon(canvas, (Polygon) geometry);
        } else if (geometry instanceof Multipolygon) {
            drawMultiPolygon(canvas, (Multipolygon) geometry);
        }
    }

    private void drawPoint(Canvas canvas, Point point) {
        canvas.drawPoint(scaleX(point.getX()), scaleY(point.getY()), activePaint);
    }

    private float scaleX(double point) {
        return (float) positionCalculator.calibrateX(point);
//        return (float) (point - xOffset) * zoomLevel * scale;
    }

    private float scaleY(double point) {
        return (float) positionCalculator.calibrateY(point);
//        return (float) (point - yOffset) * zoomLevel * scale;
    }

    private void drawLine(Canvas canvas, Point startingPoint, Point endingPoint) {
        canvas.drawLine(scaleX(startingPoint.getX()), scaleY(startingPoint.getY()),
                scaleX(endingPoint.getX()), scaleY(endingPoint.getY()), activePaint);
    }

    private void drawLine(Canvas canvas, Line line) {
//        Point startingPoint = line.getP1();
//        Point endingPoint = line.getP2();
        drawLine(canvas, line.getP1(), line.getP2());
    }

    private void drawLineString(Canvas canvas, LineString lineString) {
        List<Point> points = lineString.getLineString();
        Point point;
        Point next;
        for (int i = 0; i < points.size() - 1; i++) {
            point = points.get(i);
            next = points.get(i + 1);
            drawLine(canvas, point, next);
        }
    }

    private void drawPolygon(Canvas canvas, Polygon polygon) {
//        Path path = getClosedPath(polygon);
        canvas.drawPath(getClosedPath(polygon), activePaint);
    }

    //TODO fix multipolygon drawing
    private void drawMultiPolygon(Canvas canvas, Multipolygon multipolygon) {
        List<Polygon> polygons = multipolygon.getPolygons();
        for (Polygon polygon : polygons) {
            drawPolygon(canvas, polygon);
        }
    }

    private Path getClosedPath(Polygon polygon) {
        List<Point> pointList = polygon.getPolygon();
        Point point;

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        Point first = pointList.get(0);
        path.moveTo(scaleX(first.getX()), scaleY(first.getY()));
        for (int i = 1; i < pointList.size(); i++) {
            point = pointList.get(i);
            path.lineTo(scaleX(point.getX()), scaleY(point.getY()));
        }
        path.lineTo(scaleX(first.getX()), scaleY(first.getY()));
        return path;
    }

    public MapObjectPointCalculator getPositionCalculator() {
        return positionCalculator;
    }

    public void setPositionCalculator(MapObjectPointCalculator positionCalculator) {
        this.positionCalculator = positionCalculator;
    }
}
