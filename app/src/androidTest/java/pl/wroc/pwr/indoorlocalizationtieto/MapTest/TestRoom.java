package pl.wroc.pwr.indoorlocalizationtieto.MapTest;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by Mateusz on 2015-03-27.
 */
public class TestRoom extends InstrumentationTestCase {

    ArrayList<Point> pointsList = new ArrayList<>();
    Polygon polygon;
    Room room;
    @Override
    public void setUp() throws Exception {

        pointsList.add(new Point(0.0, 0.0));
        pointsList.add(new Point(0.0, 10.0));
        pointsList.add(new Point(8.0, 10.0));
        pointsList.add(new Point(8.0, 0.0));

        polygon = new Polygon(pointsList);

        room = new Room(2, polygon, false);
    }

    public void testGetObjectGeometry() throws Exception {
        assertEquals(polygon, room.getObjectGeometry());
    }

    public void testGetGeometries() throws Exception {
        assertEquals(polygon, room.getGeometries().get(0));
    }
}
