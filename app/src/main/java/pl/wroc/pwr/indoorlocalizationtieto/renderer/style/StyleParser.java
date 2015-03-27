package pl.wroc.pwr.indoorlocalizationtieto.renderer.style;

import android.graphics.Color;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
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

    public List<MapObjectStyle> getStyles(Map<String, String> options) {
        int width = -1;
        String backgroundColor = null;
        String borderColor = null;
        List<MapObjectStyle> styles = new ArrayList<>();

        String objectClass = options.get(MapObject.OBJECT_CLASS);
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
        if (backgroundColor != null) {
            MapObjectStyle style = new MapObjectStyle();
            style.setBackgroundColor(Color.parseColor(backgroundColor));
            if(width != -1){
                style.setLineWidth(width);
            }
            styles.add(style);
            style = new MapObjectStyle();
            if(borderColor != null){
                style.setBorderColor(Color.parseColor(borderColor));
                styles.add(style);
            }
        }
        return styles;
    }
}
