package tiago.homemanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

public class FloorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;
    private FloorCursorAdapter cursorAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.floorRecyclerView);
        cursorAdapter = new FloorCursorAdapter(this);
        recyclerView.setAdapter(cursorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {add(); }
        });

        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);

        Cursor cursor = getContentResolver().query(HomeContentProvider.TASKLIST_URI,
                new String[]{TableTasklist.SETTING_FIELD},
                TableTasklist.SETTING_FIELD + "=?",
                new String[]{String.valueOf(MainActivity.FLOOR_SETTID)},
                TableTasklist.DATE_FIELD + " DESC");
        cursor.moveToNext();
        TaskItem taskItem = TableTasklist.getCurrentTaskItem(cursor);
        long time = taskItem.getTime();
        time = System.currentTimeMillis() - time;
        TextView floor_days = (TextView) findViewById(R.id.floor_days);
        floor_days.setText((int) TimeUnit.MILLISECONDS.toDays(time));
    }

    @Override
    protected void onResume(){
        super.onResume();
        getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        if (id == CURSOR_LOADER_ID) {
            return new CursorLoader(this,
                    HomeContentProvider.TASKLIST_URI,
                    new String[]{TableTasklist.SETTING_FIELD},
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
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("parent","Floor");
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == android.R.id.home || item.getItemId() == R.id.home){
            NavUtils.navigateUpTo(this,new Intent(this,MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}