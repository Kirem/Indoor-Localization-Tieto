package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

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

    public GeometryRenderer(ArrayList<MapObject> objects, Context context) {
        renderedMapObjects = new ArrayList<>(objects);
        styleManager = new StyleManager(context);

        activePaint = new Paint();
        defaultPaint = new Paint();
        zoomLevel = 1;
    }

    public void setMapObjects(ArrayList<MapObject> mapObjects){
        renderedMapObjects.clear();
        renderedMapObjects = new ArrayList<>(mapObjects);
    }

    @Override
    public void setStyle(int id) {
        styleManager.loadStyle(id);
    }

    @Override
    public void setZoomLevel(float zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    @Override
    public void draw(Canvas canvas, PointF offset) {
        canvas.save();
//        canvas.scale(zoomLevel, zoomLevel);
        canvas.translate(offset.x*zoomLevel, offset.y*zoomLevel);


        for (MapObject object : renderedMapObjects) {
            Map<String, String> options = object.getOptions();
            String objectClass = options.get(MapObject.OBJECT_CLASS);
            String objectType = options.get(MapObject.OBJECT_TYPE);
            List<MapObjectStyle> styles =
                    styleManager.getStyleSetForData((int) zoomLevel, objectClass, objectType);
            drawObject(canvas, object, styles);
        }
        canvas.restore();
    }

    private void drawObject(Canvas canvas, MapObject object, List<MapObjectStyle> styles) {
        if (styles != null && styles.size() > 0) {
            for (MapObjectStyle style : styles) {
                activePaint.set(defaultPaint);
                style.stylise(activePaint);
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
        canvas.drawPoint((float) point.getX()*zoomLevel, (float) point.getY()*zoomLevel, activePaint);
    }

    private void drawLine(Canvas canvas, Point startingPoint, Point endingPoint) {
        canvas.drawLine((float) startingPoint.getX()*zoomLevel, (float) startingPoint.getY()*zoomLevel,
                (float) endingPoint.getX()*zoomLevel, (float) endingPoint.getY()*zoomLevel, activePaint);
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
            drawLine(canvas, point, next);
        }
    }

    private void drawPolygon(Canvas canvas, Polygon polygon) {
        Path path = getClosedPath(polygon);
        canvas.drawPath(path, activePaint);
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
        path.moveTo((float) first.getX()*zoomLevel, (float) first.getY()*zoomLevel);
        for (int i = 1; i < pointList.size(); i++) {
            point = pointList.get(i);
            path.lineTo((float) point.getX()*zoomLevel, (float) point.getY()*zoomLevel);
        }
        path.lineTo((float) first.getX()*zoomLevel, (float) first.getY()*zoomLevel);
        return path;
    }
}
