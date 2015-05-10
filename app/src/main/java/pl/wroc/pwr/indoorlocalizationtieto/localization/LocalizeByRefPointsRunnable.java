package pl.wroc.pwr.indoorlocalizationtieto.localization;

import android.net.wifi.ScanResult;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mateusz on 2015-05-13.
 */
public class LocalizeByRefPointsRunnable implements Runnable {
    private LocalizationData localizationData;
    private WifiScanReceiver wifiScanReceiver;
    private List<ScanResult> scanResults;

    private double prevLat, prevLon;
    private int prevLevel;

    public LocalizeByRefPointsRunnable(WifiScanReceiver wifiScanReceiver, LocalizationData localizationData, List<ScanResult> scanResults) {
        this.wifiScanReceiver = wifiScanReceiver;
        this.localizationData = localizationData;
        this.scanResults = scanResults;

        prevLat = wifiScanReceiver.getLocalization().getPosLat();
        prevLon = wifiScanReceiver.getLocalization().getPosLon();
        prevLevel = wifiScanReceiver.getLocalization().getPosLevel();
    }

    @Override
    public void run() {
        //for every known reference point
        ReferencePoint bestForNow = new ReferencePoint(prevLat, prevLon, prevLevel);
        int numOfMatch = 0;
        int previousMatches = 0;

        for(ReferencePoint referencePoint : localizationData.getReferencePoints()) {
            HashMap<String, SubKey> rssiOnRefPoint = referencePoint.getRssi();
            for(ScanResult scanResult : scanResults) {
                if(rssiOnRefPoint.containsKey(scanResult.BSSID)) {
                    int level = wifiScanReceiver.getWifiManager().calculateSignalLevel(scanResult.level, 20);
                    if (level <= rssiOnRefPoint.get(scanResult.BSSID).getRssiMax() + 1 &&
                            level >= rssiOnRefPoint.get(scanResult.BSSID).getRssiMax() - 1) {
                        numOfMatch++;
                    }
                }
            }
            if(numOfMatch > previousMatches) {
                bestForNow = referencePoint;
            }
            previousMatches = numOfMatch;
            numOfMatch = 0;
        }
        //update location
        wifiScanReceiver.getLocalization().updateLocation(
                bestForNow.getLocation().getX(),
                bestForNow.getLocation().getY(),
                bestForNow.getLevel()
        );
        //wifiScanReceiver.addReferencePointOccurences(bestForNow);
    }
}
