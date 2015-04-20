package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.Crossing;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

/**
 * Created by PiroACC on 2015-04-18.
 */

// Propozycja parsowania danych do mapy
public class OSMDataParser {

    //TODO gdzie trzymac tagi, jak je wykorzystywac, jakie tagi potrzebujemy ?
    private static final String HIGHWAY_TAGS[] = new String[]{"residential", "motorway", "primary",
            "secondary", "tertiary", "service", "trunk", "unclassified"};
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

    public Map parseOSMData(OSMData osmData) {
        Map parsedMap = new Map();
        HashMap<Long, Way> tempWaysMap = osmData.getWaysMap();
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
            }
        }

        HashMap<Long, Node> tempNodesMap = osmData.getNodesMap();
        for (Long nodesKey : tempNodesMap.keySet()) {
            Node tempNode = tempNodesMap.get(nodesKey);
            //sprawdzanie czy w danym miejscu występuje skrzyżowanie dróg
            if (tempNode.containsKey("highway") && tempNode.checkTag("highway", "crossing")) {
                ArrayList<Road> tempRoadList = new ArrayList<>();
                //skrzyzowanie z przecinajacymi sie wayami
                if (tempNode.getPartOf().size() > 1) {
                    for (OSMElement father : tempNode.getPartOf()) {
                        for (MapObject map : parsedMap.getObjects()) {
                            if (father.getId() == map.getId()) {
                                tempRoadList.add((Road) map);
                            }
                        }
                    }
                    //TODO przemyslec jak znaleźć referencję na punkt
                    Crossing tempCrossing = new Crossing(tempNode.getId(), tempRoadList, new Point(tempNode.getLatitude(), tempNode.getLongitude()));
                    parsedMap.addObject(tempCrossing);
                }
                //skrzyzowanie bez przecinajacych sie wayow, np przejscie dla pieszych
                else if (tempNode.getPartOf().size() < 2) {
                    Crossing tempCrossing = new Crossing(tempNode.getId(), new Point(tempNode.getLatitude(), tempNode.getLongitude()));
                    parsedMap.addObject(tempCrossing);
                }
            }
        }
        return parsedMap;
    }
}