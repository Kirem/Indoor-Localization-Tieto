package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Multipolygon;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.Parser.JsonLoadedListener;
import pl.wroc.pwr.indoorlocalizationtieto.Parser.OSMData;
import pl.wroc.pwr.indoorlocalizationtieto.Parser.OSMDataParser;
import pl.wroc.pwr.indoorlocalizationtieto.Parser.OverpassDataFetcher;
import pl.wroc.pwr.indoorlocalizationtieto.R;
import pl.wroc.pwr.indoorlocalizationtieto.map.Building;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

public class RendererTestActivity extends Activity implements View.OnClickListener, JsonLoadedListener {
    private MapView mapView;
    Map map;
    private GeometryRenderer renderer;
    private ImageButton butUp;
    private ImageButton butDown;
    private int mapLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.mapView);
        renderer = new GeometryRenderer(SetupDummyMapObjects(), this);
        renderer.setStyle(R.raw.mapjsonzoom);
        butUp = (ImageButton) findViewById(R.id.butUp);
        butDown = (ImageButton) findViewById(R.id.butDown);

        butUp.setOnClickListener(this);
        butDown.setOnClickListener(this);

        mapView.setRenderer(renderer);
        mapView.setMapSize(1200f, 1200f);
    }

    private void createMap() {
        OverpassDataFetcher fetcher = new OverpassDataFetcher();
        String string[] = new String[]{"51.09408", "17.018144", "100"};
//                "way(51.093080041725514,17.017144203186035,51.09557045813361336,17.023221015930172);(._;>;);out;" +
//                "node(51.093080041725514,17.017144203186035,51.0955704581336,17.023221015930172);out;"};
        fetcher.startFetching(string, this);
//                "way(51.093080041725514,17.017144203186035,51.0955704581336,17.023221015930172);(._;>;);out;" +
//                "node(51.093080041725514,17.017144203186035,51.0955704581336,17.023221015930172);out;", this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.butDown && mapLevel > 0) {
            mapLevel--;
            //TODO change to take mapObjects from Map when map is ready.
            renderer.setMapObjects(SetupDummyMapObjects());
            mapView.invalidate();
        } else if (id == R.id.butUp && mapLevel < 1) {
            mapLevel++;
            renderer.setMapObjects(SetupDummyMapObjects1());
            mapView.invalidate();
        }
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
        ArrayList<Polygon> polygons = new ArrayList<>();
        polygons.add(new Polygon(points));
        MapObject obj = new Building(1234, new Multipolygon(polygons));
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

    public static ArrayList<MapObject> SetupDummyMapObjects1() {
        ArrayList<MapObject> objects = new ArrayList<>();
        ArrayMap<String, String> options = new ArrayMap<>();
        options.put(MapObject.OBJECT_CLASS, "buildingPart");
        options.put(MapObject.OBJECT_TYPE, "room");
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(50.0, 50.0));
        points.add(new Point(25.0, 100.0));
        points.add(new Point(50.0, 150.0));
        points.add(new Point(75.0, 100.0));
        MapObject obj = new Room(1234, new Polygon(points), false);
        obj.setOptions(options);

        objects.add(obj);
        points = new ArrayList<>();
        points.add(new Point(50.0, 50.0));
        points.add(new Point(400.0, 50.0));
        points.add(new Point(400.0, 200.0));
        Room room = new Room(125, new Polygon(points), true);
        options = new ArrayMap<>();
        options.put(MapObject.OBJECT_CLASS, "buildingPart");
        options.put(MapObject.OBJECT_TYPE, "corridor");
        room.setOptions(options);
        objects.add(room);
        points = new ArrayList<>();
        points.add(new Point(100.0, 50.0));
        points.add(new Point(200.0, 50.0));
        points.add(new Point(500.0, 200.0));
        Road road = new Road(126, new LineString(points));
        options = new ArrayMap<>();
        options.put(MapObject.OBJECT_CLASS, "highway");
        options.put(MapObject.OBJECT_TYPE, "secondary");
        road.setOptions(options);
        objects.add(road);

        return objects;
    }


    @Override
    public void onJsonLoaded(OSMData data) {
        OSMDataParser parser = new OSMDataParser();
        map = parser.parseOSMData(data);
        Log.i("OBJECTS", "objects loaded: " + map.getObjects().size());
        renderer.setMapObjects(map.getObjects());
        mapView.invalidate();
    }
}
