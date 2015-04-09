package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

public class Elements {
    // URL with api service
    private static String SERVICE_URL = "http://www.overpass-api.de/api/interpreter?data=";
    private static String url;

    // elements JSONArray
    static private JSONArray JElements = null;
    static private JSONArray JArray = null;
    static private JSONArray members = null;

    static private HashMap<Long, Node> nodesList = new HashMap<>();
    static private HashMap<Long, Way> waysList = new HashMap<>();
    static private HashMap<Long, Relation> relationsList = new HashMap<>();
    static private ArrayList<Members> membersList = new ArrayList<>();

    public static HashMap<Long, Node> getNodesList() {
        return nodesList;
    }

    public static HashMap<Long, Way> getWaysList() {
        return waysList;
    }

    public static ArrayList<Members> getMembersList() {
        return membersList;
    }

    public static HashMap<Long, Relation> getRelationsList() {
        return relationsList;
    }

    public Elements(double latitude, double longitude, double r) {
        fetchElements(latitude, longitude, r);
    }

    /**
     * Method to generate query according to schema
     * <p/>
     * [out:json];
     * rel(around:radius,latitude,longitude);out;
     * way(around:radius,latitude,longitude);out;
     * node(around:radius,latitude,longitude);out;
     *
     * @param latitude  current latitude
     * @param longitude current longitude
     * @param radius    given radius of area to get data from
     * @return
     */
    static private String generateQuery(double latitude, double longitude, double radius) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("[out:json];" +
                "rel(around:" + radius + "," + latitude + "," + longitude + ");out;" +
                "way(around:" + radius + "," + latitude + "," + longitude + ");out;" +
                "node(around:" + radius + "," + latitude + "," + longitude + ");out;");
        String query = strBuilder.toString();
        return query;
    }

    static private String generateEncodedQuery(double latitude, double longitude, double r) {
        String query = generateQuery(latitude, longitude, r);
        String encodedQuery = new String();

        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedQuery;
    }

    static public void fetchElements(double latitude, double longitude, double r) {
        url = SERVICE_URL + generateEncodedQuery(latitude, longitude, r);
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();
        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

        //Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                Log.d("Response: ", "> " + jsonStr);

                // Getting JSON Array node
                JElements = jsonObj.getJSONArray(Tags.TAG_ELEMENTS);
                Log.d("SIZE: ", "> " + Integer.toString(JElements.length()));

                // looping through All elements
                for (int i = 0; i < JElements.length(); i++) {
                    Log.d("SIZE: ", "> " + Integer.toString(JElements.length()));
                    JSONObject c = JElements.getJSONObject(i);

                    String type = c.getString(Tags.TAG_TYPE);
                    Log.d("TYPESTR: ", "> " + type);
                    if (type.equals("way")) {
                        JArray = c.getJSONArray(Tags.TAG_NODES);
                        ArrayList<Long> idList = new ArrayList<>();
                        for (int j = 0; j < JArray.length(); j++) {
                            idList.add(Long.parseLong(JArray.get(j).toString()));
                        }
                        long id = c.getLong(Tags.TAG_ID);

                        JSONObject jObjTags = c.getJSONObject(Tags.TAG_TAGS);
                        Map<String, String> tagMap = new HashMap<>();
                        try{
                        tagMap.put(Tags.TAG_TAGS_BUILDINGPART, jObjTags.getString(Tags.TAG_TAGS_BUILDINGPART));
                        }catch (JSONException e) {e.printStackTrace();}
                        try{
                        tagMap.put(Tags.TAG_TAGS_BUILDINGPART, jObjTags.getString(Tags.TAG_TAGS_HIGHWAY));
                        }catch (JSONException e) {e.printStackTrace();}

                        waysList.put(id, new Way(id, idList, tagMap));

                    } else if (type.equals("node")) {
                        long id = c.getLong(Tags.TAG_ID);
                        double lat = c.getDouble(Tags.TAG_LAT);
                        double lon = c.getDouble(Tags.TAG_LON);
                        Point point = new Point(lat, lon);

                        JSONObject jObjTags = c.getJSONObject(Tags.TAG_TAGS);
                        Map<String, String> tagMap = new HashMap<>();
                        try{
                        tagMap.put(Tags.TAG_TAGS_POI, jObjTags.getString(Tags.TAG_TAGS_POI));
                        }catch (JSONException e) {e.printStackTrace();}
                        try{
                        tagMap.put(Tags.TAG_TAGS_DOOR, jObjTags.getString(Tags.TAG_TAGS_DOOR));
                        }catch (JSONException e) {e.printStackTrace();}
                        nodesList.put(id, new Node(id, point, tagMap));
                    } else if (type.equals("relation")) {
                        long id = c.getLong(Tags.TAG_ID);
                        members = c.getJSONArray(Tags.TAG_MEMBERS);
                        for (int j = 0; j < members.length(); j++) {
                            JSONObject d = members.getJSONObject(j);
                            String memType = d.getString(Tags.TAG_TYPE);
                            long ref = d.getLong(Tags.TAG_REF);
                            String role = d.getString(Tags.TAG_ROLE);
                            membersList.add(new Members(memType, ref, role));
                        }
                        JSONObject jObjTags = c.getJSONObject(Tags.TAG_TAGS);
                        Map<String, String> tagMap = new HashMap<>();
                        try {
                            tagMap.put(Tags.TAG_TAGS_NAME, jObjTags.getString(Tags.TAG_TAGS_NAME));
                        } catch (JSONException e) {e.printStackTrace();}
                        try {
                            tagMap.put(Tags.TAG_TAGS_TYPE, jObjTags.getString(Tags.TAG_TAGS_TYPE));
                        }catch (JSONException e) {e.printStackTrace();}
                        //tagMap.put(Tags.TAG_TAGS_REF, jObjTags.getString(Tags.TAG_TAGS_REF));

                        //tagMap.put(Tags.TAG_TAGS_ROUTE, jObjTags.getString(Tags.TAG_TAGS_ROUTE));
                        relationsList.put(id, new Relation(id, membersList, tagMap));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Exception: ", "> " + e.toString());
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
    }
}