package tiago.homemanagement;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomePreferencesFragment fragment = new HomePreferencesFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content,fragment).commit();
    }
}