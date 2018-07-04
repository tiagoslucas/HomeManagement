package tiago.homemanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "homemanagement.db";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableTasklist tasklist = new TableTasklist(db);
        tasklist.create();

        TaskItem laundryTask = new TaskItem();
        laundryTask.setName("laundry");
        laundryTask.setDone(1);
        laundryTask.setDate(System.currentTimeMillis());
        laundryTask.setSettid(1);
        tasklist.insert(TableTasklist.getContentValues(laundryTask));

        TaskItem dishesTask = new TaskItem();
        dishesTask.setName("dishes");
        dishesTask.setDone(0);
        dishesTask.setDate(System.currentTimeMillis());
        dishesTask.setSettid(3);
        tasklist.insert(TableTasklist.getContentValues(dishesTask));

        TableSettings settings = new TableSettings(db);
        settings.create();
        settings.insert(TableSettings.getContentValues(new Settings(0,"empty",0)));
        settings.insert(TableSettings.getContentValues(new Settings(1,"laundry",2)));
        settings.insert(TableSettings.getContentValues(new Settings(2,"shopping",0)));
        settings.insert(TableSettings.getContentValues(new Settings(3, "dishes", 1)));
        settings.insert(TableSettings.getContentValues(new Settings(4, "floor", 30)));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
