package pl.wroc.pwr.indoorlocalizationtieto.localization;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.TextView;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.R;

/**
 * Created by Mateusz on 2015-05-09.
 * Scan receiver for capturing data about access points and signal level
 */
public class WifiScanReceiver extends BroadcastReceiver {
    private WifiManager wifiManager;
    private LocalizationData localizationData;
    private Context context;
    private Localization localization;

    private List<ScanResult> scanResults;

    private boolean changingLevel;
    private boolean alertBuilt;

    /*
    testowe text fieldy
     */
    /*private TextView textViewLat;
    private TextView textViewLon;
    private TextView textViewCounter;
    private TextView textViewLevelChanging;
    private TextView textViewTicks;*/

    private boolean tick; //helper line

    /**
     * Constructor for WifiScanReceiver
     * @param wifiManager - WifiManager object
     * @param context - application context
     * @param localization - instance of Localization class
     */
    protected WifiScanReceiver(WifiManager wifiManager, Context context, Localization localization) {
        this.wifiManager = wifiManager;
        this.context = context;
        this.localization = localization;

        LocalizationFetcher localizationFetcher = new LocalizationFetcher(this.context);
        this.localizationData = localizationFetcher.fetchData();

        this.alertBuilt = false;
        this.changingLevel = false;
        this.tick = false;

   /*     this.textViewLat = (TextView) ((Activity) context).findViewById(R.id.textView);
        this.textViewLon = (TextView) ((Activity) context).findViewById(R.id.textView2);
        this.textViewCounter = (TextView) ((Activity) context).findViewById(R.id.textView3);
        this.textViewLevelChanging = (TextView) ((Activity) context).findViewById(R.id.textView4);
        this.textViewTicks = (TextView) ((Activity) context).findViewById(R.id.textView5);*/
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.startScan();
            if (changingLevel && !alertBuilt) {
                alertBuilt = true;
                changingLevel = false;

                //run alert dialog asking if changing floor
                AlertDialog.Builder builderYesNo = new AlertDialog.Builder(context);

                builderYesNo.setTitle("Change floor");
                builderYesNo.setMessage("Do you want to change floor? Current = " + localization.getPosLevel());

                builderYesNo.setPositiveButton("Yes, +1", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        changeLevel(localization.getPosLevel() + 1);
                        //changingLevel = false;
                        dialog.dismiss();
                        runThreadSleep();
                    }

                });

                builderYesNo.setNeutralButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //changingLevel = false;
                        dialog.dismiss();
                        runThreadSleep();
                    }
                });

                builderYesNo.setNegativeButton("Yes, -1", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeLevel(localization.getPosLevel() - 1);
                        //changingLevel = false;
                        dialog.dismiss();
                        runThreadSleep();
                    }
                });

                AlertDialog alert = builderYesNo.create();
                alert.show();
            } else {
                scanResults = wifiManager.getScanResults();
                Thread thread = new Thread(new LocalizeByRefPointsRunnable(this, localizationData, scanResults));
                thread.start();
                wifiManager.startScan();
                // tick = !tick;
            }
  /*          textViewLat.setText("Lat: " + localization.getPosLat());
            textViewLon.setText("Lon: " + localization.getPosLon());
            textViewCounter.setText("level: " + localization.getPosLevel());
            textViewLevelChanging.setText("Changing: " + isChangingLevel());
            textViewTicks.setText("Tick: " + !tick);*/
            wifiManager.startScan();
        }
    }

    protected List<ScanResult> getScanResults() {
        return scanResults;
    }

    synchronized protected void changeLevel(int level) {
        setChangingLevel(false);
        localization.setPosLevel(level);
    }

    protected Localization getLocalization() {
        return localization;
    }

    protected boolean isChangingLevel() {
        return changingLevel;
    }

    protected synchronized void setChangingLevel(boolean changingLevel) {
        this.changingLevel = changingLevel;
    }

    protected Context getContext() {
        return context;
    }

    private void runThreadSleep() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10000, 0);
                    alertBuilt = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
