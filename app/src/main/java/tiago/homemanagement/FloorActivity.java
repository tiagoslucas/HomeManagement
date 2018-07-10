package tiago.homemanagement;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;

public class FloorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;
    private HomeCursorAdapter cursorAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {add(); }
        });

        recyclerView = (RecyclerView) findViewById(R.id.floorRecyclerView);
        cursorAdapter = new HomeCursorAdapter(this);
        recyclerView.setAdapter(cursorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);

        Cursor cursor = getContentResolver().query(HomeContentProvider.TASKLIST_URI,
                TableTasklist.ALL_COLUMNS,
                TableTasklist.SETTING_FIELD + "=?",
                new String[]{String.valueOf(MainActivity.FLOOR_SETTID)},
                TableTasklist.DATE_FIELD + " DESC");
        if (cursor.moveToFirst()) {
            TextView floor_days = (TextView) findViewById(R.id.floor_days);
            TaskItem taskItem = TableTasklist.getCurrentTaskItem(cursor);
            long time = System.currentTimeMillis() - taskItem.getTime();
            if (TimeUnit.MILLISECONDS.toDays(time) > 99)
                floor_days.setTextSize(15);
            floor_days.setText(Long.toString(TimeUnit.MILLISECONDS.toDays(time)));
        } else {
            Toast.makeText(this, R.string.no_data,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        if (id == CURSOR_LOADER_ID) {
            return new android.support.v4.content.CursorLoader(this,
                    HomeContentProvider.TASKLIST_URI,
                    TableTasklist.ALL_COLUMNS,
                    TableTasklist.SETTING_FIELD + "=?",
                    new String[]{String.valueOf(MainActivity.FLOOR_SETTID)},
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cursorAdapter.refreshData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.refreshData(null);
    }

    private void add() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.alert_floor));
        final EditText input = new EditText(this);
        dialog.setView(input);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TaskItem task = new TaskItem();
                task.setName(input.getText().toString());
                task.setDone(0);
                task.setDate(System.currentTimeMillis());
                task.setSettid(MainActivity.FLOOR_SETTID);
                getContentResolver().insert(HomeContentProvider.TASKLIST_URI, TableTasklist.getContentValues(task));
                dialog.dismiss();
                cursorAdapter.refreshData(getContentResolver().query(HomeContentProvider.TASKLIST_URI,
                        TableTasklist.ALL_COLUMNS,
                        TableTasklist.SETTING_FIELD + "=?",
                        new String[]{String.valueOf(MainActivity.FLOOR_SETTID)},
                        null));
                Cursor cursor = getContentResolver().query(Uri.withAppendedPath(HomeContentProvider.SETTINGS_URI,
                        String.valueOf(MainActivity.DISHES_SETTID)),
                        new String[]{TableSettings.TIME_FIELD},
                        null,
                        null,
                        null);
                if (cursor.moveToFirst()) {
                    long notificationTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(cursor.getInt(cursor.getColumnIndex(TableSettings.TIME_FIELD)));
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class).putExtra("activity",MainActivity.FLOOR_SETTID);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime,
                            PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                }
            }
        });
        dialog.show();
    }
}