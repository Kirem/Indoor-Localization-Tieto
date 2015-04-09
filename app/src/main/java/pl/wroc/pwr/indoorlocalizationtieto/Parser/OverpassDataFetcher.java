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

public class OverpassDataFetcher {
    // URL with api service
    private String url;

    public OverpassDataFetcher() {
        url = "http://www.overpass-api.de/api/interpreter?data=";
    }

    public OverpassDataFetcher(String serviceUrl) {
        this.url = serviceUrl;
    }

    private Map<String, String> buildMap(JSONObject jObj, String name, String tags[]) {
        JSONObject jObjTags = null;
        Map<String, String> tagMap = new HashMap<>();
        try {
            jObjTags = jObj.getJSONObject(name);
        } catch (JSONException e) {
            return tagMap;
        }
        for (String tag : tags) {
            String val = jObjTags.optString(tag);
            if (!(val.isEmpty())) {
                tagMap.put(tag, val);
            }
        }
        return tagMap;
    }

    public OSMData fetchDataWithRadius(double latitude, double longitude, double radius) {
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
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JElements = jsonObj.getJSONArray(Tags.ELEMENTS);
                // looping through All elements
                for (int i = 0; i < JElements.length(); i++) {
                    JSONObject tempJSONOBject = JElements.getJSONObject(i);
                    String type = tempJSONOBject.getString(Tags.TYPE);
                    switch (type) {
                        case "node":
                            long nodesId = tempJSONOBject.getLong(Tags.ID);
                            double lat = tempJSONOBject.getDouble(Tags.LAT);
                            double lon = tempJSONOBject.getDouble(Tags.LON);

                            Map<String, String> nodesTagMap = buildMap(tempJSONOBject, Tags.TAGS, Tags.NODES_TAGS);
                            osmData.nodesList.put(nodesId, new Node(nodesId, lat, lon, nodesTagMap));
                            break;
                        case "way":
                            JSONArray nodesJArray = tempJSONOBject.getJSONArray(Tags.NODES);
                            ArrayList<Long> nodesList = new ArrayList<>();
                            for (int j = 0; j < nodesJArray.length(); j++) {
                                nodesList.add(Long.parseLong(nodesJArray.get(j).toString()));
                            }
                            long waysId = tempJSONOBject.getLong(Tags.ID);

                            Map<String, String> waysTagMap = buildMap(tempJSONOBject, Tags.TAGS, Tags.WAYS_TAGS);
                            osmData.waysList.put(waysId, new Way(waysId, nodesList, waysTagMap));
                            break;
                        case "relation":
                            JSONArray membersJArray = tempJSONOBject.getJSONArray(Tags.MEMBERS);
                            ArrayList<Member> membersList = new ArrayList<>();
                            long relationsId = tempJSONOBject.getLong(Tags.ID);
                            for (int j = 0; j < membersJArray.length(); j++) {
                                JSONObject tempMember = membersJArray.getJSONObject(j);
                                String tempMemberType = tempMember.getString(Tags.TYPE);
                                long tempMemberRef = tempMember.getLong(Tags.REF);
                                //TODO SPRAWDZIC CO SIE DZIEJE JEZELI BEDZIE PUSTE
                                String tempMemberRole = tempMember.optString(Tags.ROLE);
                                membersList.add(new Member(tempMemberType, tempMemberRef, tempMemberRole));
                            }
                            Map<String, String> tagMap = buildMap(tempJSONOBject, Tags.TAGS, Tags.RELATIONS_TAGS);
                            osmData.relationsList.put(relationsId, new Relation(relationsId, membersList, tagMap));
                            break;
                        default:
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Exception: ", "> " + e.toString());
            }
        }
        return osmData;
    }
}