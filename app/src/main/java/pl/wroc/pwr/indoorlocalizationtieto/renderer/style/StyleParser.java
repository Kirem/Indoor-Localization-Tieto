package pl.wroc.pwr.indoorlocalizationtieto.renderer.style;

import android.graphics.Color;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;

public class StyleParser {
    private static final String STYLE_PARSER_TAG = "STYLE PARSER STYLE";
    JSONObject jsonObject;

    public StyleParser(String styleJson) {
        try {
            jsonObject = new JSONObject(styleJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MapObjectStyle getStyle(Map<String, String> options) {
        if (jsonObject == null) {
            return null;
        }
        String objectClass = options.get(MapObject.OBJECT_CLASS);

        String backgroundColor = null;
        String borderColor = null;
        Integer width = -1;
        try {
            backgroundColor = jsonObject.getJSONObject(objectClass).
                    getJSONObject(options.get(MapObject.OBJECT_TYPE)).getString("background_color");
        } catch (JSONException e) {
            Log.e(STYLE_PARSER_TAG, e.toString());
        }
        try {
            borderColor = jsonObject.getJSONObject(objectClass).
                    getJSONObject(options.get(MapObject.OBJECT_TYPE)).getString("border_color");
        } catch (JSONException e) {
            Log.e(STYLE_PARSER_TAG, e.toString());
        }

        try {
            width = jsonObject.getJSONObject(objectClass).
                    getJSONObject(options.get(MapObject.OBJECT_TYPE)).getInt("width");
        } catch (JSONException e) {
            Log.e(STYLE_PARSER_TAG, e.toString());
        }
//                Log.d(STYLE_PARSER_TAG, "borderColor: " + backgroundColor);
//                Log.d(STYLE_PARSER_TAG, "backgroundColor: " + Color.parseColor(backgroundColor));

        MapObjectStyle style = new MapObjectStyle();
        if (backgroundColor != null)
            style.setBackgroundColor(Color.parseColor(backgroundColor));

        if (borderColor != null)
            style.setBorderColor(Color.parseColor(borderColor));

        if (width != -1)
            style.setLineWidth(width);


        return style;

    }
}
