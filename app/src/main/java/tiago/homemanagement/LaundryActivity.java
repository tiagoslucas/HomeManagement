package tiago.homemanagement;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class LaundryActivity extends AppCompatActivity {

    TaskItem task = new TaskItem();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {check();
            }
        });

        /* Rain Propability NOT Working, API needs payment
           See more: https://www.wunderground.com/weather/api/
        TextView rainyChance = (TextView) findViewById(R.id.rainy_chance);
        Cursor cursor = getContentResolver().query(Uri.withAppendedPath(HomeContentProvider.SETTINGS_URI, String.valueOf(MainActivity.LAUNDRY_SETTID)),
                TableSettings.ALL_COLUMNS,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            Settings settings = TableSettings.getCurrentSettings(cursor);
            rainyChance.setText(Integer.toString(settings.getTime()));
        } else {
            Toast.makeText(this,"No rain propability",Toast.LENGTH_LONG).show();
        }
        */
        setFields();
    }

    private void setFields(){
        TextView hanging_check = findViewById(R.id.hanging_check);
        TextView laundry_days = findViewById(R.id.laundry_days);
        Cursor cursor = getContentResolver().query(
                HomeContentProvider.TASKLIST_URI,
                TableTasklist.ALL_COLUMNS,
                TableTasklist.SETTING_FIELD + "=?",
                new String[]{String.valueOf(MainActivity.LAUNDRY_SETTID)},
                null);
        if (cursor.moveToFirst()) {
            task = TableTasklist.getCurrentTaskItem(cursor);
            hanging_check.setVisibility(task.isDone() ? View.VISIBLE : View.INVISIBLE);
            long time = System.currentTimeMillis() - task.getTime();
            if (TimeUnit.MILLISECONDS.toDays(time) > 99)
                laundry_days.setTextSize(15);
            laundry_days.setText(Long.toString(TimeUnit.MILLISECONDS.toDays(time)));
        } else {
            Toast.makeText(this,R.string.no_data,Toast.LENGTH_LONG).show();
        }
    }

    private void check() {
        if (task.isDone()) {
            task.setDate(System.currentTimeMillis());
            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("notifications_switch", false)) {
                Cursor cursor = getContentResolver().query(Uri.withAppendedPath(HomeContentProvider.SETTINGS_URI,
                        String.valueOf(MainActivity.DISHES_SETTID)),
                        new String[]{TableSettings.TIME_FIELD},
                        null,
                        null,
                        null);
                if (cursor.moveToFirst()) {
                    long notificationTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(cursor.getInt(cursor.getColumnIndex(TableSettings.TIME_FIELD)));
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class).putExtra("activity",MainActivity.LAUNDRY_SETTID);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime,
                            PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                }
                cursor.close();
            }
        }
        task.setDone(task.isDone() ? 0 : 1);
        getContentResolver().update(
                Uri.withAppendedPath(HomeContentProvider.TASKLIST_URI, String.valueOf(task.getId())),
                TableTasklist.getContentValues(task),
                null,
                null);
        setFields();
    }
}