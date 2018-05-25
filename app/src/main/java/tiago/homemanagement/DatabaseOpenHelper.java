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
        TableSettings settings = new TableSettings(db);
        tasklist.create();
        settings.create();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
