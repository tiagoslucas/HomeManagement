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

public class ShoppingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {
    private static final int CURSOR_LOADER_ID = 0;
    private ShoppingCursorAdapter cursorAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.shoppingRecyclerView);
        cursorAdapter = new ShoppingCursorAdapter(this);
        recyclerView.setAdapter(cursorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        /*
        Populate shopping list

        LinearLayout shopList = (LinearLayout) findViewById(R.id.shopList);
        TextView shopItem = new TextView(getApplicationContext());
        shopItem.setTextSize(20);

        for(  ){
            shopItem.setText( take from db );
            shopList.addView(shopItem, i);
        } */
    }

    @Override
    protected void onResume(){
        super.onResume();
        getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        if (id == CURSOR_LOADER_ID) {
            return new CursorLoader(this, HomeContentProvider.TASKLIST_URI, TableTasklist.ALL_COLUMNS, null, null, null);
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
        intent.putExtra("parent","Shopping");
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

    public void clearShoplist(View view) {
        getContentResolver().delete(HomeContentProvider.TASKLIST_URI, TableTasklist.NAME_FIELD + "=?", new String[] { "shopping" });
    }
}
