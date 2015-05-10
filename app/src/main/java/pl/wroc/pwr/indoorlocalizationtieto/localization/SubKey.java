package pl.wroc.pwr.indoorlocalizationtieto.localization;

/**
 * Created by Mateusz on 2015-05-12.
 */
public class SubKey {
    private int rssiMin;
    private int rssiMax;

    public SubKey(int rssiMax, int rssiMin) {
        this.rssiMax = rssiMax;
        this.rssiMin = rssiMin;
    }

    public int getRssiMax() {
        return rssiMax;
    }

    public int getRssiMin() {
        return rssiMin;
    }
}