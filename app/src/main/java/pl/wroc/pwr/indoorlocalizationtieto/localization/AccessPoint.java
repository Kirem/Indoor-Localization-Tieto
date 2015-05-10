package pl.wroc.pwr.indoorlocalizationtieto.localization;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

/**
 * Created by Mateusz on 2015-05-09.
 */
public class AccessPoint {
    private Point location;
    private int level;
    private String macAddress;

    public AccessPoint(double lat, double lon, int level, String macAddress) {
        this.location = new Point(lat, lon);
        this.level = level;
        this.macAddress = macAddress;
    }

    public Point getLocation() {
        return location;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public int getLevel() {
        return level;
    }
}
