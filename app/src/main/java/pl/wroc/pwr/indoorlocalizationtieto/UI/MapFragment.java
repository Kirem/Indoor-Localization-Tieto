package pl.wroc.pwr.indoorlocalizationtieto.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import pl.wroc.pwr.indoorlocalizationtieto.R;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.GeometryRenderer;
import pl.wroc.pwr.indoorlocalizationtieto.renderer.MapView;

public class MapFragment extends Fragment implements View.OnClickListener {
    private MapView mapView;
//    private GeometryRenderer renderer;
    private ImageButton butPlus;
    private ImageButton butMinus;
    private ImageButton butUp;
    private ImageButton butDown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
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

        return rootView;
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        if(id == R.id.butPlus){

        }else if(id == R.id.butMinus){

        }else if(id == R.id.butUp){

        }else if(id == R.id.butDown){

        }
    }
}
