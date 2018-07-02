package tiago.homemanagement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class TableTasklist implements BaseColumns {
    public static final String TABLE_NAME = "Tasklist";
    public static final String NAME_FIELD = "name";
    public static final String DONE_FIELD = "done";
    public static final String DATE_FIELD = "date";
    public static final String SETTING_FIELD = "settid";
    private SQLiteDatabase db;
    public static final String [] ALL_COLUMNS = new String [] {_ID,NAME_FIELD,DONE_FIELD,DATE_FIELD,SETTING_FIELD};

    public TableTasklist(SQLiteDatabase db){
        this.db = db;
    }

    public void create() {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME_FIELD + " TEXT NOT NULL," +
                DONE_FIELD + " INTEGER," +
                DATE_FIELD + " TEXT," +
                SETTING_FIELD + " INTEGER," +
                "FOREIGN KEY (" + SETTING_FIELD + ") REFERENCES " +
                    TableSettings.TABLE_NAME +
                        "(" + TableSettings._ID + ")" +
                ")"
        );
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

    public static TaskItem getCurrentTaskItem(Cursor cursor){
        final int idPosition = cursor.getColumnIndex(_ID);
        final int namePosition = cursor.getColumnIndex(NAME_FIELD);
        final int donePosition = cursor.getColumnIndex(DONE_FIELD);
        final int datePosition = cursor.getColumnIndex(DATE_FIELD);
        final int settidPosition = cursor.getColumnIndex(SETTING_FIELD);

        TaskItem item = new TaskItem();
        item.setId(cursor.getInt(idPosition));
        item.setName(cursor.getString(namePosition));
        item.setDone(cursor.getInt(donePosition));
        item.setDate(cursor.getString(datePosition));
        item.setSettid(cursor.getInt(settidPosition));
        return item;
    }

    public static ContentValues getContentValues(TaskItem taskItem) {
        ContentValues values = new ContentValues();
        values.put(NAME_FIELD, taskItem.getName());
        values.put(DONE_FIELD, taskItem.isDone());
        values.put(DATE_FIELD, taskItem.getDate());
        values.put(SETTING_FIELD, taskItem.getDate());
        return values;
    }
}