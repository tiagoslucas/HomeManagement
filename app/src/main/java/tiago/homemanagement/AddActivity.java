package tiago.homemanagement;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if(getIntent().getStringExtra("parent").equals("Floor")) {
            spinner.setSelection(1);
        } else{
            spinner.setSelection(0);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home || item.getItemId() == R.id.home){
            NavUtils.navigateUpTo(this, getIntent());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void newTask(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        EditText name = (EditText) findViewById(R.id.editText);
        TaskItem task = new TaskItem();
        if(spinner.getSelectedItemPosition() == 0){
            // Shopping
            task.setName(name.getText().toString());
            task.setSettid(MainActivity.SHOPPING_SETTID);
            getContentResolver().insert(HomeContentProvider.TASKLIST_URI, TableTasklist.getContentValues(task));

        } else if (spinner.getSelectedItemPosition() == 1){
            // Floor
            task.setName(name.getText().toString());
            task.setDate(System.currentTimeMillis());
            task.setSettid(MainActivity.FLOOR_SETTID);
            getContentResolver().insert(HomeContentProvider.TASKLIST_URI, TableTasklist.getContentValues(task));
        }
    }
}