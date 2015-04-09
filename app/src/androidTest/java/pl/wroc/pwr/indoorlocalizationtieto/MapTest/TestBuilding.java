package pl.wroc.pwr.indoorlocalizationtieto.MapTest;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Multipolygon;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.Building;
import pl.wroc.pwr.indoorlocalizationtieto.map.Door;
import pl.wroc.pwr.indoorlocalizationtieto.map.Level;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by Mateusz on 2015-04-02.
 */
public class TestBuilding extends InstrumentationTestCase {

    Building building;
    Level levelA, levelB;
    Multipolygon multipolygon;
    ArrayList<Polygon> polygonsList = new ArrayList<>();
    ArrayList<Level> levels = new ArrayList<>();
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

        levelA = new Level(12, multipolygon, 0, rooms, doors);

        rooms.clear();
        rooms.add(roomB);
        rooms.add(roomA);

        levelB = new Level(13, multipolygon, 1, rooms, doors);

        levels.add(levelA);
        levels.add(levelB);

        polygonsList.addAll(polygonsList);

        multipolygon = new Multipolygon(polygonsList);

        building = new Building(15, multipolygon, levels);
    }

    public void testGetLevelOutOfBounds() {
        assertEquals(null, building.getLevel(3));
    }

    public void testGetLevel() throws Exception {
        assertEquals(building.getLevel(0), levelA);
        assertNotSame(building.getLevel(0), levelB);

        assertEquals(building.getLevel(1), levelB);
        assertNotSame(building.getLevel(1), levelA);
    }
}
