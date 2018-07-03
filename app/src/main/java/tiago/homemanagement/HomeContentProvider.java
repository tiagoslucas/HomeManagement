package tiago.homemanagement;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class HomeContentProvider extends ContentProvider {
    private static final String AUTHORITY = "tiago.homemanagement";

    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri SETTINGS_URI = Uri.withAppendedPath(BASE_URI, TableSettings.TABLE_NAME);
    public static final Uri TASKLIST_URI = Uri.withAppendedPath(BASE_URI, TableTasklist.TABLE_NAME);

    private static final int SETTINGS = 100;
    private static final int SETTINGS_ID = 101;
    private static final int TASKLIST = 200;
    private static final int TASKLIST_ID = 201;


    private static final String MULTIPLE_ITEMS = "vnd.android.cursor.dir";
    private static final String SINGLE_ITEM = "vnd.android.cursor.item";

    DatabaseOpenHelper openHelper;

    private static UriMatcher matchUri(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, "Settings", 100);
        uriMatcher.addURI(AUTHORITY, "Settings/#", 101);

        uriMatcher.addURI(AUTHORITY, "Tasklist", 100);
        uriMatcher.addURI(AUTHORITY, "Tasklist/#", 101);

        return uriMatcher;
    }

    @Override
    public boolean onCreate(){
        openHelper = new DatabaseOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String id = uri.getLastPathSegment();
        UriMatcher matcher = matchUri();

        switch (matcher.match(uri)){
            case SETTINGS:
                return new TableSettings(db).query(projection, selection, selectionArgs, null, null, sortOrder);

            case TASKLIST:
                return new TableTasklist(db).query(projection, selection, selectionArgs, null, null, sortOrder);

            case SETTINGS_ID:
                return new TableSettings(db).query(projection, TableSettings._ID + "=?", new String[] { id }, null, null, null);

            case TASKLIST_ID:
                return new TableTasklist(db).query(projection, TableTasklist._ID + "=?", new String[] { id }, null, null, null);

            default:
                throw new UnsupportedOperationException("Invalid URI: " + uri);
        }
    }

    public String getType(@NonNull Uri uri){
        UriMatcher matcher = matchUri();

        switch (matcher.match(uri)){
            case SETTINGS:
                return MULTIPLE_ITEMS + "/" + AUTHORITY + "/" + TableSettings.TABLE_NAME;

            case TASKLIST:
                return MULTIPLE_ITEMS + "/" + AUTHORITY + "/" + TableTasklist.TABLE_NAME;

            case SETTINGS_ID:
                return SINGLE_ITEM + "/" + AUTHORITY + "/" + TableSettings.TABLE_NAME;

            case TASKLIST_ID:
                return SINGLE_ITEM + "/" + AUTHORITY + "/" + TableTasklist.TABLE_NAME;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    public Uri insert(@NonNull Uri uri, ContentValues values){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        UriMatcher matcher = matchUri();

        long id = -1;
        switch (matcher.match(uri)){
            case SETTINGS:
                id = new TableSettings(db).insert(values);
                break;

            case TASKLIST:
                id = new TableTasklist(db).insert(values);
                break;

            default:
                throw new UnsupportedOperationException("Invalide URI: " + uri);
        }

        if (id > 0) {
            notifyChanges(uri);
            return Uri.withAppendedPath(uri, Long.toString(id));
        } else {
            throw new SQLException("Could not insert record");
        }
    }

    private void notifyChanges(@NonNull Uri uri){
        getContext().getContentResolver().notifyChange(uri, null);
    }

    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        UriMatcher matcher = matchUri();
        String id = uri.getLastPathSegment();
        int rows = 0;

        switch (matcher.match(uri)){
            case SETTINGS_ID:
                rows = new TableSettings(db).delete(TableSettings._ID + "=?", new String[] { id });
                break;

            case TASKLIST_ID:
                rows = new TableTasklist(db).delete(TableTasklist._ID + "=?", new String[] { id });
                break;

            default:
                throw new UnsupportedOperationException("Invalid URI: " + uri);
        }

        if (rows > 0) notifyChanges(uri);
        return rows;
    }

    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        UriMatcher matcher = matchUri();
        String id = uri.getLastPathSegment();
        int rows = 0;

        switch (matcher.match(uri)){
            case SETTINGS_ID:
                rows = new TableSettings(db).update(values, TableSettings._ID + "=?", new String[] { id });
                break;

            case TASKLIST_ID:
                rows = new TableTasklist(db).update(values, TableTasklist._ID + "=?", new String[] { id });
                break;

            default:
                throw new UnsupportedOperationException("Invalid URI: " + uri);
        }

        if (rows > 0) notifyChanges(uri);

        return rows;
    }
}