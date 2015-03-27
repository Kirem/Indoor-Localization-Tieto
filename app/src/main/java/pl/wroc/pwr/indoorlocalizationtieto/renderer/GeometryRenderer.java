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

    public GeometryRenderer(ArrayList<MapObject> objects, Context context) {
        mapper = new GeometryStyleMapper(objects, this, context);
    }


    @Override
    public void draw(Canvas canvas) {

//        mapper.moveToFirst();
        while (!mapper.isAfterLast()) {
            Pair<Geometry, MapObjectStyle> mapObjectStylePair = mapper.next();
            Log.d(getClass().getSimpleName(), "BackgroundColor: " + mapObjectStylePair.second.getBackgroundColor());
            drawObject(canvas, mapObjectStylePair);
        }
    }

    private void drawObject(Canvas canvas, Pair<Geometry, MapObjectStyle> mapObjectStylePair) {
        if (mapObjectStylePair.first instanceof Point) {
            drawPoint(canvas, (Point) mapObjectStylePair.first, mapObjectStylePair.second);
        } else if (mapObjectStylePair.first instanceof Line) {
            Log.d(getClass().getSimpleName(), "line drawn");
            drawLine(canvas, (Line) mapObjectStylePair.first, mapObjectStylePair.second);
        } else if (mapObjectStylePair.first instanceof LineString) {
            Log.d(getClass().getSimpleName(), "LineString drawn");
            drawLineString(canvas, (LineString) mapObjectStylePair.first, mapObjectStylePair.second);
        } else if (mapObjectStylePair.first instanceof Polygon) {
            Log.d(getClass().getSimpleName(), "polygon drawn");
            drawPolygon(canvas, (Polygon) mapObjectStylePair.first, mapObjectStylePair.second);
        } else if (mapObjectStylePair.first instanceof Multipolygon) {
            drawMultiPolygon(canvas, (Multipolygon) mapObjectStylePair.first, mapObjectStylePair.second);
        }
    }


    @Override
    public void setStyle(int id) {
        mapper.setStyle(id);
    }

    private void drawPoint(Canvas canvas, Point point, MapObjectStyle style) {
        Paint paint = new Paint();
        paint.setColor(style.getBackgroundColor());
        canvas.drawPoint((float) point.getX(), (float) point.getY(), paint);

    }

    private void drawLine(Canvas canvas, Point startingPoint, Point endingPoint, MapObjectStyle style) {
        Paint paint = new Paint();
        paint.setColor(style.getBackgroundColor());
        paint.setStrokeWidth(style.getLineWidth());
        canvas.drawLine((float) startingPoint.getX(), (float) startingPoint.getY(),
                (float) endingPoint.getX(), (float) endingPoint.getY(), paint);
    }

    private void drawLine(Canvas canvas, Line line, MapObjectStyle style) {
        Point startingPoint = line.getP1();
        Point endingPoint = line.getP2();
        drawLine(canvas, startingPoint, endingPoint, style);
    }

    private void drawLineString(Canvas canvas, LineString lineString, MapObjectStyle paint) {
        List<Point> points = lineString.getLineString();
        Point point;
        Point next;
        for (int i = 0; i < points.size() - 1; i++) {
            point = points.get(i);
            next = points.get(i + 1);
            drawLine(canvas, point, next, paint);
        }
    }

    private void drawPolygon(Canvas canvas, Polygon polygon, MapObjectStyle style) {
        Path path = getPath(polygon);
        Paint paint = new Paint();

        //prepare fill color
        paint.setColor(style.getBackgroundColor());
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);

        //prepare contour color
        paint.setColor(style.getBorderColor());
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawPath(path, paint);
    }




    private void drawMultiPolygon(Canvas canvas, Multipolygon multipolygon, MapObjectStyle style) {
        List<Polygon> polygons = multipolygon.getPolygons();
        for (Polygon polygon : polygons) {
            drawPolygon(canvas, polygon, style);
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
