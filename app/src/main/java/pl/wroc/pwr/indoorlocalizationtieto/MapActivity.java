package pl.wroc.pwr.indoorlocalizationtieto;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import pl.wroc.pwr.indoorlocalizationtieto.UI.MapFragment;
import pl.wroc.pwr.indoorlocalizationtieto.UI.SearchFragment;
import pl.wroc.pwr.indoorlocalizationtieto.UI.SpecificationFragment;


public class MapActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private MapFragment mapFragment;
    private SearchFragment searchFragment;
    private SpecificationFragment specificationFragment;
    private static final String[] ITEMS_ARRAY = {"OBECNA LOKALIZACJA", "NAWIGUJ", "SZUKAJ", "OPCJE", "OPIS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mapFragment = new MapFragment();
        searchFragment = new SearchFragment();
        specificationFragment = new SpecificationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mapFragment).commit();

        mActivityTitle = getTitle().toString();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addDrawerItems(){
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ITEMS_ARRAY);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 0:
                        Toast.makeText(MapActivity.this, "TODO: Wyswietla mape z obecna " +
                                "lokalizacja i markerem", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        openAlertDialog(view);
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                searchFragment).commit();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 3:
                        Toast.makeText(MapActivity.this, "TODO: opcje wyszukiwania w " +
                                "nowym layoucie", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                specificationFragment).commit();
                        mDrawerLayout.closeDrawers();
                        break;
                }
            }
        });
    }

    private void openAlertDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nawigacja:");
        builder.setMessage("Aby ustawić lokalizację kliknij, które " +
                "położenie chcesz określić, a następnie określ miejsce na mapie...");
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.navi_alert_dialog,
                (ViewGroup) findViewById(R.id.navi_dialog));
        builder.setView(layout);
        builder.setPositiveButton(R.string.ok_but, new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(MapActivity.this, "TODO: Okresla nawigacje na podstawie " +
                        "ustawionych miejsc", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.cancel_but, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close){

            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}
