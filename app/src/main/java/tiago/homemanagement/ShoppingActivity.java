package tiago.homemanagement;

import android.content.DialogInterface;
import android.database.Cursor;
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

public class ShoppingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {
    private static final int CURSOR_LOADER_ID = 0;
    private HomeCursorAdapter cursorAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.shoppingRecyclerView);
        cursorAdapter = new HomeCursorAdapter(this);
        recyclerView.setAdapter(cursorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
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
                    new String[]{String.valueOf(MainActivity.SHOPPING_SETTID)},
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
        dialog.setTitle(getString(R.string.new_item));
        final EditText input = new EditText(this);
        dialog.setView(input);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TaskItem task = new TaskItem();
                task.setName(input.getText().toString());
                task.setDone(0);
                task.setDate(System.currentTimeMillis());
                task.setSettid(MainActivity.SHOPPING_SETTID);
                getContentResolver().insert(HomeContentProvider.TASKLIST_URI, TableTasklist.getContentValues(task));
                dialog.dismiss();
                refresh();
            }
        });
        dialog.show();
    }

    public void clearShoplist(View view) {
        getContentResolver().delete(HomeContentProvider.TASKLIST_URI, TableTasklist.SETTING_FIELD + "=?", new String[] {String.valueOf(MainActivity.SHOPPING_SETTID)});
        refresh();
    }

    public void refresh(){
        cursorAdapter.refreshData(getContentResolver().query(HomeContentProvider.TASKLIST_URI,
                TableTasklist.ALL_COLUMNS,
                TableTasklist.SETTING_FIELD + "=?",
                new String[]{String.valueOf(MainActivity.SHOPPING_SETTID)},
                null));
    }
}
