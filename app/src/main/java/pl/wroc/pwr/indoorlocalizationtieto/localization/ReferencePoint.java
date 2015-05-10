package pl.wroc.pwr.indoorlocalizationtieto.localization;

import java.util.HashMap;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

/**
 * Created by Mateusz on 2015-05-09.
 */
public class ReferencePoint {

    private Point location;
    private int level;
    private HashMap<String, SubKey> rssi;

    public ReferencePoint(double lat, double lon, int level) {
        this.location = new Point(lat, lon);
        this.level = level;
        rssi = new HashMap<>();
    }

    public void setRssi(HashMap<String, SubKey> map) {
        this.rssi.putAll(map);
    }

    public void addRssi(String macAddr, SubKey key) {
        this.rssi.put(macAddr, key);
    }

    public HashMap<String, SubKey> getRssi() {
        return rssi;
    }

    public Point getLocation() {
        return location;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReferencePoint that = (ReferencePoint) o;

        if (level != that.level) return false;
        if (!location.equals(that.location)) return false;
        return !(rssi != null ? !rssi.equals(that.rssi) : that.rssi != null);

    }

    @Override
    public int hashCode() {
        int result = location.hashCode();
        result = 31 * result + level;
        result = 31 * result + (rssi != null ? rssi.hashCode() : 0);
        return result;
    }
}
