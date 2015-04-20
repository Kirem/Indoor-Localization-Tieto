package pl.wroc.pwr.indoorlocalizationtieto.map;


import android.support.v4.util.ArrayMap;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;

public abstract class MapObject {
    public static final String OBJECT_CLASS = "objectClass";
    public static final String OBJECT_TYPE = "objectType";
    private Geometry objectGeometry;
    private ArrayMap<String, String> options;
    private String name;
    private long id;
    MapObject(long n, Geometry geometry) {
        id = n;
        objectGeometry = geometry;
    }

    public Geometry getObjectGeometry() {
        return objectGeometry;
    }

    public Long getId() { return this.id; }

    abstract ArrayList<Geometry> getGeometries();

    public ArrayMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(ArrayMap<String, String> options) {
        this.options = options;
    }

    public void setObjectGeometry(Geometry objectGeometry) {
        this.objectGeometry = objectGeometry;
    }

    @Override
    public boolean equals(Object o) {
        return this.id == ((MapObject)o).id;
    }
}
