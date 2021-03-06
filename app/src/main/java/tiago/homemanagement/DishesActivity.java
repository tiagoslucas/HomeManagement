package tiago.homemanagement;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class DishesActivity extends AppCompatActivity {

    TaskItem task = new TaskItem();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes);
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
        setFields();
    }

    private void setFields(){
        TextView drying_check = findViewById(R.id.drying_check);
        TextView dishes_days = findViewById(R.id.dishes_days);
        Cursor cursor = getContentResolver().query(
                HomeContentProvider.TASKLIST_URI,
                TableTasklist.ALL_COLUMNS,
                TableTasklist.SETTING_FIELD + "=?",
                new String[]{String.valueOf(MainActivity.DISHES_SETTID)},
                null);
        if (cursor.moveToFirst()) {
            task = TableTasklist.getCurrentTaskItem(cursor);
            drying_check.setVisibility(task.isDone() ? View.VISIBLE : View.INVISIBLE);
            long time = System.currentTimeMillis() - task.getTime();
            if (TimeUnit.MILLISECONDS.toDays(time) > 99)
                dishes_days.setTextSize(20);
            dishes_days.setText(Long.toString(TimeUnit.MILLISECONDS.toDays(time)));
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
                    // notificationTime = System.currentTimeMillis() + 2000;
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class).putExtra("activity",MainActivity.DISHES_SETTID);
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