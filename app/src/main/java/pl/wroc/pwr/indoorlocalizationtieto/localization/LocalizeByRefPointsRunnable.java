package pl.wroc.pwr.indoorlocalizationtieto.localization;

import android.net.wifi.ScanResult;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mateusz on 2015-05-13.
 * Runnable Class for determining user position depending on Reference Points
 */
public class LocalizeByRefPointsRunnable implements Runnable {
    private LocalizationData localizationData;
    private WifiScanReceiver wifiScanReceiver;
    private List<ScanResult> scanResults;

    //helper variables about last known postion
    private double prevLat, prevLon;
    private int prevLevel;

    /**
     * Constructor
     * @param wifiScanReceiver - instance of WifiScanReceiver class
     * @param localizationData - instance of LocalizationData class
     * @param scanResults - list of ScanResult's
     */
    protected LocalizeByRefPointsRunnable(WifiScanReceiver wifiScanReceiver, LocalizationData localizationData, List<ScanResult> scanResults) {
        this.wifiScanReceiver = wifiScanReceiver;
        this.localizationData = localizationData;
        this.scanResults = scanResults;

        this.prevLat = wifiScanReceiver.getLocalization().getPosLat();
        this.prevLon = wifiScanReceiver.getLocalization().getPosLon();
        this.prevLevel = wifiScanReceiver.getLocalization().getPosLevel();
    }

    @Override
    public void run() {
        //for every known reference point
        ReferencePoint bestForNow = new ReferencePoint(prevLat, prevLon, prevLevel, false);
        int numOfMatch = 0;
        float previousMatches = 0;

        if (!wifiScanReceiver.isChangingLevel()) {
            for (ReferencePoint referencePoint : localizationData.getReferencePoints()) {
                //check only ref points for certain floor
                if (referencePoint.getLevel() == this.prevLevel) {
                    HashMap<String, SubKey> rssiOnRefPoint = referencePoint.getRssi();
                    for (ScanResult scanResult : scanResults) {
                        if (rssiOnRefPoint.containsKey(scanResult.BSSID)) {
                            int signalLevel = scanResult.level;
                            if (signalLevel <= rssiOnRefPoint.get(scanResult.BSSID).getRssiMax() &&
                                    signalLevel >= rssiOnRefPoint.get(scanResult.BSSID).getRssiMin()) {
                                numOfMatch++;
                            }
                        }
                    }
                    float percentage = ((float) numOfMatch / (float) referencePoint.getRssi().size()) * 100f;
                    if (percentage > previousMatches) {
                        bestForNow = referencePoint;
                    }
                    previousMatches = percentage;
                    numOfMatch = 0;
                }
            }
            wifiScanReceiver.getLocalization().updateLocation(
                    bestForNow.getLocation().getX(),
                    bestForNow.getLocation().getY(),
                    //bestForNow.getLevel()
                    this.prevLevel
            );
        }

        if (bestForNow.getStairsOrElevator()) {
            wifiScanReceiver.setChangingLevel(true);
        }
    }
}
