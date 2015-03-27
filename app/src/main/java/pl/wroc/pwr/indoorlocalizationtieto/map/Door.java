package pl.wroc.pwr.indoorlocalizationtieto.map;

import android.util.Pair;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

public class Door extends MapObject {
    Pair<Room, Room> connectedRooms;
    public Door(long id, Point p, Room room1, Room room2) {
        super(id, p);
        connectedRooms = new Pair<>(room1, room2);
    }

    public Room getFirstRoom(){
        return connectedRooms.first;
    }

    public Room getSecondRoom(){
        return connectedRooms.second;
    }

    public Pair<Room, Room> getConnectedRooms() {
        return connectedRooms;
    }

    public void setConnectedRooms(Pair<Room, Room> connectedRooms) {
        this.connectedRooms = connectedRooms;
    }

    public void setFirstRoom(Room room){
//        connectedRooms.
    }

    @Override
    ArrayList<Geometry> getGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        geometries.add(this.getObjectGeometry());
        return geometries;
    }
}