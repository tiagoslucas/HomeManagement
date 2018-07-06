package tiago.homemanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final int LAUNDRY_SETTID = 1;
    public static final int SHOPPING_SETTID = 2;
    public static final int DISHES_SETTID = 3;
    public static final int FLOOR_SETTID = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_settings){
            startActivity(new Intent(this, PreferencesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void laundry(View view) { startActivity(new Intent(this,LaundryActivity.class)); }

    public void shopping(View view) { startActivity(new Intent(this,ShoppingActivity.class)); }

    public void floor(View view) { startActivity(new Intent(this,FloorActivity.class)); }

    public void dishes(View view) { startActivity(new Intent(this,DishesActivity.class)); }
}
