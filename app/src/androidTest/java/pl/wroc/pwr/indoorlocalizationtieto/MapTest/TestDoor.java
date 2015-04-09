package pl.wroc.pwr.indoorlocalizationtieto.MapTest;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.Door;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by Mateusz on 2015-03-27.
 */
public class TestDoor extends InstrumentationTestCase {
    ArrayList<Point> pointsList = new ArrayList<>();
    Polygon polygon;
    Room roomA, roomB;
    Door door;
    Point point;

    @Override
    public void setUp() throws Exception {

        pointsList.add(new Point(0.0, 0.0));
        pointsList.add(new Point(0.0, 10.0));
        pointsList.add(new Point(8.0, 10.0));
        pointsList.add(new Point(8.0, 0.0));

        polygon = new Polygon(pointsList);
        roomA = new Room(1, polygon, false);

        pointsList.clear();

        pointsList.add(new Point(0.0, 10.0));
        pointsList.add(new Point(0.0, 15.0));
        pointsList.add(new Point(8.0, 15.0));
        pointsList.add(new Point(8.0, 10.0));

        polygon = new Polygon(pointsList);
        roomB = new Room(2, polygon, true);

        point = new Point(4.0, 10.0);

        door = new Door(3, point, roomA, roomB);
    }

    public void testGetGeometries() throws Exception {
        assertEquals(point, door.getGeometries().get(0));
    }

    public void testGetConnectedRooms() throws Exception {
        assertEquals(roomA, door.getConnectedRooms().first);
        assertEquals(roomB, door.getConnectedRooms().second);
    }
}
