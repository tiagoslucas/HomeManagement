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

    public static final int SHOPPING_SETTID = 2;
    private static final int FLOOR_SETTID = 4;

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        if (item.getItemId() == android.R.id.home || item.getItemId() == R.id.home){
            switch(spinner.getSelectedItemPosition()){
                case 0:
                    NavUtils.navigateUpTo(this,getIntent());
                    break;
                case 1:
                    NavUtils.navigateUpTo(this,getIntent());
                    break;
                default:
                    NavUtils.navigateUpTo(this,new Intent(this,MainActivity.class));
            }
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
            DatabaseOpenHelper openHelper = new DatabaseOpenHelper(getApplicationContext());
            TableTasklist tableTasklist = new TableTasklist(openHelper.getReadableDatabase());
            task.setName(name.getText().toString());
            task.setSettid(SHOPPING_SETTID);
            tableTasklist.update(
                    TableTasklist.getContentValues(task),
                    TableTasklist.SETTING_FIELD + "=?",
                    new String[] {String.valueOf(SHOPPING_SETTID)}
            );
        } else if (spinner.getSelectedItemPosition() == 1){
            // Floor
            DatabaseOpenHelper openHelper = new DatabaseOpenHelper(getApplicationContext());
            TableTasklist tableTasklist = new TableTasklist(openHelper.getReadableDatabase());
            task.setName(name.getText().toString());
            task.setDate(System.currentTimeMillis());
            task.setSettid(FLOOR_SETTID);
            tableTasklist.update(
                    TableTasklist.getContentValues(task),
                    TableTasklist.SETTING_FIELD + "=?",
                    new String[] {String.valueOf(FLOOR_SETTID)}
            );
        }
    }
}