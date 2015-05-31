package pl.wroc.pwr.indoorlocalizationtieto.map;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;

public class Map {
    ArrayList<MapObject> objects;

    public Map() {
        objects = new ArrayList<>();
    }

    public void addObject(MapObject object) {
        objects.add(object);
    }

    public ArrayList<MapObject> getObjects() {
        return this.objects;
    }

    public ArrayList<Geometry> getGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        for (MapObject object : objects) {
            geometries.addAll(object.getGeometries());
        }
        return geometries;
    }

    /**
     * @param level
     * @return returns ArrayList of MapObject elements for demanded level.
     */
    public ArrayList<MapObject> getObjectsForLevel(float level) {
        ArrayList<MapObject> objectsForLevel = new ArrayList<>();
        for (MapObject mapObject : objects) {
            if (level == 0) {
                if ((mapObject instanceof Road)
                        || (mapObject instanceof Crossing)) {
                    objectsForLevel.add(mapObject);
                }
            }
            if (mapObject instanceof Building) {
                objectsForLevel.add(mapObject);
            }
            if (mapObject instanceof Level) {
                if (((Level) mapObject).getCurrentLevel() == level) {
                    objectsForLevel.addAll(((Level) mapObject).getMapObjects());
                }
            }
        }
        return objectsForLevel;
    }
}
