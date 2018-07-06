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

    //Database Manager code
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

}
