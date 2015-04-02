package pl.wroc.pwr.indoorlocalizationtieto.MapTest;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;

/**
 * Created by Mateusz on 2015-03-27.
 */
public class TestRoad extends InstrumentationTestCase {

    ArrayList<Point> pointsList = new ArrayList<>();
    LineString lineString;
    Road road;

    @Override
    public void setUp() throws Exception {
        pointsList.add(new Point(0.0, 0.0));
        pointsList.add(new Point(0.0, 10.0));
        pointsList.add(new Point(8.0, 10.0));
        pointsList.add(new Point(8.0, 15.0));

        lineString = new LineString(pointsList);
        road = new Road(1, lineString);
    }

    public void testGetObjectGeometry() throws Exception {
        assertEquals(lineString, road.getObjectGeometry());
    }

    public void testGetGeometries() throws Exception {
        assertEquals(lineString, road.getGeometries().get(0));
    }
}
