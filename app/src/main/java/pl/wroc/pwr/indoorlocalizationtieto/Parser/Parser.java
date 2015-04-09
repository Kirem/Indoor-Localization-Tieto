package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Multipolygon;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.Door;
import pl.wroc.pwr.indoorlocalizationtieto.map.Elevator;
import pl.wroc.pwr.indoorlocalizationtieto.map.Level;
import pl.wroc.pwr.indoorlocalizationtieto.map.POI;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

public class Parser {

    public Parser() {
    }

    public void parseRegion() {
        ArrayList<Point> pointArr = new ArrayList<>();

        /*
        Parsing Relations (Levels, romms, corridors etc.)
         */
        for (Map.Entry<Long, Relation> relationEntry : Elements.getRelationsList().entrySet()) {
            for (Members member : relationEntry.getValue().getMembers()) {
                ArrayList<Room> roomsList = new ArrayList<>();
                ArrayList<Door> doorsList = new ArrayList<>();
                ArrayList<Polygon> shapesList = new ArrayList<>();
                if (member.getType().equals("way")) {
                    Way way = Elements.getWaysList().get(member.getRef());
                    ArrayList<Point> points = new ArrayList<>();
                    for (Long nodeId : way.getNodesList()) {
                        Elements.getNodesList().get(nodeId);
                        points.add(Elements.getNodesList().get(nodeId).getPoint());
                    }

                    if (way.getTags().get(Tags.TAG_TAGS_BUILDINGPART).equals("room")) {
                        Polygon polygon = new Polygon(points);
                        Room room = new Room(way.getId(), polygon, false);
                        roomsList.add(room);
                        shapesList.add(polygon);

                        pl.wroc.pwr.indoorlocalizationtieto.map.Map.getInstance().addObject(room);
                    }

                    if (way.getTags().get(Tags.TAG_TAGS_BUILDINGPART).equals("corridor")) {
                        Polygon polygon = new Polygon(points);
                        Room room = new Room(way.getId(), polygon, true);
                        roomsList.add(room);
                        shapesList.add(polygon);

                        pl.wroc.pwr.indoorlocalizationtieto.map.Map.getInstance().addObject(room);
                    }

                    if (way.getTags().get(Tags.TAG_TAGS_HIGHWAY).equals("elevator")) {
                        /*
                        TODO MapObject Elevator do zmiany - jest bez sensu przy parsowaniu
                         */
                    }

                    if (way.getTags().get(Tags.TAG_TAGS_HIGHWAY).equals("steps")) {
                        /*
                        TODO tak samo jak winda
                         */
                    }
                }
                Level level = new Level(relationEntry.getValue().getId(),
                        new Multipolygon(shapesList),
                        Integer.parseInt(relationEntry.getValue().getTags().get(Tags.TAG_LEVEL)),
                        roomsList,
                        doorsList);
                pl.wroc.pwr.indoorlocalizationtieto.map.Map.getInstance().addObject(level);
            }
        }

        /*
        Parsing Ways (highways)
         */
        for (Map.Entry<Long, Way> wayEntry : Elements.getWaysList().entrySet()) {
            if (wayEntry.getValue().getTags().get(Tags.TAG_TAGS_HIGHWAY).equals("residential") ||
                    wayEntry.getValue().getTags().get(Tags.TAG_TAGS_HIGHWAY).equals("service") ||
                    wayEntry.getValue().getTags().get(Tags.TAG_TAGS_HIGHWAY).equals("footway")) {

                /*
                TODO dodac tagi http://wiki.openstreetmap.org/wiki/Key:highway
                 */
                ArrayList<Point> points = new ArrayList<>();
                for (Long nodeId : wayEntry.getValue().getNodesList()) {
                    Elements.getNodesList().get(nodeId);
                    points.add(Elements.getNodesList().get(nodeId).getPoint());
                }
                LineString lineString = new LineString(points);
                Road road = new Road(wayEntry.getValue().getId(), lineString);
                pl.wroc.pwr.indoorlocalizationtieto.map.Map.getInstance().addObject(road);
            }
        }

        /*
        Parsing Nodes (POI's)
         */
        for (Map.Entry<Long, Node> nodeEntry : Elements.getNodesList().entrySet()) {
            if (nodeEntry.getValue().getTags().get(Tags.TAG_TAGS_POI).equals("yes")) {
                POI poi = new POI(nodeEntry.getValue().getId(), nodeEntry.getValue().getPoint());
                pl.wroc.pwr.indoorlocalizationtieto.map.Map.getInstance().addObject(poi);
            }
        }
    }
}
