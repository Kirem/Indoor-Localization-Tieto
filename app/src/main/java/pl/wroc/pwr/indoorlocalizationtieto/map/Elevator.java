package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

public class Elevator extends MapObject {
    ArrayList<Room> rooms;

    public Elevator(long id, Point point) {
        super(id, point);
    }

    public Elevator(long id, Point point, ArrayList<Room> rooms) {
        super(id, point);
        this.rooms = new ArrayList<>(rooms);
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }


    public ArrayList<Geometry> getGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        for (MapObject object : rooms)
            geometries.add(object.getObjectGeometry());
        return geometries;
    }
}
