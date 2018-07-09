package tiago.homemanagement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomePreferencesFragment fragment = new HomePreferencesFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content,fragment).commit();
    }
}