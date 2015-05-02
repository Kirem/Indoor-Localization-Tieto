package pl.wroc.pwr.indoorlocalizationtieto.UI;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import pl.wroc.pwr.indoorlocalizationtieto.R;

public class SettingsActivity extends PreferenceActivity {
    private CheckBoxPreference shortestPath;
    private CheckBoxPreference avoidRooms;
    private CheckBoxPreference avoidElevators;
    private CheckBoxPreference avoidStairs;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferences);

        shortestPath = (CheckBoxPreference) getPreferenceManager().findPreference("pref_shortest_path");
        shortestPath.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            public boolean onPreferenceChange(Preference preference, Object newValue){
                Toast.makeText(SettingsActivity.this, preference.getTitle() + " changed value to " + newValue.toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        avoidRooms = (CheckBoxPreference) getPreferenceManager().findPreference("pref_avoid_rooms");
        avoidRooms.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            public boolean onPreferenceChange(Preference preference, Object newValue){
                Toast.makeText(SettingsActivity.this, preference.getTitle() + " changed value to " + newValue.toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        avoidElevators = (CheckBoxPreference) getPreferenceManager().findPreference("pref_avoid_elevators");
        avoidElevators.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            public boolean onPreferenceChange(Preference preference, Object newValue){
                Toast.makeText(SettingsActivity.this, preference.getTitle() + " changed value to " + newValue.toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        avoidStairs = (CheckBoxPreference) getPreferenceManager().findPreference("pref_avoid_stairs");
        avoidStairs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            public boolean onPreferenceChange(Preference preference, Object newValue){
                Toast.makeText(SettingsActivity.this, preference.getTitle() + " changed value to " + newValue.toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
