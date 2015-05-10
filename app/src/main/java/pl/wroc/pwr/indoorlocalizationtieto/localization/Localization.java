package pl.wroc.pwr.indoorlocalizationtieto.localization;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

public class Localization {
    //location of the user
    private double posLat;
    private double posLon;
    private int posLevel;


    private WifiManager wifiManager;
    private Context context;
    private WifiScanReceiver wifiScanReceiver;

    public Localization(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        wifiScanReceiver = new WifiScanReceiver(wifiManager, context, this);
        registerReceiver();
    }

    public void registerReceiver() {
        context.registerReceiver(wifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void unregisterReceiver() {
        context.unregisterReceiver(wifiScanReceiver);
    }

    public void startScan() {
        if(wifiManager.isWifiEnabled()) {
            wifiManager.startScan();
        }
    }

    public List<ScanResult> getWifis() {
        return wifiScanReceiver.getScanResults();
    }

    synchronized public void updateLocation(double lat, double lon, int level) {
        posLat = lat;
        posLon = lon;
        posLevel = level;
    }

    public double getPosLat() {
        return posLat;
    }

    public double getPosLon() {
        return posLon;
    }

    public int getPosLevel() {
        return posLevel;
    }
}
