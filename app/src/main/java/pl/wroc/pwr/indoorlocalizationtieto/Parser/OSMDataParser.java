package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.Crossing;
import pl.wroc.pwr.indoorlocalizationtieto.map.Crossroad;
import pl.wroc.pwr.indoorlocalizationtieto.map.Elevator;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.map.POI;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by PiroACC on 2015-04-18.
 */

// Propozycja parsowania danych do mapy
public class OSMDataParser {

    private static final String HIGHWAY_TAGS[] = new String[]{"residential", "motorway", "primary",
            "secondary", "tertiary", "service", "trunk", "unclassified", "footway", "cycleway", "path"};
    //tablica tagów POI zostanie uzupełniona w trakcie mapowania budynku, trzeba ustalić jakie poi będą nas interesować
    private static final String POI_TAGS[] = new String[]{"router"};
    private static final String WAYS_TAGS[] = new String[]{"room", "corridor"};
    private static final String NODES_TAGS[] = new String[]{"crossing"};

    private List<Point> addPoints(List<Node> tempNodesList) {
        List<Point> tempPointsList = new ArrayList<>();
        for (Node tempNode : tempNodesList) {
            Point tempPoint = new Point(tempNode.getLatitude(), tempNode.getLongitude());
            tempPointsList.add(tempPoint);
        }
        return tempPointsList;
    }

    /*private Elevator addElevator(ArrayList<Room> elevatorList) {
        Point tempPoint = new Point();
    }*/

    public Map parseOSMData(OSMData osmData) {
        Map parsedMap = new Map();
        HashMap<Long, Way> tempWaysMap = osmData.getWaysMap();
        ArrayList<Room> elevatorList = new ArrayList<>();
        for (Long waysKey : tempWaysMap.keySet()) {
            Way tempWay = tempWaysMap.get(waysKey);
            if (tempWay.containsKey("highway") && tempWay.checkTagFromArray("highway", HIGHWAY_TAGS)) {
                List<Point> tempPointsList = addPoints(tempWay.getNodesList());
                LineString tempLineString = new LineString(tempPointsList);
                Road tempRoad = new Road(tempWay.getId(), tempLineString);
                parsedMap.addObject(tempRoad);
            } else if (tempWay.containsKey("buildingpart") && tempWay.checkTag("buildingpart", "room")) {
                List<Point> tempPointsList = addPoints(tempWay.getNodesList());
                Polygon tempPolygon = new Polygon(tempPointsList);
                Room tempRoom = new Room(tempWay.getId(), tempPolygon, false);
                parsedMap.addObject(tempRoom);
            } else if (tempWay.containsKey("buildingpart") && tempWay.checkTag("buildingpart", "corridor")) {
                List<Point> tempPointsList = addPoints(tempWay.getNodesList());
                Polygon tempPolygon = new Polygon(tempPointsList);
                Room tempRoom = new Room(tempWay.getId(), tempPolygon, true);
                parsedMap.addObject(tempRoom);
            } //TODO "naprawienie" działania parsowania windy
            else if (tempWay.containsKey("buildingpart:verticalpassage") && tempWay.checkTag("buildingpart:verticalpassage", "elevator")) {
                List<Point> tempPointsList = addPoints(tempWay.getNodesList());
                Polygon tempPolygon = new Polygon(tempPointsList);
                Room tempRoom = new Room(tempWay.getId(), tempPolygon, false);
                //TODO na razie będzie działać tylko dla jednej windy w budynku...
                if(elevatorList.size() == 0) {
                    elevatorList.add(tempRoom);
                } else {
                    //TODO ten if ma sprawdzac pierwszego noda pokoju w liscie z pierwszym nodem pokoju ktory sprawdzamy
                    if (elevatorList.get(0).getGeometries().get(0).equals(tempRoom.getGeometries().get(0))) {
                        elevatorList.add(tempRoom);
                    }
                }
            }
        }

        HashMap<Long, Node> tempNodesMap = osmData.getNodesMap();
        for (Long nodesKey : tempNodesMap.keySet()) {
            Node tempNode = tempNodesMap.get(nodesKey);
            //sprawdzanie czy w danym miejscu występuje przejście dla pieszych
            if (tempNode.containsKey("highway") && tempNode.checkTag("highway", "crossing")) {
                ArrayList<Road> tempRoadList = new ArrayList<>();
                //przejście dla pieszych z przecinajacymi sie wayami
                if (tempNode.getPartOf().size() > 1) {
                    for (OSMElement father : tempNode.getPartOf()) {
                        for (MapObject map : parsedMap.getObjects()) {
                            if (father.getType().equals("way") && father.getTags().containsKey("highway")) {
                                if (father.getId() == map.getId()) {
                                    tempRoadList.add((Road) map);
                                }
                            }
                        }
                    }
                    if (tempRoadList.size() > 1) {
                        //TODO przemyslec jak znaleźć referencję na punkt
                        Crossing tempCrossing = new Crossing(tempNode.getId(), tempRoadList, new Point(tempNode.getLatitude(), tempNode.getLongitude()));
                        parsedMap.addObject(tempCrossing);
                    }
                }
                //przejście dla pieszych bez przecinajacych sie wayow
                else if (tempNode.getPartOf().size() < 2) {
                    Crossing tempCrossing = new Crossing(tempNode.getId(), new Point(tempNode.getLatitude(), tempNode.getLongitude()));
                    parsedMap.addObject(tempCrossing);
                }
            } else if (tempNode.containsKey("poi") && tempNode.checkTagFromArray("poi", POI_TAGS)) {
                POI tempPoi = new POI(tempNode.getId(), new Point(tempNode.getLatitude(), tempNode.getLongitude()));
                parsedMap.addObject(tempPoi);
            } else { //skrzyżowanie dróg
                ArrayList<Road> tempRoadList = new ArrayList<>();
                if (tempNode.getPartOf().size() > 1) {
                    for (OSMElement father : tempNode.getPartOf()) {
                        for (MapObject map : parsedMap.getObjects()) {
                            if (father.getType().equals("way") && father.getTags().containsKey("highway")) {
                                if (father.getId() == map.getId()) {
                                    tempRoadList.add((Road) map);
                                }
                            }
                        }
                    }
                    //Crossing czy Crossroads?
                    //Crossing tempCrossing = new Crossing(tempNode.getId(), tempRoadList, new Point(tempNode.getLatitude(), tempNode.getLongitude()));
                    if (tempRoadList.size() > 1) {
                        Crossroad tempCrossroad = new Crossroad(tempNode.getId(), tempRoadList, new Point(tempNode.getLatitude(), tempNode.getLongitude()));
                        parsedMap.addObject(tempCrossroad);
                    }
                }
            }
        }
        return parsedMap;
    }
}