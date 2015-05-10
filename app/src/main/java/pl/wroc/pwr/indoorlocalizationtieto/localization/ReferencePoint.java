package pl.wroc.pwr.indoorlocalizationtieto.localization;

import java.util.HashMap;

import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;

/**
 * Created by Mateusz on 2015-05-09.
 */
public class ReferencePoint {
    private Point location;
    private HashMap<String, Integer> rssi;

    public ReferencePoint(long lat, long lon) {
        this.location = new Point(lat, lon);
        rssi = new HashMap<>();
    }

    public void setRssi(HashMap<String, Integer> map) {
        this.rssi.putAll(map);
    }

    public void addRssi(String macAddr, Integer rssi) {
        this.rssi.put(macAddr, rssi);
    }

    public HashMap<String, Integer> getRssi() {
        return rssi;
    }
}
