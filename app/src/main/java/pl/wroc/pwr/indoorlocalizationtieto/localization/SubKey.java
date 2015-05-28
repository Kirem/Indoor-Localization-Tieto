package pl.wroc.pwr.indoorlocalizationtieto.localization;

/**
 * Created by Mateusz on 2015-05-12.
 * Class for storing min and max value of RSSI level
 */
public class SubKey {
    private int rssiMin;
    private int rssiMax;

    protected SubKey(int rssiMax, int rssiMin) {
        this.rssiMax = rssiMax;
        this.rssiMin = rssiMin;
    }

    protected int getRssiMax() {
        return rssiMax;
    }

    protected int getRssiMin() {
        return rssiMin;
    }
}