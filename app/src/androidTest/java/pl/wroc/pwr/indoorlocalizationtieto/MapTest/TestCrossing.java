package pl.wroc.pwr.indoorlocalizationtieto.MapTest;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.map.Crossing;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;

/**
 * Created by Mateusz on 2015-03-27.
 */
public class TestCrossing extends InstrumentationTestCase {
    ArrayList<Point> pointsList = new ArrayList<>();
    LineString lineString;
    Road roadA, roadB;
    Crossing crossing;
    Point point;

    @Override
    public void setUp() throws Exception {
        pointsList.add(new Point(0.0, 0.0));
        pointsList.add(new Point(0.0, 100.0));

        lineString = new LineString(pointsList);
        roadA = new Road(1, lineString);

        pointsList.clear();

        pointsList.add(new Point(-50.0, 50.0));
        pointsList.add(new Point(50.0, 50.0));

        lineString = new LineString(pointsList);
        roadB = new Road(2, lineString);

        ArrayList<MapObject> roads = new ArrayList<>();
        roads.add(roadA);
        roads.add(roadB);

        point = new Point(0.0, 50.0);
        crossing = new Crossing(3, roads, point);
    }

    public void testGetRoads() throws Exception {
        assertEquals(roadA, crossing.getRoads().get(0));
        assertEquals(roadB, crossing.getRoads().get(1));
    }
}
