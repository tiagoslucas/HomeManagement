package tiago.homemanagement;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {check();
            }
        });
        setFields();

        TextView rainyChance = (TextView) findViewById(R.id.rainy_chance);
        Cursor cursor = getContentResolver().query(Uri.withAppendedPath(HomeContentProvider.SETTINGS_URI,"0"),
                TableSettings.ALL_COLUMNS,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            Settings settings = TableSettings.getCurrentSettings(cursor);
            rainyChance.setText(settings.getTime());
        } else {
            Toast.makeText(this,"No data obtained",Toast.LENGTH_LONG).show();
        }
    }

    private void setFields(){
        TextView drying_check = (TextView) findViewById(R.id.hanging_check);
        TextView laundry_days = (TextView) findViewById(R.id.laundry_days);
        Cursor cursor = getContentResolver().query(
                HomeContentProvider.TASKLIST_URI,
                TableTasklist.ALL_COLUMNS,
                TableTasklist.SETTING_FIELD + "=?",
                new String[]{String.valueOf(MainActivity.LAUNDRY_SETTID)},
                null);
        if (cursor.moveToFirst()) {
            task = TableTasklist.getCurrentTaskItem(cursor);
            drying_check.setVisibility(task.isDone() ? View.VISIBLE : View.INVISIBLE);
            long time = System.currentTimeMillis() - task.getTime();
            laundry_days.setText((int) TimeUnit.MILLISECONDS.toDays(time));
        } else {
            Toast.makeText(this,"No data obtained",Toast.LENGTH_LONG).show();
        }
    }

    private void check() {
        TextView check = (TextView) findViewById(R.id.hanging_check);
        if(check.getVisibility() == View.VISIBLE){
            check.setVisibility(View.INVISIBLE);
            setFields();
        } else {
            check.setVisibility(View.VISIBLE);
        }
    }
}
