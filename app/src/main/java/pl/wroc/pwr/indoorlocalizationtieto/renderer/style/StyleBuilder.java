package pl.wroc.pwr.indoorlocalizationtieto.renderer.style;

import android.content.Context;
import java.util.List;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;

public class StyleBuilder {
    private static StyleParser parser = null;

    public static List<MapObjectStyle> build(MapObject object, Context context, int id){
        if(parser == null) {
            String styleJson = StyleLoader.ReadStringFromResource(context, id);
            parser = new StyleParser(styleJson);
        }
        List<MapObjectStyle> objectStyles = parser.getStyles(object.getOptions());
        return objectStyles;
    }
}
