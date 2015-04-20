package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.Crossing;
import pl.wroc.pwr.indoorlocalizationtieto.map.Elevator;
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
    private static final String POI_TAGS[] = new String[]{"router"};
    private static final String ROOM_TAGS[] = new String[]{"room", "corridor"};
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
                    || tempWay.checkTag("builpart:verticalpassage", "elevator")) {
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
                            tempElevatorsRoom);
                    //TODO Nie mam co do tego pewnosci (co tu ma byc), poniewaz winda to
                    //TODO builpart:verticalpassage oraz buildingpart:verticalpassage
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
                        //TODO przemyslec jak znaleźć referencję na punkt
                        Crossing tempCrossing = new Crossing(tempNode.getId(), tempRoadList,
                                new Point(tempNode.getLatitude(), tempNode.getLongitude()),
                                tempNode.checkTag("highway", "crossing"));
                        //Co tu za string wrzucic highway czy crossing
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
                        new Point(tempNode.getLatitude(), tempNode.getLongitude()));
                tempElevator.setOptions(getOptions(tempNode, "elevator"));
                parsedMap.addObject(tempElevator);
            } else { //parsowanie skrzyżowań dróg
                ArrayList<Road> tempRoadList = new ArrayList<>();
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
                        Crossing tempCrossroad = new Crossing(tempNode.getId(), tempRoadList,
                                new Point(tempNode.getLatitude(), tempNode.getLongitude()),
                                tempNode.checkTag("highway", "crossing"));
                        //poniewaz nody, ktore sa skrzyzowaniami nie musza miec specjalnych tagow, wrzucam z palca tagi highway crossroad
                        //Mozliwe uzycie getOptions - aczkolwiek nie lapie sie jak - Piotrek
                        tempCrossroad.setOptions(crossroadTags);
                        parsedMap.addObject(tempCrossroad);
                    }
                }
            }
        }
        return parsedMap;
    }
}