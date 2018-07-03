package tiago.homemanagement;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class DishesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {check();
            }
        });
    }

    private void check() {
        TextView check = (TextView) findViewById(R.id.drying_check);
        if(check.getVisibility() == View.VISIBLE){
            check.setVisibility(View.INVISIBLE);
            TextView laundry_days = (TextView) findViewById(R.id.laundry_days);
            Cursor cursor = getContentResolver().query(Uri.withAppendedPath(HomeContentProvider.TASKLIST_URI, Integer.toString(MainActivity.DISHES_SETTID)),
                    new String[]{ TableTasklist.SETTING_FIELD + "=?"}, new Bundle(MainActivity.DISHES_SETTID), null);
            TaskItem task = TableTasklist.getCurrentTaskItem(cursor);
            long time = System.currentTimeMillis() - task.getTime();
            laundry_days.setText((int) TimeUnit.MILLISECONDS.toDays(time));
        } else {
            check.setVisibility(View.VISIBLE);
        }
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
