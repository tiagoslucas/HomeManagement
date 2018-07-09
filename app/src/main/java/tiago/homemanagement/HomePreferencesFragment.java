package tiago.homemanagement;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class HomePreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference button = findPreference("database_manager");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getContext(), AndroidDatabaseManager.class));
                return true;
            }
        });

        Preference.OnPreferenceChangeListener changeListener = new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();
                if (preference instanceof ListPreference) {
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);
                    preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                    int value = preferenceSwitch(stringValue);
                    int settid;
                    switch (preference.getTitle().toString().toLowerCase()) {
                        case "dishes":
                            settid = MainActivity.DISHES_SETTID;
                            break;
                        case "floor":
                            settid = MainActivity.FLOOR_SETTID;
                            break;
                        case "laundry":
                            settid = MainActivity.LAUNDRY_SETTID;
                            break;
                        default:
                            settid = 2;
                    }
                    Settings setting;
                    setting = new Settings(settid, preference.getTitle().toString().toLowerCase(), value);
                    getContext().getContentResolver().update(
                            Uri.withAppendedPath(HomeContentProvider.SETTINGS_URI, String.valueOf(settid)),
                            TableSettings.getContentValues(setting),
                            null,
                            null);

                    return true;
                }
                return false;
            }
        };

        ListPreference pref1 = (ListPreference)findPreference("laundry_pref");
        pref1.setOnPreferenceChangeListener(changeListener);

        ListPreference pref2 = (ListPreference)findPreference("dishes_pref");
        pref2.setOnPreferenceChangeListener(changeListener);

        ListPreference pref3 = (ListPreference)findPreference("floor_pref");
        pref3.setOnPreferenceChangeListener(changeListener);

        summarySetup(pref1, MainActivity.LAUNDRY_SETTID);
        summarySetup(pref2, MainActivity.DISHES_SETTID);
        summarySetup(pref3, MainActivity.FLOOR_SETTID);
    }

    private void summarySetup(Preference preference, int settid) {
        String summary = null;
        Cursor cursor = getContext().getContentResolver().query(Uri.withAppendedPath(HomeContentProvider.SETTINGS_URI, String.valueOf(settid)),
                new String[]{TableSettings.TIME_FIELD}, null, null, null);
        if (cursor.moveToFirst()) {
            summary = preferenceSwitch(cursor.getInt(cursor.getColumnIndex(TableSettings.TIME_FIELD)));
        }
        if (summary != null) {
            preference.setSummary(summary);
        }
        cursor.close();
    }

    private static String preferenceSwitch(int position) {
        switch (position) {
            case 1:
                return "1 day";
            case 2:
                return "2 days";
            case 3:
                return "3 days";
            case 7:
                return "7 days";
            case 15:
                return "15 days";
            case 30:
                return "30 days";
            case 45:
                return "45 days";
            case 60:
                return "60 days";
            default:
                return "";
        }
    }
    private static int preferenceSwitch(String value) {
        switch (value) {
            case "1 dia":
            case "1 day":
                return 1;
            case "2 dias":
            case "2 days":
                return 2;
            case "3 dias":
            case "3 days":
                return 3;
            case "7 dias":
            case "7 days":
                return 7;
            case "15 dias":
            case "15 days":
                return 15;
            case "30 dias":
            case "30 days":
                return 30;
            case "45 dias":
            case "45 days":
                return 45;
            case "60 dias":
            case "60 days":
                return 60;
            default:
                return 0;
        }
    }
}