package pl.wroc.pwr.indoorlocalizationtieto.renderer.style;

import android.graphics.Color;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class StyleParser {
    StyleParser() {
    public List<MapObjectStyle> getStyles(Map<String, String> options) {
        int width = -1;
        List<MapObjectStyle> styles = new ArrayList<>();
        if (backgroundColor != null) {
            MapObjectStyle style = new MapObjectStyle();
            styles.add(style);
            if(borderColor != null){
                style = new MapObjectStyle();
                style.setBorderColor(Color.parseColor(borderColor));
                styles.add(style);
            }
            if(width != -1){
                style.setLineWidth(width);
            }
        }
        return styles;
    }
}
