package tiago.homemanagement;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
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
        if (item.getItemId() == android.R.id.home || item.getItemId() == R.id.home){
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            switch(spinner.getSelectedItemPosition()){
                case 0:
                    NavUtils.navigateUpTo(this,new Intent(this,ShoppingActivity.class));
                    break;
                case 1:
                    NavUtils.navigateUpTo(this,new Intent(this,FloorActivity.class));
                    break;
                default:
                    NavUtils.navigateUpTo(this,new Intent(this,MainActivity.class));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void newTask(View view) {
    }
}