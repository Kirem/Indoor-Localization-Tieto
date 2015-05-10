package pl.wroc.pwr.indoorlocalizationtieto.localization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mateusz on 2015-05-09.
 */
public class WifiScanReciever extends BroadcastReceiver {
    WifiManager wifiManager;
    List<ScanResult> scanResults;
    private LocalizationData localizationData;
    private Context context;
    private Localization localization;

    public WifiScanReciever(WifiManager wifiManager, Context context, Localization localization) {
        this.wifiManager = wifiManager;
        this.context = context;
        this.localization = localization;
        LocalizationFetcher localizationFetcher = new LocalizationFetcher(this.context);
        localizationData = localizationFetcher.fetchData();
    }

    //invoke every
    @Override
    public void onReceive(Context context, Intent intent) {
        if(wifiManager.isWifiEnabled()) {
            wifiManager.startScan();
            scanResults = wifiManager.getScanResults();
            //Toast.makeText(context, scanResults.get(0).BSSID.toString(), Toast.LENGTH_SHORT).show();
            Thread thread = new Thread(new LocalizeByRefPointsThread(this, localizationData, scanResults));
            thread.start();
            Toast.makeText(context, localization.getPosLat() + "  " + localization.getPosLon(), Toast.LENGTH_SHORT).show();
        }
    }

    public List<ScanResult> getScanResults() {
        return scanResults;
    }

    public void updateLocation(ReferencePoint referencePoint) {

    }

    protected Localization getLocalization() {
        return localization;
    }
}
