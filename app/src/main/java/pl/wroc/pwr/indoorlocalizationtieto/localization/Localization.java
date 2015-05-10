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
    private double posLevel;


    private WifiManager wifiManager;
    private Context context;
    private WifiScanReciever wifiScanReciever;

    public Localization(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        wifiScanReciever = new WifiScanReciever(wifiManager);
        registerReciever();
    }

    public void registerReciever() {
        context.registerReceiver(wifiScanReciever,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void unregisterReciever() {
        context.unregisterReceiver(wifiScanReciever);
    }

    public void startScan() {
        if(wifiManager.isWifiEnabled()) {
            wifiManager.startScan();
        }
    }

    public List<ScanResult> getWifis() {
        return wifiScanReciever.getScanResults();
    }
}
