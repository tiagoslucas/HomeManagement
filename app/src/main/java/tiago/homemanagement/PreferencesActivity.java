package tiago.homemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            bindSummaryValue(findPreference("laundry_pref"));
            bindSummaryValue(findPreference("dishes_pref"));
            bindSummaryValue(findPreference("floor_pref"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.home){
            NavUtils.navigateUpTo(this,new Intent(this,MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindSummaryValue(Preference preference) {
        preference.setOnPreferenceChangeListener(listener);
        listener.onPreferenceChange(preference, PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference
                        .getKey(), ""));
    }

    private static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                // Set changes in the database
                if (preference.getKey().equals("laundry_pref")){

                } else if (preference.getKey().equals("dishes_pref")) {

                } else if (preference.getKey().equals("floor_pref")) {

                }

                // Set the summary to reflect the new value
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            }
            return false;
        }
    };
}
