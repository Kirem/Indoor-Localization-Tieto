package pl.wroc.pwr.indoorlocalizationtieto;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import pl.wroc.pwr.indoorlocalizationtieto.localTester.LocalTester;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.GeometryRenderer;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.MapView;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.Renderer;


public class MapActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapView map = (MapView) findViewById(R.id.mapView);
        Renderer renderer = new GeometryRenderer(LocalTester.SetupDummyMapObjects(), this);
        renderer.setStyle(R.raw.mapjson);
        map.setRenderer(renderer);

        map.setRenderer(renderer);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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



}
