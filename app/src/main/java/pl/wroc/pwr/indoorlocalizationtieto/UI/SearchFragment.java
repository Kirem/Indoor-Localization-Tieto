package pl.wroc.pwr.indoorlocalizationtieto.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.wroc.pwr.indoorlocalizationtieto.R;
import pl.wroc.pwr.indoorlocalizationtieto.map.MapObject;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private ListView listView;
    private EditText editText;
    private Button searchBut;
    private MapObject selectedObject;
    private ArrayList<MapObject> objectsList;
    private String searchedPhrase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.search_layout, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        editText = (EditText) rootView.findViewById(R.id.eTSearch);
        searchBut = (Button) rootView.findViewById(R.id.butSearch);
        searchBut.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                selectedObject= getSelectedObject(item.replace(searchedPhrase + " ", ""));
                Toast.makeText(getActivity(), selectedObject.getName(), Toast.LENGTH_SHORT).show();
            }

        });

        return rootView;
    }

    private ArrayList<String> createDisplayList (){
        ArrayList<String> list = new ArrayList<String>();
        for (MapObject object : objectsList) {
            list.add(searchedPhrase + " " + object.getName());
        }
        return list;
    }

    private void displayList () {
        final ArrayList<String> list = new ArrayList<>(createDisplayList());
        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    private MapObject getSelectedObject (String selected) {
        for (MapObject object : objectsList) {
            if (object.getName().equals(selected)) {
                return object;
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.butSearch) {
            searchedPhrase = editText.getText().toString();
            objectsList = new ArrayList<>();
            //objectsList = searchEngine.findElementsWithinQuery(text);
            displayList();
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
