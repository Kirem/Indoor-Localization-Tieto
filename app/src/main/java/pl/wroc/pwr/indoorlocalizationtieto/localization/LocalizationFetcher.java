package pl.wroc.pwr.indoorlocalizationtieto.localization;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import pl.wroc.pwr.indoorlocalizationtieto.R;

/**
 * Class for fetching data about Access Points and Reference Points from JSON files
 * Created by Mateusz on 2015-05-10.
 */
public class LocalizationFetcher {
    JSONObject jsonObject = null;
    String jsonString;
    Context context;

    protected LocalizationFetcher(Context context) {
        this.context = context;
    }

    /**
     * Method for fetching data from resource raw JSON files
     */
    protected LocalizationData fetchData() {
        //get string from access_points.json file
        LocalizationData localizationData = new LocalizationData();
        InputStream inputStream = context.getResources().openRawResource(R.raw.access_points);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jsonString = writer.toString();

        //fetch AccessPoints
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("AccessPoints");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tempJsonObj = jsonArray.getJSONObject(i);
                String macAddr = tempJsonObj.getString("mac_address");
                double lat = tempJsonObj.getDouble("lat");
                double lon = tempJsonObj.getDouble("lon");
                int level = tempJsonObj.getInt("level");

                AccessPoint accessPoint = new AccessPoint(lat, lon, level, macAddr);
                localizationData.addAccessPoint(accessPoint);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //get string from refpoints.json
        inputStream = context.getResources().openRawResource(R.raw.refpoints); //delcared earlier
        writer = new StringWriter(); //declared earlier
        //char[] buffer = new char[1024]; //declared earlier
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jsonString = writer.toString();

        //fetch ReferencePoints
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("RefPoints");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tempJsonObj = jsonArray.getJSONObject(i);
                double lat = tempJsonObj.getDouble("lat");
                double lon = tempJsonObj.getDouble("lon");
                int level = tempJsonObj.getInt("level");
                boolean stairs = tempJsonObj.getBoolean("stairs_elevator");
                ReferencePoint referencePoint = new ReferencePoint(lat, lon, level, stairs);

                JSONArray tempJsonArray = tempJsonObj.getJSONArray("access_points");
                for (int j = 0; j < tempJsonArray.length(); j++) {
                    JSONObject tempJsonObj2 = tempJsonArray.getJSONObject(j);
                    String macAddress = tempJsonObj2.getString("mac_address");
                    int minRssi = tempJsonObj2.getInt("min_rssi");
                    int maxRssi = tempJsonObj2.getInt("max_rssi");
                    SubKey key = new SubKey(maxRssi, minRssi);
                    referencePoint.addRssi(macAddress, key);
                }
                localizationData.addReferencePoint(referencePoint);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return localizationData;
    }
}
