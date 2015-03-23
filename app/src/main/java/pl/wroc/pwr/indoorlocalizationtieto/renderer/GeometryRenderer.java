package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Line;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Multipolygon;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.R;

public class GeometryRenderer implements Renderer {
    ArrayList<Geometry> geometries = null;
    private Context context;

    public GeometryRenderer(ArrayList<Geometry> geometries) {
        this.geometries = new ArrayList<>(geometries);
    }


    @Override
    public void draw(Canvas canvas){
        if(geometries == null){
            Log.e(getClass().getSimpleName(), "Geometry array set to null");
            return;
        }

        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.map_line_color));
        Log.i(getClass().getSimpleName(), "paint set");
        for(Geometry geometry : geometries){
            if(geometry instanceof Point){
                drawPoint(canvas, (Point) geometry, paint);
            }else if(geometry instanceof Line){
                drawLine(canvas, (Line) geometry, paint);
            }else if(geometry instanceof LineString){
                drawLineString(canvas, (LineString) geometry, paint);
            }else if(geometry instanceof Polygon){
                drawPolygon(canvas, (Polygon) geometry, paint);
            }else if(geometry instanceof Multipolygon){
                drawMultiPolygon(canvas, (Multipolygon) geometry, paint);
            }
        }
    }

    @Override
    public void setContext(Context context){
        this.context = context;
    }

    private void drawPoint(Canvas canvas, Point point, Paint paint) {
        canvas.drawPoint((float) point.getX(), (float) point.getY(), paint);
    }

    private void drawLine(Canvas canvas, Point startingPoint, Point endingPoint, Paint paint) {
        Log.i(getClass().getSimpleName(), "line drawn");
        canvas.drawLine((float) startingPoint.getX(), (float) startingPoint.getY(),
                (float) endingPoint.getX(), (float) endingPoint.getY(), paint);
    }

    private void drawLine(Canvas canvas, Line line, Paint paint) {
        Point startingPoint = line.getP1();
        Point endingPoint = line.getP2();
        drawLine(canvas, startingPoint, endingPoint, paint);
    }

    private void drawLineString(Canvas canvas, LineString lineString, Paint paint) {
        List<Point> points = lineString.getLineString();
        Point point;
        Point next;
        for(int i = 0; i < points.size()-1; i++){
            point = points.get(i);
            next = points.get(i+1);
            drawLine(canvas, point, next, paint);
        }
    }

    private void drawPolygon(Canvas canvas, Polygon polygon, Paint paint) {
        List<Point> pointList = polygon.getPolygon();
        Point point;
        Point next;
        for (int i = 0; i < pointList.size() - 1; i++) {
            point = pointList.get(i);
            next = pointList.get(i + 1);
            drawLine(canvas, point, next, paint);
        }
    }

    private void drawMultiPolygon(Canvas canvas, Multipolygon multipolygon, Paint paint) {
        List<Polygon> polygons = multipolygon.getPolygons();
        for(Polygon polygon:polygons){
            drawPolygon(canvas, polygon, paint);
        }
    }
}
