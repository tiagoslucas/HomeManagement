package tiago.homemanagement;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.MatrixCursor;

import java.util.ArrayList;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final boolean TESTING = false;
    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "homemanagement.db";

    DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableTasklist tasklist = new TableTasklist(db);
        TableSettings settings = new TableSettings(db);
        tasklist.create();
        settings.create();

        if (!TESTING) {
            tasklistDefaults(db);
            settingsTableConstructor(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    private void settingsTableConstructor(SQLiteDatabase db){
        TableSettings settings = new TableSettings(db);
        settings.insert(TableSettings.getContentValues(new Settings(1,"laundry",2)));
        settings.insert(TableSettings.getContentValues(new Settings(2,"shopping",0)));
        settings.insert(TableSettings.getContentValues(new Settings(3, "dishes", 1)));
        settings.insert(TableSettings.getContentValues(new Settings(4, "floor", 30)));
    }

    private void tasklistDefaults(SQLiteDatabase db){
        TableTasklist tasklist = new TableTasklist(db);

        TaskItem laundryTask = new TaskItem();
        laundryTask.setName("laundry");
        laundryTask.setDone(1);
        laundryTask.setDate(System.currentTimeMillis());
        laundryTask.setSettid(1);
        tasklist.insert(TableTasklist.getContentValues(laundryTask));

        TaskItem dishesTask = new TaskItem();
        dishesTask.setName("dishes");
        dishesTask.setDone(1);
        dishesTask.setDate(System.currentTimeMillis());
        dishesTask.setSettid(3);
        tasklist.insert(TableTasklist.getContentValues(dishesTask));
    }
}
