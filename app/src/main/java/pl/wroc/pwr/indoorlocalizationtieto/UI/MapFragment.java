package pl.wroc.pwr.indoorlocalizationtieto.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import pl.wroc.pwr.indoorlocalizationtieto.Parser.JsonLoadedListener;
import pl.wroc.pwr.indoorlocalizationtieto.Parser.OSMData;
import pl.wroc.pwr.indoorlocalizationtieto.Parser.OSMDataParser;
import pl.wroc.pwr.indoorlocalizationtieto.Parser.OverpassDataFetcher;
import pl.wroc.pwr.indoorlocalizationtieto.R;
import pl.wroc.pwr.indoorlocalizationtieto.map.Map;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.GeometryRenderer;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.MapObjectPointCalculator;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.MapView;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.Renderer;

public class MapFragment extends Fragment implements View.OnClickListener, JsonLoadedListener {
    public static final String LATITUDE = "51.09408";
    public static final String LONGITUDE = "17.018144";
    //        public static final String LATITUDE = "51.10897";
//    public static final String LONGITUDE = "17.06019";
    public static final String RADIUS = "100";
    private MapView mapView;
    private GeometryRenderer renderer;
    private ImageButton butPlus;
    private ImageButton butMinus;
    private ImageButton butUp;
    private ImageButton butDown;
    Map map;

    public MapFragment() {
    }

    public MapFragment(Context context) {
        createMap();
        renderer = new GeometryRenderer(new ArrayList<MapObject>(), context);
        renderer.setStyle(R.raw.mapjsonzoom);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_layout, container, false);

        mapView = (MapView) rootView.findViewById(R.id.view);
        butPlus = (ImageButton) rootView.findViewById(R.id.butPlus);
        butMinus = (ImageButton) rootView.findViewById(R.id.butMinus);
        butUp = (ImageButton) rootView.findViewById(R.id.butUp);
        butDown = (ImageButton) rootView.findViewById(R.id.butDown);
        butPlus.setOnClickListener(this);
        butMinus.setOnClickListener(this);
        butUp.setOnClickListener(this);
        butDown.setOnClickListener(this);

        mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                renderer.setPositionCalculator
                        (new MapObjectPointCalculator(Float.valueOf(LATITUDE),
                                Float.valueOf(LONGITUDE), mapView.getHeight(), mapView.getWidth(), Float.valueOf(RADIUS)/**10*/));
                mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.i("OnCreateView", "calculator set:" + mapView.getHeight());
            }
        });
        mapView.setRenderer(renderer);
        mapView.setMapSize(1200f, 1200f);
        return rootView;
    }


    private void createMap() {
        OverpassDataFetcher fetcher = new OverpassDataFetcher();
        String string[] = new String[]{LATITUDE, LONGITUDE, RADIUS};
        Log.i("OBJECTS", "startedloading: ");
        fetcher.startFetching(string, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.butPlus) {
            mapView.zoomIn();
        } else if (id == R.id.butMinus) {
            mapView.zoomOut();
        } else if (id == R.id.butUp) {

        } else if (id == R.id.butDown) {

        }
    }

    @Override
    public void onJsonLoaded(final OSMData data) {
        final OSMDataParser parser = new OSMDataParser();
        Log.i("OBJECTS", "loaded: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                map = parser.parseOSMData(data);
                mapView.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "objects loaded: " + map.getObjects().size(), Toast.LENGTH_SHORT).show();
                        renderer.setMapObjects(map.getObjects());
                        Log.i("OBJECTS", "parsed: ");
                        mapView.invalidate();
                    }
                });
            }
        }).start();
    }
}