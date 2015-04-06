package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.R;
import pl.wroc.pwr.indoorlocalizationtieto.map.Building;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;

public class RendererTestActivity extends Activity {
    private MapView mapView;
    private Renderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.mapView);
        renderer = new GeometryRenderer(SetupDummyMapObjects(), this);
        renderer.setStyle(R.raw.zoomjson);

        mapView.setRenderer(renderer);
        mapView.setMapSize(1200f, 1200f);
    }



    public static ArrayList<MapObject> SetupDummyMapObjects() {
        ArrayList<MapObject> objects = new ArrayList<>();
        ArrayMap<String, String> options = new ArrayMap<>();
        options.put(MapObject.OBJECT_CLASS, "building");
        options.put(MapObject.OBJECT_TYPE, "university");
        Point pointS = new Point(1.0, 1.0);
        Point pointE = new Point(200.0, 200.0);
//        geometries.add(new Line(pointS, pointE));
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(50.0, 50.0));
        points.add(new Point(25.0, 100.0));
        points.add(new Point(50.0, 150.0));
        points.add(new Point(75.0, 100.0));
        MapObject obj = new Building(1234, new Polygon(points));
        obj.setOptions(options);

        objects.add(obj);
        points = new ArrayList<>();
        points.add(new Point(50.0, 50.0));
        points.add(new Point(400.0, 50.0));
        points.add(new Point(400.0, 200.0));
        Road road = new Road(125, new LineString(points));
        options = new ArrayMap<>();
        options.put(MapObject.OBJECT_CLASS, "highway");
        options.put(MapObject.OBJECT_TYPE, "primary");
        road.setOptions(options);
        objects.add(road);
        points = new ArrayList<>();
        points.add(new Point(100.0, 50.0));
        points.add(new Point(200.0, 50.0));
        points.add(new Point(500.0, 200.0));
        road = new Road(126, new LineString(points));
        options = new ArrayMap<>();
        options.put(MapObject.OBJECT_CLASS, "highway");
        options.put(MapObject.OBJECT_TYPE, "secondary");
        road.setOptions(options);
        objects.add(road);

        return objects;
    }
}
