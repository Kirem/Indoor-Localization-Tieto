package pl.wroc.pwr.indoorlocalizationtieto.Parser;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import pl.wroc.pwr.indoorlocalizationtieto.R;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;

public class TestParserActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_parser);
        OverpassDataFetcher fetcher = new OverpassDataFetcher();
        JsonLoadedListener listener = new JsonLoadedListener() {
            @Override
            public void onJsonLoaded(OSMData data) {
                OSMDataParser parser = new OSMDataParser();
                Map map = parser.parseOSMData(data);
                Log.i("OBJECTS", "objects loaded: " + map.getObjects().size());
            }
        };
        String[] query = {"51.10897" ,"17.06019" , "100"};
        //String query[] = new String[]{"51.09408", "17.018144", "200"};
        fetcher.startFetching(query, listener);
        String temp = new String("test");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_parser, menu);
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
}