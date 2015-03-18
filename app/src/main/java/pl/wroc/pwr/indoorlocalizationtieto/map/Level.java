package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;

public class Level extends MapObject {
    private ArrayList<Room> rooms;
    private ArrayList<Door> doors;
    private int currentLevel;
    //TODO zmienic nazwe currentLevel...

    public Level(Polygon shape, int currentLevel) {
        super(shape);
        this.currentLevel = currentLevel;
        rooms = new ArrayList<>();
        doors = new ArrayList<>();
    }

    public Level(Polygon shape, int currentLevel, ArrayList<Room> rooms, ArrayList<Door> doors) {
        super(shape);
        this.currentLevel = currentLevel;
        this.rooms = new ArrayList<>(rooms);
        this.doors = new ArrayList<>(doors);
    }

    public int getCurrentLevel() {
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
        for(MapObject object:doors)
            geometries.add(object.getObjectGeometry());
        for(MapObject object:rooms)
            geometries.add(object.getObjectGeometry());
        return geometries;
    }
}
