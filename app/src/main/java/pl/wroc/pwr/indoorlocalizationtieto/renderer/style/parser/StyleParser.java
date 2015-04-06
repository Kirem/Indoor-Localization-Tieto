package pl.wroc.pwr.indoorlocalizationtieto.renderer.style.parser;


import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.StyleSet;

public interface StyleParser {
    StyleSet getStyleSet(String zoomJson);

}
