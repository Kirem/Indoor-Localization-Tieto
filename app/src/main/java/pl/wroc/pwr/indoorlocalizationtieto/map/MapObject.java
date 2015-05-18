package pl.wroc.pwr.indoorlocalizationtieto.map;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.util.ArrayMap;

import java.util.Map;
import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;

public abstract class MapObject {
    public static final String OBJECT_CLASS = "objectClass";
    public static final String OBJECT_TYPE = "objectType";
    private Geometry objectGeometry;
    private Map<String, String> options;
    private String name;
    private long id;

    MapObject(long n, Geometry geometry) {
        id = n;
        objectGeometry = geometry;
        options = new ArrayMap<>();
    }

    public Geometry getObjectGeometry() {
        return objectGeometry;
    }

    public Long getId() { return this.id; }

    abstract ArrayList<Geometry> getGeometries();

    public Map<String, String> getOptions() {
        return options;
    }

    public String getName() {
        return name;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public void setObjectGeometry(Geometry objectGeometry) {
        this.objectGeometry = objectGeometry;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return this.id == ((MapObject)o).id;
    }
}
