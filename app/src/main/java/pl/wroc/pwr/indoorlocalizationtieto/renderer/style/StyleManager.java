package pl.wroc.pwr.indoorlocalizationtieto.renderer.style;

import android.content.Context;

import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.parser.StyleParser;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.parser.ZoomStyleParser;

public class StyleManager {
    private StyleSet styles = null;
    private Context context;
    public StyleManager(Context context) {
        this.context = context;
    }

    public void loadStyle(int styleId) {
        String style = StyleLoader.ReadStringFromResource(context, styleId);
        StyleParser zoomParser = new ZoomStyleParser();
        styles = zoomParser.getStyleSet(style);
    }

    public List<MapObjectStyle> getStyleSetForData(int zoomLevel, String objectClass, String objectType) {
        return styles.getStyleList(new StyleMatcher(zoomLevel, objectClass, objectType));
    }


}
