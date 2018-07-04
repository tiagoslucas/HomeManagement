package tiago.homemanagement;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class PreferencesActivity extends PreferenceActivity {

    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainPreferenceFragment()).commit();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            bindSummaryValue(findPreference("laundry_pref"));
            bindSummaryValue(findPreference("dishes_pref"));
            bindSummaryValue(findPreference("floor_pref"));
            bindSummaryValue(findPreference("rainy_days"));
        }
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
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        updateDatabase();
        super.onDestroy();
    }

    private void updateDatabase(){
        Settings setting;
        String stringValue;
        stringValue = (String) prefs.getString("laundry_pref","");
        setting = new Settings(1,"laundry",calcTime(stringValue));
        getContentResolver().update(
                Uri.withAppendedPath(HomeContentProvider.SETTINGS_URI, String.valueOf(MainActivity.LAUNDRY_SETTID)),
                TableSettings.getContentValues(setting),
                null,
                null);

        stringValue = (String) prefs.getString("dishes_pref","");
        setting = new Settings(3,"dishes",calcTime(stringValue));
        getContentResolver().update(
                Uri.withAppendedPath(HomeContentProvider.SETTINGS_URI, String.valueOf(MainActivity.DISHES_SETTID)),
                TableSettings.getContentValues(setting),
                null,
                null);

        stringValue = (String) prefs.getString("floor_pref","");
        setting = new Settings(4,"floor",calcTime(stringValue));
        getContentResolver().update(
                Uri.withAppendedPath(HomeContentProvider.SETTINGS_URI, String.valueOf(MainActivity.FLOOR_SETTID)),
                TableSettings.getContentValues(setting),
                null,
                null);

        stringValue = (String) prefs.getString("rainy_days","");
        setting = new Settings(0,"laundry",calcTime(stringValue));
        getContentResolver().update(
                Uri.withAppendedPath(HomeContentProvider.SETTINGS_URI, "0"),
                TableSettings.getContentValues(setting),
                null,
                null);
    }

    public static int calcTime(String stringValue){
        switch (stringValue) {
            case "1 day":
                return 1;
            case "2 days":
                return 2;
            case "3 days":
                return 3;
            case "7 days":
                return 7;
            case "15 days":
                return 15;
            case "30 days":
                return 30;
            case "45 days":
                return 45;
            case "60 days":
                return 60;
            default:
                return 0;
        }
    }
}