package pl.wroc.pwr.indoorlocalizationtieto.localization;

import android.content.Context;
import android.net.wifi.ScanResult;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mateusz on 2015-05-13.
 */
public class LocalizeByRefPointsThread implements Runnable {
    private LocalizationData localizationData;
    private WifiScanReciever wifiScanReciever;
    private List<ScanResult> scanResults;

    private double prevLat, prevLon;
    private int prevLevel;

    public LocalizeByRefPointsThread(WifiScanReciever wifiScanReciever, LocalizationData localizationData, List<ScanResult> scanResults) {
        this.wifiScanReciever = wifiScanReciever;
        this.localizationData = localizationData;
        this.scanResults = scanResults;

        prevLat = wifiScanReciever.getLocalization().getPosLat();
        prevLon = wifiScanReciever.getLocalization().getPosLon();
        prevLevel = wifiScanReciever.getLocalization().getPosLevel();
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
                    if (scanResult.level < rssiOnRefPoint.get(scanResult.BSSID).getRssiMax() &&
                            scanResult.level > rssiOnRefPoint.get(scanResult.BSSID).getRssiMin()) {
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
        wifiScanReciever.getLocalization().updateLocation(bestForNow.getLocation().getX(), bestForNow.getLocation().getY(), bestForNow.getLevel());
    }
}
