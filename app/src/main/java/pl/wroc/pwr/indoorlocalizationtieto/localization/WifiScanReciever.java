package pl.wroc.pwr.indoorlocalizationtieto.localization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mateusz on 2015-05-09.
 */
public class WifiScanReciever extends BroadcastReceiver {
    WifiManager wifiManager;
    List<ScanResult> scanResults;

    public WifiScanReciever(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    //invoke every
    @Override
    public void onReceive(Context context, Intent intent) {
        if(wifiManager.isWifiEnabled()) {
            wifiManager.startScan();
            scanResults = wifiManager.getScanResults();
            Toast.makeText(context, scanResults.get(0).BSSID.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public List<ScanResult> getScanResults() {
        return scanResults;
    }
}
