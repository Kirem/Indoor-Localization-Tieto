package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
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

    public Map parseOSMData(OSMData osmData) {
        Map parsedMap = new Map();
        HashMap<Long, Way> tempWaysMap = osmData.getWaysMap();
        for (Long waysKey : tempWaysMap.keySet()) {
            Way tempWay = tempWaysMap.get(waysKey);
            if (tempWay.containsKey("highway")) {
                if (tempWay.checkTagFromArray("highway", HIGHWAY_TAGS)) {
                    List<Point> tempPointsList = new ArrayList<>();
                    for (Node tempNode : tempWay.getNodesList()) {
                        Point tempPoint = new Point(tempNode.getLatitude(), tempNode.getLongitude());
                        tempPointsList.add(tempPoint);
                    }
                    LineString tempLineString = new LineString(tempPointsList);
                    Road tempRoad = new Road(tempWay.getId(), tempLineString);
                    parsedMap.addObject(tempRoad);
                }
            }
            else if (tempWay.containsKey("buildingpart")) {
                if (tempWay.checkTag("buildingpart", "room")) {
                    List<Point> tempPointsList = new ArrayList<>();
                    for (Node tempNode : tempWay.getNodesList()) {
                        Point tempPoint = new Point(tempNode.getLatitude(), tempNode.getLongitude());
                        tempPointsList.add(tempPoint);
                    }
                    Polygon tempPolygon = new Polygon(tempPointsList);
                    Room tempRoom = new Room(tempWay.getId(), tempPolygon, false);
                    parsedMap.addObject(tempRoom);
                }
            }
            else if (tempWay.containsKey("buildingpart")) {
                if (tempWay.checkTag("buildingpart", "corridor")) {
                    List<Point> tempPointsList = new ArrayList<>();
                    for (Node tempNode : tempWay.getNodesList()) {
                        Point tempPoint = new Point(tempNode.getLatitude(), tempNode.getLongitude());
                        tempPointsList.add(tempPoint);
                    }
                    Polygon tempPolygon = new Polygon(tempPointsList);
                    Room tempRoom = new Room(tempWay.getId(), tempPolygon, true);
                    parsedMap.addObject(tempRoom);
                }
            }
        }
        HashMap<Long, Node> tempNodesMap = osmData.getNodesMap();
        for (Long nodesKey : tempNodesMap.keySet()) {
            Node tempNode = tempNodesMap.get(nodesKey);
            if (tempNode.containsKey("highway")) {
                if (tempNode.checkTag("highway", "crossing")) {
                    //TODO dokończyć parsowanie POI.
                }
            }
        }
        return parsedMap;
    }
}