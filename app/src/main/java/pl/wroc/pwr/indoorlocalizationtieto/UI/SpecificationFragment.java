package pl.wroc.pwr.indoorlocalizationtieto.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.wroc.pwr.indoorlocalizationtieto.R;

public class SpecificationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.specification_layout, container, false);
        return rootView;
    }
}
