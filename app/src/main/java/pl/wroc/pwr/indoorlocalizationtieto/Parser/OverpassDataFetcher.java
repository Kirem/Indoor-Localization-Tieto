package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.Pair;

public class OverpassDataFetcher {
    // URL with api service
    private String url;

    //TODO uzupelnic tagi !
    public static final String TAGS = "tags";
    public static final String NODES = "nodes";
    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String ELEMENTS = "elements";
    public static final String MEMBERS = "members";
    public static final String REF = "ref";
    public static final String ROLE = "role";


    public OverpassDataFetcher() {
        this("http://www.overpass-api.de/api/interpreter?data=");
    }

    public OverpassDataFetcher(String serviceUrl) {
        this.url = serviceUrl;
    }

   static private Map<String, String> buildMap(JSONObject jObj, String name) {
        JSONObject jObjTags = null;
        Map<String, String> tagMap = new HashMap<>();
        try {
            jObjTags = jObj.getJSONObject(name);
        } catch (JSONException e) {
            return tagMap;
        }
        Iterator<?> keys = jObjTags.keys();
        while (keys.hasNext()) {
            String tempKey = (String) keys.next();
            String tempValue = jObjTags.optString(tempKey);
            if (!(tempValue.isEmpty())) {
                tagMap.put(tempKey, tempValue);
            }
        }
        return tagMap;
    }

    public OSMData fetchDataWithinRadius(double latitude, double longitude, double radius) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("[out:json];" +
                "rel(around:" + radius + "," + latitude + "," + longitude + ");(._;>;);out;" +
                "way(around:" + radius + "," + latitude + "," + longitude + ");(._;>;);out;" +
                "node(around:" + radius + "," + latitude + "," + longitude + ");out;");
        String query = strBuilder.toString();
        return fetchData(query);
    }

    public OSMData fetchData(String query) {
        String encodedQuery = new String();
        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = url + encodedQuery;
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();
        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
        return parseFromJSON(jsonStr);
    }

    private OSMData parseFromJSON(String jsonStr) {
        OSMData osmData = new OSMData();
        // elements JSONArray
        JSONArray JElements;
        if (jsonStr == null) return null;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // Getting JSON Array node
            JElements = jsonObj.getJSONArray(ELEMENTS);
            // looping through All elements
            for (int i = 0; i < JElements.length(); i++) {
                JSONObject tempJSONOBject = JElements.getJSONObject(i);
                String type = tempJSONOBject.getString(TYPE);
                switch (type) {
                    case "node":
                        long nodesId = tempJSONOBject.getLong(ID);
                        double lat = tempJSONOBject.getDouble(LAT);
                        double lon = tempJSONOBject.getDouble(LON);

                        Map<String, String> nodesTagMap = buildMap(tempJSONOBject, TAGS);
                        osmData.addNode(nodesId, new Node(nodesId, lat, lon, nodesTagMap));
                        break;
                    case "way":
                        JSONArray nodesJArray = tempJSONOBject.getJSONArray(NODES);
                        ArrayList<Node> nodesList = new ArrayList<>();
                        for (int j = 0; j < nodesJArray.length(); j++) {
                            nodesList.add(osmData.getNode(Long.parseLong(nodesJArray.get(j).toString())));
                        }
                        long waysId = tempJSONOBject.getLong(ID);

                        Map<String, String> waysTagMap = buildMap(tempJSONOBject, TAGS);
                        osmData.addWay(waysId, new Way(waysId, nodesList, waysTagMap));
                        break;
                    case "relation":
                        JSONArray membersJArray = tempJSONOBject.getJSONArray(MEMBERS);
                        ArrayList<Pair<String, OSMElement>> relationsMembers = new ArrayList<>();
                        long relationsId = tempJSONOBject.getLong(ID);
                        for (int j = 0; j < membersJArray.length(); j++) {
                            JSONObject tempMember = membersJArray.getJSONObject(j);
                            String tempMemberType = tempMember.getString(TYPE);
                            long tempMemberRef = tempMember.getLong(REF);
                            String tempMemberRole = tempMember.optString(ROLE);
                            switch (tempMemberType) {
                                case "node":
                                    relationsMembers.add(new Pair<String, OSMElement>(tempMemberRole,
                                            osmData.getNode(tempMemberRef)));
                                    break;
                                case "way":
                                    relationsMembers.add(new Pair<String, OSMElement>(tempMemberRole,
                                            osmData.getWay(tempMemberRef)));
                                    break;
                                case "rel":
                                    relationsMembers.add(new Pair<String, OSMElement>(tempMemberRole,
                                            osmData.getRelation(tempMemberRef)));
                                    break;
                                default:
                                    break;
                            }
                        }
                        Map<String, String> tagMap = buildMap(tempJSONOBject, TAGS);
                        osmData.addRelation(relationsId, new Relation("relation", relationsId,
                                tagMap, relationsMembers));
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Exception: ", "> " + e.toString());
        }
        return osmData;
    }
}