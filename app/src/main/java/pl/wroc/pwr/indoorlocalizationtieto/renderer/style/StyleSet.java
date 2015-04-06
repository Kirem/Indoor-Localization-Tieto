package pl.wroc.pwr.indoorlocalizationtieto.renderer.style;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StyleSet {
    Map<StyleMatcher, List<MapObjectStyle>> styleMap;

    public StyleSet(Map<StyleMatcher, List<MapObjectStyle>> styleMap) {
        this.styleMap = new HashMap<>(styleMap);
    }

    public List<MapObjectStyle> getStyleList(StyleMatcher matcher){
        return styleMap.get(matcher);
    }
}
