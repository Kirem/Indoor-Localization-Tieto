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
        butPlus = (ImageButton) rootView.findViewById(R.id.buttonPlus);
        butMinus = (ImageButton) rootView.findViewById(R.id.buttonMinus);
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
        if(butPlus == v){

        }else
            if(butMinus == v){

            }else
                if(butUp == v){

                }else
                    if(butDown == v){

                    }
    }
}
