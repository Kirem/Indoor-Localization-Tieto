package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.LineString;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.map.Road;
import pl.wroc.pwr.indoorlocalizationtieto.map.Room;

public class Parser {

    // URL to get contacts JSON
    private static String url;

    // JSON Node names
    private static final String TAG_ELEMENTS = "elements";
    private static final String TAG_NODES = "nodes";
    private static final String TAG_TYPE = "type";
    private static final String TAG_ID = "id";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LON = "lon";
    private static final String TAG_TAGS = "tags";
    private static final String TAG_TAGS_BUILDINGPART = "buildingpart";
    private static final String TAG_TAGS_HIGHWAY = "highway";

    // elements JSONArray
    private JSONArray elements = null;
    private JSONArray JArray = null;

    private ArrayList<Node> nodesList = new ArrayList<>();
    private ArrayList<Way> waysList = new ArrayList<>();

    public Parser() {
        url = "http://www.overpass-api.de/api/interpreter?data=%5Bout%3Ajson%5D%3B+%28+++node%28poly%3A%2251.0955288+17.0194662+51.094649+17.0215236+51.0933816+17.0200614+51.094052+17.0174978%22%29%3B+++way%28poly%3A%2251.0955288+17.0194662+51.094649+17.0215236+51.0933816+17.0200614+51.094052+17.0174978%22%29%3B+%29%3B+out+body%3B+%3E%3B+out+skel+qt%3B";
    }

    public Parser(String s) {
        url = s;
    }

    public void GetElements() {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    elements = jsonObj.getJSONArray(TAG_ELEMENTS);

                    // looping through All elements
                    for (int i = 0; i < elements.length(); i++) {
                        JSONObject c = elements.getJSONObject(i);

                        String type = c.getString(TAG_TYPE);

                        if(type.equals("way"))
                        {
                            JArray = c.getJSONArray(TAG_NODES);
                            ArrayList<Long> idList = new ArrayList<>();
                            for (int j=0; j <JArray.length(); j++) {
                                idList.add(Long.parseLong(JArray.get(j).toString()));
                            }
                            long id = c.getLong(TAG_ID);

                            JSONObject jObjTags = c.getJSONObject(TAG_TAGS);
                            Map<String, String> tagMap = new HashMap<>();
                            tagMap.put(TAG_TAGS_BUILDINGPART, jObjTags.getString(TAG_TAGS_BUILDINGPART));
                            tagMap.put(TAG_TAGS_BUILDINGPART, jObjTags.getString(TAG_TAGS_HIGHWAY));

                            waysList.add(new Way(id, idList, tagMap));
                        }
                        else if(type.equals("node"))
                        {
                            long id = c.getLong(TAG_ID);
                            double lat = c.getDouble(TAG_LAT);
                            double lon = c.getDouble(TAG_LON);
                            Point point = new Point(lat, lon);

                            JSONObject jObjTags = c.getJSONObject(TAG_TAGS);
                            Map<String, String> tagMap = new HashMap<>();
                            /*
                            TODO uzupelnic tagi node'ow
                             */
                            nodesList.add(new Node(id, point, tagMap));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
    }

    public void ParseData() {
        /*
        TODO - uwzględnić relacje (rekurencja?)
         */
        ArrayList<Point> pointArr = new ArrayList<>();
        for (Way way : waysList) {
            for (Long nodeId : way.getNodesList()) {
                for (Node node : nodesList) {
                    if(nodeId == node.getId()) {
                        pointArr.add(node.getPoint());
                    }
                }
            }
            /*
            warunek tagow
             */
            if(way.getTags().get(TAG_TAGS_BUILDINGPART).equals("room")) {
                Polygon polygon = new Polygon(pointArr);
                Room room = new Room(way.getId(), polygon, false);
            } else if (way.getTags().get(TAG_TAGS_BUILDINGPART).equals("corridor")) {
                Polygon polygon = new Polygon(pointArr);
                Room room = new Room(way.getId(), polygon, true);
            } else if (way.getTags().containsKey(TAG_TAGS_HIGHWAY)) {
                LineString lineString = new LineString(pointArr);
                Road road = new Road(way.getId(), lineString);
            }
        }

        /*
        TODO zrobic to samo dla listy nodeList - sprawdzac czy node ma tagi - teśli ma to albo drzwi albo POI
         */
    }
}
