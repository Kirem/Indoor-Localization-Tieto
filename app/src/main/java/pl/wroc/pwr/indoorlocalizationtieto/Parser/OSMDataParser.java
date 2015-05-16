package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Multipolygon;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.Crossing;
import pl.wroc.pwr.indoorlocalizationtieto.map.Door;
import pl.wroc.pwr.indoorlocalizationtieto.map.Elevator;
import pl.wroc.pwr.indoorlocalizationtieto.map.Level;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.map.POI;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by PiroACC on 2015-04-18.
 */
public class OSMDataParser {

    private static final String HIGHWAY_TAGS[] = new String[]{"residential", "motorway", "primary",
            "secondary", "tertiary", "service", "trunk", "unclassified", "footway", "cycleway", "path"};
    //TODO tablica tagów POI zostanie uzupełniona w trakcie mapowania budynku,
    //TODO trzeba ustalić jakie poi będą nas interesować
    private static final String POI_TAGS[] = new String[]{"router", "wc"};
    private static final String ROOM_TAGS[] = new String[]{"room", "corridor"};
    private static final String DOOR_TAGS[] = new String[]{"door", "entrance"};
    private static final String NODES_TAGS[] = new String[]{"crossing"};
    private static java.util.Map<String, String> crossroadTags = new HashMap<>();

    private List<Point> addPoints(List<Node> tempNodesList) {
        List<Point> tempPointsList = new ArrayList<>();
        for (Node tempNode : tempNodesList) {
            Point tempPoint = new Point(tempNode.getLatitude(), tempNode.getLongitude());
            tempPointsList.add(tempPoint);
        }
        return tempPointsList;
    }

    private ArrayMap<String, String> getOptions(OSMElement object, String objectClass) {
        ArrayMap<String, String> options = new ArrayMap<>();
        options.put(MapObject.OBJECT_CLASS, objectClass);
        options.put(MapObject.OBJECT_TYPE,
                object.getTags().get(objectClass));
        return options;
    }

    public Map parseOSMData(OSMData osmData) {
        Log.i("OBJECTS TO PARSE" ,"WAYE " +osmData.getWaysMap().size());
        Log.i("OBJECTS TO PARSE" ,"NODEY " +osmData.getNodesMap().size());
        Log.i("OBJECTS TO PARSE" ,"RELACJE "+ osmData.getRelationsMap().size());

        crossroadTags.put("highway", "crossroad");
        Map parsedMap = new Map();
        HashMap<Long, Way> tempWaysMap = osmData.getWaysMap();
        for (Long waysKey : tempWaysMap.keySet()) {
            Way tempWay = tempWaysMap.get(waysKey);
            if (tempWay.checkTagFromArray("highway", HIGHWAY_TAGS)) {
                List<Point> tempPointsList = addPoints(tempWay.getNodesList());
                LineString tempLineString = new LineString(tempPointsList);
                Road tempRoad = new Road(tempWay.getId(), tempLineString);
                tempRoad.setOptions(getOptions(tempWay, "highway"));
                parsedMap.addObject(tempRoad);
            } else if (tempWay.checkTagFromArray("buildingpart", ROOM_TAGS)) {
                List<Point> tempPointsList = addPoints(tempWay.getNodesList());
                Polygon tempPolygon = new Polygon(tempPointsList);
                Room tempRoom = new Room(tempWay.getId(), tempPolygon, tempWay.checkTag("buildingpart", "corridor"));
                tempRoom.setOptions(getOptions(tempWay, "buildingpart"));
                parsedMap.addObject(tempRoom);
            } else if (tempWay.checkTag("buildingpart:verticalpassage", "elevator")
                    || tempWay.checkTag("builpart:verticalpassage", "elevator")
                    || tempWay.checkTag("buildingpart:verticalpassage", "stairs")) {
                List<Point> tempPointsList = addPoints(tempWay.getNodesList());
                Polygon tempPolygon = new Polygon(tempPointsList);
                Room tempRoom = new Room(tempWay.getId(), tempPolygon, false);
                ArrayList<Room> tempElevatorsRoom = new ArrayList<>();
                tempElevatorsRoom.add(tempRoom);
                boolean addedRoom = false;
                for (MapObject mapObject : parsedMap.getObjects()) {
                    if (mapObject.getClass() == Elevator.class) {
                        Elevator elevator = (Elevator) mapObject;
                        if (elevator.getId() == tempWay.getNodesList().get(0).getId()) {
                            elevator.addRoom(tempRoom);
                            addedRoom = true;
                            break;
                        }
                    }
                }
                if (!addedRoom) {
                    Elevator tempElevator = new Elevator(tempWay.getNodesList().get(0).getId(), tempPointsList.get(0),
                            tempElevatorsRoom, tempWay.checkTag("buildingpart:verticalpassage", "stairs"));
                    tempElevator.setOptions(getOptions(tempWay, "buildingpart:verticalpassage"));
                    parsedMap.addObject(tempElevator);
                }
            }
        }

        HashMap<Long, Node> tempNodesMap = osmData.getNodesMap();
        for (Long nodesKey : tempNodesMap.keySet()) {
            Node tempNode = tempNodesMap.get(nodesKey);
            //sprawdzanie czy w danym miejscu występuje przejście dla pieszych
            if (tempNode.checkTag("highway", "crossing")) {
                ArrayList<Road> tempRoadList = new ArrayList<>();
                //przejście dla pieszych z przecinajacymi sie wayami
                if (tempNode.getPartOf().size() > 1) {
                    for (OSMElement father : tempNode.getPartOf()) {
                        for (MapObject map : parsedMap.getObjects()) {
                            if (father.getType().equals("way")
                                    && father.getTags().containsKey("highway")) {
                                if (father.getId() == map.getId()) {
                                    tempRoadList.add((Road) map);
                                }
                            }
                        }
                    }
                    if (tempRoadList.size() > 1) {
                        Crossing tempCrossing = new Crossing(tempNode.getId(), tempRoadList,
                                new Point(tempNode.getLatitude(), tempNode.getLongitude()),
                                tempNode.checkTag("highway", "crossing"));
                        tempCrossing.setOptions(getOptions(tempNode, "highway"));
                        parsedMap.addObject(tempCrossing);
                    }
                }
                //przejście dla pieszych bez przecinajacych sie wayow
                else if (tempNode.getPartOf().size() < 2) {
                    Crossing tempCrossing = new Crossing(tempNode.getId(),
                            new Point(tempNode.getLatitude(), tempNode.getLongitude()),
                            tempNode.checkTag("highway", "crossing"));
                    tempCrossing.setOptions(getOptions(tempNode, "highway"));

                    parsedMap.addObject(tempCrossing);
                }
            } else if (tempNode.checkTagFromArray("poi", POI_TAGS)) {
                POI tempPoi = new POI(tempNode.getId(),
                        new Point(tempNode.getLatitude(), tempNode.getLongitude()));
                tempPoi.setOptions(getOptions(tempNode, "poi"));
                parsedMap.addObject(tempPoi);
            } else if (tempNode.checkTag("elevator", "yes")) {
                Elevator tempElevator = new Elevator(tempNode.getId(),
                        new Point(tempNode.getLatitude(), tempNode.getLongitude()),false);
                tempElevator.setOptions(getOptions(tempNode, "elevator"));
                parsedMap.addObject(tempElevator);
            }else if (tempNode.checkTag("buildingpart", "door")
                    || tempNode.checkTag("buildingpart", "entrance")) {
                Door tempDoor = new Door(tempNode.getId(), new Point(tempNode.getLatitude(),
                        tempNode.getLongitude()), null, null);
                if (tempNode.getTags().containsKey("door:safearea")) {
                    for (MapObject tempRooms : parsedMap.getObjects()) {
                        if (tempRooms.getId().equals(tempNode.getTagValue("door:saferea"))) {
                            tempDoor.setFirstRoom((Room) tempRooms);
                        } else if (tempRooms.getId().equals(tempNode.getTagValue("door:unsaferea"))) {
                            tempDoor.setSecondRoom((Room) tempRooms);
                        }
                    }
                }
                tempDoor.setOptions(getOptions(tempNode, "buildingpart"));
                parsedMap.addObject(tempDoor);
            }  else if (tempNode.getPartOf().size() > 1) {        //parsowanie skrzyzowan
                ArrayList<Road> tempRoadList = new ArrayList<>();
                for (OSMElement father : tempNode.getPartOf()) {
                    if (father.getType().equals("way") && father.getTags().containsKey("highway")) {
                        for (MapObject map : parsedMap.getObjects()) {
                            if (father.getId() == map.getId()) {
                                tempRoadList.add((Road) map);
                            }
                        }
                    }
                }
                if (tempRoadList.size() > 1) {
                    Crossing tempCrossroad = new Crossing(tempNode.getId(), tempRoadList,
                            new Point(tempNode.getLatitude(), tempNode.getLongitude()),
                            tempNode.checkTag("highway", "crossing"));
                    tempCrossroad.setOptions(crossroadTags);
                    parsedMap.addObject(tempCrossroad);
                }
            }
        }
        Log.i("OBJECTS", "BEZ REALCJI: " + parsedMap.getObjects().size());

        //Parsowanie relacji
        HashMap<Long, Relation> tempRelationsMap = osmData.getRelationsMap();
        List<OSMElement> osmWaysElements, osmNodesElements, osmRelsElements;
        for (Long relationsKey : tempRelationsMap.keySet()) {
            Relation tempRelation = tempRelationsMap.get(relationsKey);
            osmWaysElements = tempRelation.getMembersList("way");
            osmNodesElements = tempRelation.getMembersList("node");
            osmRelsElements = tempRelation.getMembersList("relation");
            List<Polygon> tempPolygons = new ArrayList<>();
            ArrayList<Room> tempRooms = new ArrayList<>();
            ArrayList<Door> tempDoors = new ArrayList<>();
            //Parsowanie pięter
            if (tempRelation.checkTag("type", "level")) {
                //Szukanie elementów piętra ( przeglad po wayach danej relacji)
                for (OSMElement element : osmWaysElements) {
                    for (MapObject object : parsedMap.getObjects()) {
                        if (element.checkTagFromArray("buildingpart", ROOM_TAGS)) {
                            if (element.getId() == object.getId()) {
                                tempPolygons.add((Polygon) object.getObjectGeometry());
                                if (object.getOptions().containsValue("room")
                                        || object.getOptions().containsValue("corridor")) {
                                    tempRooms.add((Room) object);
                                }
                            }
                        }
                    }
                }
                //Szukanie drzwi dla danego pietra

                for (OSMElement element : osmNodesElements) {
                    for (MapObject object : parsedMap.getObjects()) {
                        if (element.checkTagFromArray("buildingpart", DOOR_TAGS)) {
                            if (element.getId() == object.getId()) {
                                if (object.getOptions().containsValue("door")
                                        || object.getOptions().containsValue("entrance")) {
                                    tempDoors.add((Door) object);
                                }
                            }
                        }
                    }
                }

                Multipolygon tempMultipolygon = new Multipolygon(tempPolygons);
                Level tempLevel = new Level(tempRelation.getId(), tempMultipolygon,
                        Integer.parseInt(tempRelation.getTagValue("level")), tempRooms, tempDoors);
                parsedMap.addObject(tempLevel);
            } 
        }
        return parsedMap;
    }
}