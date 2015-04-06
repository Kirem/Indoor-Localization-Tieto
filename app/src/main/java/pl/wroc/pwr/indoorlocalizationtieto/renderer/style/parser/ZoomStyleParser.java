package pl.wroc.pwr.indoorlocalizationtieto.renderer.style.parser;

import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.MapObjectStyle;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.StyleMatcher;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.StyleSet;

public class ZoomStyleParser implements StyleParser {
    private static final String STYLE_PARSER_TAG = "STYLE_PARSER_TAG";
    private static final String COUNT_TAG = "cnt";
    private static final String ZOOM_COUNT_TAG = "zoom_cnt";
    private static final String ZOOM_LEVEL_TAG = "zoom_level";
    private static final String ZOOM_TAG = "zoom";
    private static final String CLASS_COUNT_TAG = "class_cnt";
    private static final String CLASS_ARRAY_TAG = "class_array";
    private static final String CLASS_TAG = "class";
    private static final String TYPE_TAG = "type";
    private static final String TYPES_TAG = "types";
    private static final String STYLE_TAG = "style";

    Map<StyleMatcher, List<MapObjectStyle>> styleMap;


    public ZoomStyleParser() {
    }

    public StyleSet getStyleSet(String zoomJson) {
        styleMap = new HashMap<>();
        JSONObject jsonMain = null;
        try {
            jsonMain = new JSONObject(zoomJson);

            int zoomCount = jsonMain.getInt(ZOOM_COUNT_TAG);
            JSONArray zoomArray = jsonMain.getJSONArray(ZOOM_TAG);

            for (int i = 0; i < zoomCount; i++) {
                JSONObject zoomObject = zoomArray.getJSONObject(i);
                int zoomLevel = zoomObject.getInt(ZOOM_LEVEL_TAG);
                int classCount = zoomObject.getInt(CLASS_COUNT_TAG);
                JSONArray classArray = zoomObject.getJSONArray(CLASS_ARRAY_TAG);

                for(int j = 0; j < classCount; j++){
                    JSONObject classObject = classArray.getJSONObject(j);
                    String className = classObject.getString(CLASS_TAG);
                    int count = classObject.getInt(COUNT_TAG);
                    JSONArray typeArray = classObject.getJSONArray(TYPES_TAG);

                    for(int k = 0; k < count; k++){
                        JSONObject typeObject = typeArray.getJSONObject(k);
                        String typeName = typeObject.getString(TYPE_TAG);
                        JSONObject style = typeObject.getJSONObject(STYLE_TAG);
                        StyleMatcher matcher = new StyleMatcher(zoomLevel, className, typeName);
                        ArrayList<MapObjectStyle> mapObjectStyles = parseStyle(style);
                        styleMap.put(matcher, mapObjectStyles);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new StyleSet(styleMap);
    }


    private ArrayList<MapObjectStyle> parseStyle(JSONObject style) {
        String backgroundColor = null;
        String borderColor = null;
        int width = -1;
        int drawn = -1;
        try {
            backgroundColor = style.getString("background_color");
        } catch (JSONException ignored) {
        }
        try {
            borderColor = style.getString("border_color");
        } catch (JSONException ignored) {
        }
        try {
            width = style.getInt("width");
        } catch (JSONException ignored) {
        }
        try {
            drawn = style.getInt("draw");
        } catch (JSONException ignored) {
        }

        ArrayList<MapObjectStyle> styles = new ArrayList<>();
        if(drawn < 1){
            return styles;
        }

        if (backgroundColor != null) {
            MapObjectStyle objectStyle = new MapObjectStyle();
            objectStyle.setBackgroundColor(Color.parseColor(backgroundColor));
            if (width != -1) {
                objectStyle.setLineWidth(width);
            }

            styles.add(objectStyle);
        }

        if (borderColor != null) {
            MapObjectStyle objectStyle = new MapObjectStyle();
            objectStyle.setBorderColor(Color.parseColor(borderColor));
            if (width != -1) {
                objectStyle.setLineWidth(width);
            }



            styles.add(objectStyle);
        }
        return styles;
    }
}

