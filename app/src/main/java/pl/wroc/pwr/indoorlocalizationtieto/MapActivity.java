package pl.wroc.pwr.indoorlocalizationtieto;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Geometry;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Line;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Point;
import pl.wroc.pwr.indoorlocalizationtieto.Geometry.Polygon;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.GeometryRenderer;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.MapView;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.Renderer;



public class MapActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapView mapView = (MapView) findViewById(R.id.mapView);
        Renderer renderer = new GeometryRenderer(setupDummyGeometries());
        mapView.setRenderer(renderer);

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


    private ArrayList<Geometry> setupDummyGeometries() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        Point pointS = new Point(1.0, 1.0);
        Point pointE = new Point(200.0, 200.0);
        geometries.add(new Line(pointS, pointE));
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(50.0, 50.0));
        points.add(new Point(25.0, 75.0));
        points.add(new Point(50.0, 100.0));
        points.add(new Point(75.0, 75.0));
        geometries.add(new Polygon(points));

        return geometries;
    }

}
