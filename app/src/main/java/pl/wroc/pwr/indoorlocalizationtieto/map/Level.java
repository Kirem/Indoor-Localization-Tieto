package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Multipolygon;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;

public class Level extends MapObject {
    private ArrayList<Room> rooms;
    private ArrayList<Door> doors;
    private float currentLevel;

    public Level(long id, Multipolygon shape, int currentLevel) {
        super(id, shape);
        this.currentLevel = currentLevel;
        rooms = new ArrayList<>();
        doors = new ArrayList<>();
    }

    public Level(long id, Multipolygon shape, int currentLevel, ArrayList<Room> rooms, ArrayList<Door> doors) {
        super(id, shape);
        this.currentLevel = currentLevel;
        this.rooms = new ArrayList<>(rooms);
        this.doors = new ArrayList<>(doors);
    }

    public float getCurrentLevel() {
        return currentLevel;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setDoors(ArrayList<Door> doors) {
        this.doors = doors;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

    public void addDoor(Door door){
        doors.add(door);
    }


    public ArrayList<Geometry> getGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        geometries.add(this.getObjectGeometry());
        for(MapObject object:doors)
            geometries.add(object.getObjectGeometry());
        for(MapObject object:rooms)
            geometries.add(object.getObjectGeometry());
        return geometries;
    }
}
