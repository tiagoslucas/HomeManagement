package tiago.homemanagement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class TableSettings implements BaseColumns {
    public static final String TABLE_NAME = "homemanagement";
    public static final String TYPE_FIELD = "type";
    public static final String TIME_FIELD = "time";
    private SQLiteDatabase db;
    public static final String [] ALL_COLUMNS = new String [] {_ID,TYPE_FIELD,TIME_FIELD};

    public TableSettings(SQLiteDatabase db){
        this.db = db;
    }

    public void create(){
        db.execSQL("CREATE TABLE "+TABLE_NAME+
                "("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TYPE_FIELD+" TEXT NOT NULL,"+
                TIME_FIELD+" INTEGER NOT NULL)");
    }

    public long insert(ContentValues values) {
        return db.insert(TABLE_NAME, null, values);
    }

    public int update(ContentValues values, String whereClause, String[] whereArgs) {
        return db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    public int delete (String whereClause, String[] whereArgs) {
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        return db.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public static ContentValues getContentValues(Settings settings){
        ContentValues values = new ContentValues();
        values.put(TYPE_FIELD, settings.getType());
        return values;
    }

    public static Settings getCurrentSettings(Cursor cursor){
        final int idPosition = cursor.getColumnIndex(_ID);
        final int typePosition = cursor.getColumnIndex(TYPE_FIELD);
        final int timePosition = cursor.getColumnIndex(TIME_FIELD);

        Settings item = new Settings();
        item.setId(cursor.getInt(idPosition));
        item.setType(cursor.getString(typePosition));
        item.setTime(cursor.getInt(timePosition));
        return item;
    }
}
