package tiago.homemanagement;

import android.content.Intent;
import android.database.Cursor;
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

    TaskItem task;
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
        Cursor cursor = getContentResolver().query(
                HomeContentProvider.TASKLIST_URI,
                new String[]{TableTasklist.SETTING_FIELD},
                TableTasklist.SETTING_FIELD + "=?",
                new String[]{String.valueOf(MainActivity.DISHES_SETTID)},
                null);

        task = TableTasklist.getCurrentTaskItem(cursor);
        setFields();
    }

    private void setFields(){
        TextView drying_check = (TextView) findViewById(R.id.drying_check);
        TextView laundry_days = (TextView) findViewById(R.id.dishes_days);
        drying_check.setVisibility(task.isDone() ? View.VISIBLE : View.INVISIBLE);
        long time = System.currentTimeMillis() - task.getTime();
        laundry_days.setText((int) TimeUnit.MILLISECONDS.toDays(time));
    }

    private void check() {
        TextView check = (TextView) findViewById(R.id.drying_check);
        if (check.getVisibility() == View.VISIBLE) {
            check.setVisibility(View.INVISIBLE);
            setFields();
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