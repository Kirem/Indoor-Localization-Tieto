package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private ArrayList<MapObject> renderedMapObjects;
    private StyleManager styleManager;
    private Paint defaultPaint;
    private Paint activePaint;
    private float zoomLevel;
    private MapObjectPointCalculator positionCalculator;
    private Map<String, String> options;
    private List<MapObjectStyle> styles;
    private String objectClass;
    private String objectType;
    private float zoomScale = 1;
    private int width;
    private int height;

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
        } else if (zoomLevel > 5) {
            this.zoomLevel = 5;
        } else {
            this.zoomLevel = zoomLevel;
        }
    }

    @Override
    public void setDrawnArea(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Canvas canvas, PointF offset) {

//        canvas.save();
//        canvas.translate(offset.x * zoomLevel * zoomScale, offset.y * zoomLevel * zoomScale);

        for (MapObject object : renderedMapObjects) {
            options = object.getOptions();
            objectClass = options.get(MapObject.OBJECT_CLASS);
            objectType = options.get(MapObject.OBJECT_TYPE);
            if (objectClass == null || objectType == null) continue;
//            Log.i("OBJECT_DRAW", "CLASS: " + objectClass + " TYPE: " + objectType);
            styles = styleManager.getStyleSetForData((int) zoomLevel, objectClass, objectType);
            drawObject(canvas, object, styles);
        }
//        canvas.restore();
    }

    private void drawObject(Canvas canvas, MapObject object, List<MapObjectStyle> styles) {
        if (styles != null && styles.size() > 0) {
//            Log.i("OBJECT_DRAW", "style: " + styles.size());

            for (MapObjectStyle style : styles) {
                activePaint.set(defaultPaint);
                style.stylise(activePaint);
                if (object.getObjectGeometry() != null) {
                    drawGeometry(canvas, object.getObjectGeometry());
                }
            }
        }
    }

    private void drawGeometry(Canvas canvas, Geometry geometry) {
        if (geometry instanceof Point) {
            Point point = (Point) geometry;
            if (shouldPointBeDrawn(point)) {
                drawPoint(canvas, point);
            }
        } else if (geometry instanceof Line) {
            Line line = (Line) geometry;
            if (shouldLineBeDrawn(line)) {
                drawLine(canvas, (Line) geometry);
            }
        } else if (geometry instanceof LineString) {
            LineString lines = (LineString) geometry;
            if (shouldLineStringBeDrawn(lines)) {
                drawLineString(canvas, lines);
            }
        } else if (geometry instanceof Polygon) {
            Polygon polygon = (Polygon) geometry;
            //if(shouldPolygonBeDrawn(polygon)) {
            drawPolygon(canvas, (Polygon) geometry);
            // }
        } else if (geometry instanceof Multipolygon) {
            drawMultiPolygon(canvas, (Multipolygon) geometry);
        }
    }

    private boolean shouldPolygonBeDrawn(Polygon polygon) {
        for (Point point : polygon.getPolygon()) {
//            if(shouldPointBeDrawn(point)){
            return true;
//            }
        }
        return false;
    }

    private boolean shouldLineStringBeDrawn(LineString lines) {
        for (Point point : lines.getLineString()) {
//            if(shouldPointBeDrawn(point)){
            return true;
//            }
        }
        return false;
    }

    private boolean shouldLineBeDrawn(Line line) {
        return shouldPointBeDrawn(line.getP1()) || shouldPointBeDrawn(line.getP2());
    }

    private boolean shouldPointBeDrawn(Point point) {
        return !(point.getX() < -10 || point.getY() < -10 || point.getX() > width
                || point.getY() > height);
    }

    private void drawPoint(Canvas canvas, Point point) {
        PointF wsp = positionCalculator.calibrate(point.getX(), point.getY());
        canvas.drawPoint(wsp.x * zoomLevel * zoomScale, wsp.y * zoomLevel * zoomScale, activePaint);
    }

    private void drawLine(Canvas canvas, Point startingPoint, Point endingPoint) {
        PointF wsp1 = positionCalculator.calibrate(startingPoint.getX(), startingPoint.getY());
        PointF wsp2 = positionCalculator.calibrate(endingPoint.getX(), endingPoint.getY());
        canvas.drawLine(wsp1.x * zoomLevel * zoomScale, wsp1.y * zoomLevel * zoomScale,
                wsp2.x * zoomLevel * zoomScale, wsp2.y * zoomLevel * zoomScale, activePaint);
    }

    private void drawLine(Canvas canvas, Line line) {
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
        PointF wsp = positionCalculator.calibrate(first.getX(), first.getY());
        PointF wsp1;
        path.moveTo(wsp.x * zoomLevel * zoomScale, wsp.y * zoomLevel * zoomScale);
        for (int i = 1; i < pointList.size(); i++) {
            point = pointList.get(i);
            wsp1 = positionCalculator.calibrate(point.getX(), point.getY());
            path.lineTo(wsp1.x * zoomLevel * zoomScale, wsp1.y * zoomLevel * zoomScale);
        }
        path.lineTo(wsp.x * zoomLevel * zoomScale, wsp.y * zoomLevel * zoomScale);
        return path;
    }

    public MapObjectPointCalculator getPositionCalculator() {
        return positionCalculator;
    }

    public void setPositionCalculator(MapObjectPointCalculator positionCalculator) {
        this.positionCalculator = positionCalculator;
    }
}
