package pl.wroc.pwr.indoorlocalizationtieto.localization;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

/**
 * Created by Mateusz on 2015-05-09.
 */
public class AccessPoint {
    private Point location;
    private String macAddress;

    public AccessPoint(long lat, long lon, String macAddress) {
        this.location = new Point(lat, lon);
        this.macAddress = macAddress;
    }
}
