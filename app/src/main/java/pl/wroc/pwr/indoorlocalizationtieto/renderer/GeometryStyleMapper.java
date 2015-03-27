package pl.wroc.pwr.indoorlocalizationtieto.renderer;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.MapObjectStyle;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.StyleLoader;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.style.StyleParser;

public class GeometryStyleMapper {
    private ArrayList<Pair<Geometry, MapObjectStyle>> mapper;
    private ArrayList<MapObject> objects;
    private int position = 0;
    private StylesLoadedListener listener;
    private Context context;

    public GeometryStyleMapper(ArrayList<MapObject> mapObjects, StylesLoadedListener listener, Context context) {
        objects = new ArrayList<>(mapObjects);
        this.listener = listener;
        this.context = context;

    }


    public void setStyle(int id){
        loadStyleSheet(id);

    }

    public void moveToFirst(){
        position = 0;
    }

    public boolean isAfterLast(){
        return mapper.size() <= position;
    }

    public Pair<Geometry, MapObjectStyle> next(){
        Pair<Geometry, MapObjectStyle> pair = mapper.get(position);
        position+=1;
        return pair;
    }


    private void loadStyleSheet(int id) {
        String styleJson = StyleLoader.ReadStringFromResource(context, id);
        StyleParser parser = new StyleParser(styleJson);
        mapper = new ArrayList<>();
        for (MapObject object : objects) {
            Geometry objectGeometry = object.getObjectGeometry();
            MapObjectStyle style = parser.getStyle(object.getOptions());
//            Log.d(getClass().getSimpleName(), "loadStyleSheet, color: " + style.getBackgroundColor());
            Pair<Geometry, MapObjectStyle> map = new Pair<>(objectGeometry, style);
            mapper.add(map);
        }
    }

    interface StylesLoadedListener{
        public void onStyleLoaded();
    }
}
