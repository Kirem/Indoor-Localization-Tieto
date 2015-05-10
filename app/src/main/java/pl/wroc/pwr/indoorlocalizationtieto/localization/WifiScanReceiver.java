package pl.wroc.pwr.indoorlocalizationtieto.localization;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.R;

/**
 * Created by Mateusz on 2015-05-09.
 */
public class WifiScanReceiver extends BroadcastReceiver {
    private WifiManager wifiManager;
    private HashMap<ReferencePoint, Integer> referencePointOccurences;
    private List<ScanResult> scanResults;
    private LocalizationData localizationData;
    private Context context;
    private Localization localization;
    private int scanCounter;

    /*
    testowe text fieldy
     */
//    TextView textViewLat;
//    TextView textViewLon;
//    TextView textViewCounter;

    public WifiScanReceiver(WifiManager wifiManager, Context context, Localization localization) {
        this.wifiManager = wifiManager;
        this.context = context;
        this.localization = localization;
        LocalizationFetcher localizationFetcher = new LocalizationFetcher(this.context);
        localizationData = localizationFetcher.fetchData();
        referencePointOccurences = new HashMap<>();
        scanCounter = 0;

//        textViewLat = (TextView) ((Activity)context).findViewById(R.id.textView);
//        textViewLon = (TextView) ((Activity)context).findViewById(R.id.textView2);
//        textViewCounter = (TextView) ((Activity)context).findViewById(R.id.textView3);
    }

    //invoke every
    @Override
    public void onReceive(Context context, Intent intent) {
        if(wifiManager.isWifiEnabled()) {
            wifiManager.startScan();
            scanResults = wifiManager.getScanResults();
            Thread thread = new Thread(new LocalizeByRefPointsRunnable(this, localizationData, scanResults));
            thread.start();
            wifiManager.startScan();

//            textViewLat.setText("Lat: " + localization.getPosLat());
//            textViewLon.setText("Lon: " + localization.getPosLon());
//            textViewCounter.setText("level: " + localization.getPosLevel());
            wifiManager.startScan();
        }
    }

    public List<ScanResult> getScanResults() {
        return scanResults;
    }

    public void updateLocation(ReferencePoint referencePoint) {
        localization.updateLocation(
                referencePoint.getLocation().getX(),
                referencePoint.getLocation().getY(),
                referencePoint.getLevel()
        );
    }

    protected Localization getLocalization() {
        return localization;
    }

    protected void addReferencePointOccurences(ReferencePoint referencePoint) {
        if(referencePointOccurences.containsKey(referencePoint)) {
            int temp = referencePointOccurences.get(referencePoint);
            referencePointOccurences.put(referencePoint, temp +1);
        } else
            referencePointOccurences.put(referencePoint, 1);
    }

    protected WifiManager getWifiManager() {
        return wifiManager;
    }
}
