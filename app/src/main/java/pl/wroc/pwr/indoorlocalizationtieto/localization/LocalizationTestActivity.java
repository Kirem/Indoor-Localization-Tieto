package pl.wroc.pwr.indoorlocalizationtieto.localization;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import pl.wroc.pwr.indoorlocalizationtieto.R;

/**
 * Created by Mateusz on 2015-05-10.
 * Test Activity for localization module
 */
public class LocalizationTestActivity extends ActionBarActivity {
    private Localization localization;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localization_layout);

        localization = new Localization(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        localization.registerReceiver();
        super.onResume();
    }

    @Override
    public void onPause() {
        localization.unregisterReceiver();
        super.onPause();
    }
}
