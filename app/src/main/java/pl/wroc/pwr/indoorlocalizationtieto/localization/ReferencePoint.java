package pl.wroc.pwr.indoorlocalizationtieto.localization;

import java.util.HashMap;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

/**
 * Created by Mateusz on 2015-05-09.
 * Class describing Reference Point for localization module
 */
public class ReferencePoint {
    //location variables
    private Point location;
    private int level;

    //map of visible access points with signal power
    private HashMap<String, SubKey> rssi;

    //Boolean value, if reference point is near stairs or elevator
    //used for determining floor change
    private boolean stairsOrElevator;

    /**
     * Constructor
     * @param lat - latitude
     * @param lon - longitude
     * @param level - floor number
     * @param stairsOrElevator - boolean value if Reference Point is near stairs or elevator
     */
    protected ReferencePoint(double lat, double lon, int level, boolean stairsOrElevator) {
        this.location = new Point(lat, lon);
        this.level = level;
        this.stairsOrElevator = stairsOrElevator;
        rssi = new HashMap<>();
    }

    /**
     * @return if Reference Point is near stairs or elevator
     */
    protected boolean getStairsOrElevator() {
        return this.stairsOrElevator;
    }

    /**
     * Set map of visible access points
     * @param map - HashMap of visible access points
     */
    public void setRssi(HashMap<String, SubKey> map) {
        this.rssi.putAll(map);
    }

    /**
     * Add an access point to rssi map
     * @param macAddr - mac address of access point
     * @param key - SubKey containing max and min power level of RSSI signal
     */
    public void addRssi(String macAddr, SubKey key) {
        this.rssi.put(macAddr, key);
    }

    /**
     * @return map of visible accesspoints
     */
    public HashMap<String, SubKey> getRssi() {
        return rssi;
    }

    /**
     * @return latitude and longitude (Point)
     */
    public Point getLocation() {
        return location;
    }

    /**
     * @return floor number
     */
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
