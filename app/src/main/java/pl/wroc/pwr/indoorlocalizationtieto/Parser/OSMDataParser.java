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
import pl.wroc.pwr.indoorlocalizationtieto.map.Building;
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
    private static final String POI_TAGS[] = new String[]{"router", "wc"};
    private static final String ROOM_TAGS[] = new String[]{"room", "corridor"};
    private static final String DOOR_TAGS[] = new String[]{"door", "entrance"};
    private static final String VERTICAL_PASSAGE_TAGS[] = new String[]{"elevator", "stairs", "escalator"};
    private static final String BUILDING_TAGS[] = new String[]{"yes", "university", "commercial",
            "hospital", "school", "public", "retail"};
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
    private Road createRoad(Way tempWay, String tagName) {
        List<Point> tempPointsList = addPoints(tempWay.getNodesList());
        LineString tempLineString = new LineString(tempPointsList);
        Road tempRoad = new Road(tempWay.getId(), tempLineString);
        tempRoad.setOptions(getOptions(tempWay, tagName));
        return tempRoad;
    }
    private Room createRoom(Way tempWay, String tagName) {
        List<Point> tempPointsList = addPoints(tempWay.getNodesList());
        Polygon tempPolygon = new Polygon(tempPointsList);
        Room tempRoom = new Room(tempWay.getId(), tempPolygon, tempWay.checkTag("buildingpart", "corridor"));
        tempRoom.setOptions(getOptions(tempWay, tagName));
        tempRoom.setName(tempWay.getTagValue("name"));
        return tempRoom;
    }
    private Crossing createCrossing(Node tempNode, ArrayList<Road> tempRoadList, String tagName) {
        Crossing tempCrossing = new Crossing(tempNode.getId(), tempRoadList,
                new Point(tempNode.getLatitude(), tempNode.getLongitude()),
                tempNode.checkTag("highway", "crossing"));
        tempCrossing.setOptions(getOptions(tempNode, tagName));
        return tempCrossing;
    }
    private POI createPoi(Node tempNode, String tagName) {
        POI tempPoi = new POI(tempNode.getId(),
                new Point(tempNode.getLatitude(), tempNode.getLongitude()));
        tempPoi.setOptions(getOptions(tempNode, tagName));
        return tempPoi;
    }
    private Elevator createElevator(Node tempNode, String tagName) {
        Elevator tempElevator = new Elevator(tempNode.getId(),
                new Point(tempNode.getLatitude(), tempNode.getLongitude()),false);
        tempElevator.setOptions(getOptions(tempNode, tagName));
        return tempElevator;
    }
    private Elevator createWaysElevator(Way tempWay, List<Point> tempPointsList, ArrayList<Room> tempElevatorsRoom) {
        Elevator tempElevator = new Elevator(tempWay.getNodesList().get(0).getId(), tempPointsList.get(0),
                tempElevatorsRoom, tempWay.checkTag("buildingpart:verticalpassage", "stairs"));
        tempElevator.setOptions(getOptions(tempWay, "buildingpart:verticalpassage"));
        return tempElevator;
    }
    private Door createDoor(Node tempNode, Map parsedMap) {
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
        tempDoor.setOptions(getOptions(tempNode, "door:safearea"));
        tempDoor.setOptions(getOptions(tempNode, "door:unsafearea"));
        return tempDoor;
    }
    private Crossing createCrossroad(Node tempNode, ArrayList<Road> tempRoadList) {
        Crossing tempCrossroad = new Crossing(tempNode.getId(), tempRoadList,
                new Point(tempNode.getLatitude(), tempNode.getLongitude()),
                tempNode.checkTag("highway", "crossing"));
        tempCrossroad.setOptions(crossroadTags);
        return tempCrossroad;
    }
    private Level createLevel(Relation tempRelation, List<Polygon> tempPolygons, ArrayList<Room> tempRooms, ArrayList<Door> tempDoors) {
        Multipolygon tempMultipolygon = new Multipolygon(tempPolygons);
        Level tempLevel = new Level(tempRelation.getId(), tempMultipolygon,
                Integer.parseInt(tempRelation.getTagValue("level")), tempRooms, tempDoors);
        tempLevel.setOptions(getOptions(tempRelation, "level"));
        return tempLevel;
    }
    private Building createBuilding(Relation tempRelation, Polygon tempPolygon, ArrayList<Level> tempLevels) {
        Building tempBuilding = new Building(tempRelation.getId(), tempPolygon, tempLevels);
        tempBuilding.setOptions(getOptions(tempRelation, "building"));
        return tempBuilding;
    }
    private ArrayList<Door> createDoorsList(Relation tempRelation, Map parsedMap) {
        List<OSMElement> osmNodesElements = tempRelation.getMembersList("node");
        ArrayList<Door> tempDoors = new ArrayList<>();
        for (OSMElement node : osmNodesElements) {
            for (MapObject object : parsedMap.getObjects()) {
                if (node.checkTagFromArray("buildingpart", DOOR_TAGS)) {
                    if (node.getId() == object.getId()) {
                        if (object.getOptions().containsValue("door")
                                || object.getOptions().containsValue("entrance")) {
                            tempDoors.add((Door) object);
                        }
                    }
                }
            }
        }
        return tempDoors;
    }
    private ArrayList<Road> createRoadsList(Node tempNode, Map parsedMap, ArrayList<Road> tempRoadList) {
        for (OSMElement father : tempNode.getPartOf()) {
            if (father.getType().equals("way") && father.getTags().containsKey("highway")) {
                for (MapObject map : parsedMap.getObjects()) {
                    if (father.getId() == map.getId()) {
                        tempRoadList.add((Road) map);
                    }
                }
            }
        }
        return tempRoadList;
    }
    private ArrayList<Room> createRoomsList(Relation tempRelation, Map parsedMap, List<Polygon> tempPolygons) {
        List<OSMElement> osmWaysElements = tempRelation.getMembersList("way");
        ArrayList<Room> tempRooms = new ArrayList<>();
        for (OSMElement way : osmWaysElements) {
            for (MapObject object : parsedMap.getObjects()) {
                if (way.checkTagFromArray("buildingpart", ROOM_TAGS)) {
                    if (way.getId() == object.getId()) {
                        tempPolygons.add((Polygon) object.getObjectGeometry());
                        if (object.getOptions().containsValue("room")
                                || object.getOptions().containsValue("corridor")) {
                            tempRooms.add((Room) object);
                        }
                    }
                }
            }
        }
        return tempRooms;
    }
    private ArrayList<Level> createLevelsList(Relation tempRelation, Map parsedMap) {
        List<OSMElement> osmRelsElements = tempRelation.getMembersList("relation");
        ArrayList<Level> tempLevels = new ArrayList<>();
        for (OSMElement relation : osmRelsElements) {
            if (relation.getTags().containsKey("level")) {
                for (MapObject object : parsedMap.getObjects()) {
                    if (relation.getId() == object.getId()){
                        tempLevels.add((Level)object);
                    }
                }
            }
        }
        return tempLevels;
    }
    private Polygon createPolygon(Relation tempRelation, Map parsedMap) {
        List<OSMElement> osmWaysElements = tempRelation.getMembersList("way");
        Polygon tempPolygon = new Polygon();
        for (OSMElement way : osmWaysElements) {
            if (way.getTags().containsKey("building")) {
                for (MapObject object : parsedMap.getObjects()) {
                    if (way.getId() == object.getId()){
                        tempPolygon = (Polygon) object.getObjectGeometry();
                    }
                }
            }
        }
        return tempPolygon;
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
                parsedMap.addObject(createRoad(tempWay, "highway"));
            } else if (tempWay.checkTagFromArray("building", BUILDING_TAGS)) {
                parsedMap.addObject(createRoom(tempWay, "building"));
            } else if (tempWay.checkTagFromArray("buildingpart", ROOM_TAGS)) {
                parsedMap.addObject(createRoom(tempWay, "buildingpart"));
            } else if (tempWay.checkTagFromArray("buildingpart:verticalpassage", VERTICAL_PASSAGE_TAGS)
                    || tempWay.checkTagFromArray("builpart:verticalpassage", VERTICAL_PASSAGE_TAGS)) {
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
                    parsedMap.addObject(createWaysElevator(tempWay, tempPointsList, tempElevatorsRoom));
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
                    tempRoadList = createRoadsList(tempNode, parsedMap, tempRoadList);
                    if (tempRoadList.size() > 1) {
                        parsedMap.addObject(createCrossing(tempNode, tempRoadList, "highway"));
                    }
                }
                //przejście dla pieszych bez przecinajacych sie wayow
                else if (tempNode.getPartOf().size() < 2) {
                    parsedMap.addObject(createCrossing(tempNode, tempRoadList, "highway"));
                }
            } else if (tempNode.checkTagFromArray("poi", POI_TAGS)) {
                parsedMap.addObject(createPoi(tempNode, "poi"));
            } else if (tempNode.checkTag("elevator", "yes")) {
                parsedMap.addObject(createElevator(tempNode, "elevator"));
            }else if (tempNode.checkTagFromArray("buildingpart", DOOR_TAGS)) {
                parsedMap.addObject(createDoor(tempNode, parsedMap));
            }  else if (tempNode.getPartOf().size() > 1) {        //parsowanie skrzyzowan
                ArrayList<Road> tempRoadList = new ArrayList<>();
                tempRoadList = createRoadsList(tempNode, parsedMap, tempRoadList);
                if (tempRoadList.size() > 1) {
                    parsedMap.addObject(createCrossroad(tempNode, tempRoadList));
                }
            }
        }
        Log.i("OBJECTS", "BEZ REALCJI: " + parsedMap.getObjects().size());

        //Parsowanie relacji
        HashMap<Long, Relation> tempRelationsMap = osmData.getRelationsMap();
        for (Long relationsKey : tempRelationsMap.keySet()) {
            Relation tempRelation = tempRelationsMap.get(relationsKey);
            List<Polygon> tempPolygons = new ArrayList<>();
            //Parsowanie pięter
            if (tempRelation.checkTag("type", "level")) {
                parsedMap.addObject(createLevel(tempRelation, tempPolygons,
                        createRoomsList(tempRelation, parsedMap, tempPolygons),
                        createDoorsList(tempRelation, parsedMap)));
            }
            //Parsowanie budynkow
            else if(tempRelation.checkTag("building", "yes")) {
                parsedMap.addObject(createBuilding(tempRelation,
                        createPolygon(tempRelation, parsedMap),
                        createLevelsList(tempRelation, parsedMap)));
            }
        }
        return parsedMap;
    }
}