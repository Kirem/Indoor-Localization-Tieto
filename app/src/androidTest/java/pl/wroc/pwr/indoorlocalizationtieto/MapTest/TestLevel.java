package pl.wroc.pwr.indoorlocalizationtieto.MapTest;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Multipolygon;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.Door;
import pl.wroc.pwr.indoorlocalizationtieto.map.Level;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by Mateusz on 2015-03-29.
 */
public class TestLevel extends InstrumentationTestCase {

    Level level;
    Multipolygon multipolygon;
    ArrayList<Polygon> polygonsList = new ArrayList<>();
    ArrayList<Door> doors = new ArrayList<>();
    ArrayList<Room> rooms = new ArrayList<>();

    @Override
    public void setUp() throws Exception {

        Point point;
        Polygon polygon;

        ArrayList<Point> pointsList = new ArrayList<>();
        Room roomA, roomB;
        Door door;

        pointsList.add(new Point(0.0, 0.0));
        pointsList.add(new Point(0.0, 10.0));
        pointsList.add(new Point(8.0, 10.0));
        pointsList.add(new Point(8.0, 0.0));

        polygon = new Polygon(pointsList);
        roomA = new Room(1, polygon, false);
        polygonsList.add(polygon);

        pointsList.clear();

        pointsList.add(new Point(0.0, 10.0));
        pointsList.add(new Point(0.0, 15.0));
        pointsList.add(new Point(8.0, 15.0));
        pointsList.add(new Point(8.0, 10.0));

        polygon = new Polygon(pointsList);
        roomB = new Room(2, polygon, true);
        polygonsList.add(polygon);

        point = new Point(4.0, 10.0);

        door = new Door(3, point, roomA, roomB);

        doors.add(door);
        rooms.add(roomA);
        rooms.add(roomB);

        multipolygon = new Multipolygon(polygonsList);

        level = new Level(12, multipolygon, 0, rooms, doors);
    }

    public void testGettersSetters() throws Exception {
        assertEquals(2, level.getRooms().size());
        assertEquals(1, level.getDoors().size());

        assertEquals(doors.get(0), level.getDoors().get(0));
        assertEquals(rooms.get(0), level.getRooms().get(0));
        assertEquals(rooms.get(1), level.getRooms().get(1));
    }

    public void testGetGeometries() throws Exception {
        assertTrue(level.getGeometries().contains(multipolygon));
        assertTrue(level.getGeometries().contains(rooms.get(0).getObjectGeometry()));
        assertTrue(level.getGeometries().contains(rooms.get(1).getObjectGeometry()));
        assertTrue(level.getGeometries().contains(doors.get(0).getObjectGeometry()));
    }
}
