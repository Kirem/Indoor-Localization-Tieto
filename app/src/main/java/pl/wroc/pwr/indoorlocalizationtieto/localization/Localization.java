package pl.wroc.pwr.indoorlocalizationtieto.localization;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.text.InputType;
import android.widget.EditText;


import java.util.List;

public class Localization {
    //location of the user
    private double posLat;
    private double posLon;
    private int posLevel;

    private WifiManager wifiManager;
    private Context context;
    private WifiScanReceiver wifiScanReceiver;

    /**
     * Constructor for localization module
     *
     * @param context - application context
     */
    public Localization(Context context) {
        this.context = context; //set application context
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); //ceate wifi manager
        inputLevelFromAlertDialog(); //run input dialog for entry floor number at the start of application/localization
        wifiScanReceiver = new WifiScanReceiver(wifiManager, context, this); //create wifi scan receiver
        posLon = posLat = 0; //starting position set on (0,0)
    }

    /**
     * Method for registering receiver
     */
    public void registerReceiver() {
        context.registerReceiver(wifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    /**
     * Method for unregistering receiver
     */
    public void unregisterReceiver() {
        context.unregisterReceiver(wifiScanReceiver);
    }

    /**
     * @return scan results acquired from WifiScanReceiver
     */
    public List<ScanResult> getWifis() {
        return wifiScanReceiver.getScanResults();
    }

    /**
     * @return current latitude
     */
    public double getPosLat() {
        return posLat;
    }

    /**
     * @return current longitude
     */
    public double getPosLon() {
        return posLon;
    }

    /**
     * @return current floor number
     */
    public int getPosLevel() {
        return posLevel;
    }

    /**
     * Set current floor number
     * @param posLevel
     */
    synchronized public void setPosLevel(int posLevel) {
        this.posLevel = posLevel;
    }

    /**
     * Method for performing (initializing) scanning
     */
    protected void startScan() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.startScan();
        }
    }

    /**
     * Method for updating user location
     * @param lat   - latitude
     * @param lon   - longitude
     * @param level - number of floor
     */
    synchronized protected void updateLocation(double lat, double lon, int level) {
        posLat = lat;
        posLon = lon;
        posLevel = level;
    }

    /**
     * Input Dialog (AlertDialog) for input an floor number for localization module
     */
    private void inputLevelFromAlertDialog() {
        //run alert dialog asking on floor to change
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Wprowadź piętro");
        builder.setMessage("Podaj piętro, na którym aktualnie się znajdujesz");
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                String inputString = input.getText().toString();
                if(inputString.isEmpty())
                    setPosLevel(0);
                else
                    setPosLevel(Integer.parseInt(inputString));
                dialog.dismiss();
                registerReceiver();
                startScan();
            }

        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setPosLevel(0);
                dialog.dismiss();
                registerReceiver();
                startScan();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
