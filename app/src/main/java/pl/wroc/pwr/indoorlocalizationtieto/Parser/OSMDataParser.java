package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;

/**
 * Created by PiroACC on 2015-04-18.
 */

// Propozycja parsowania danych do mapy
public class OSMDataParser {

    //TODO gdzie trzymac taki, jak je wykorzystywac, jakie tagi potrzebujemy ?
    private static final String WAYS_TAGS[] = new String[]{"residential"};

    public Map parseOSMData(OSMData osmData) {
        Map parsedMap = new Map();
        HashMap<Long, Way> tempWaysMap = osmData.getWaysMap();
        for (Long waysKey : tempWaysMap.keySet()) {
            Way tempWay = tempWaysMap.get(waysKey);
            if (tempWay.getTags().containsKey("highway")) {
                if (tempWay.getTags().get("highway").equals("residential")) {
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
        }
        return parsedMap;
    }
}
