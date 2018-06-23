package tiago.homemanagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.channels.SeekableByteChannel;
import java.util.function.LongToIntFunction;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SettingsDbTest {

    public static final String FIRST_TYPE = "laundry";
    public static final String SECOND_TYPE = "dishes";

    @Before
    public void setUp() {
        getContext().deleteDatabase(DatabaseOpenHelper.DATABASE_NAME);
    }

    @Test
    public void openDatabaseTest() {
        // Context of the app under test.
        Context appContext = getContext();

        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(appContext);

        SQLiteDatabase db = openHelper.getReadableDatabase();
        assertTrue("Could not create database", db.isOpen());
        db.close();
    }

    @Test
    public void settingsCRUDtest() {
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(getContext());

        SQLiteDatabase db = openHelper.getWritableDatabase();

        TableSettings tableSettings = new TableSettings(db);

        Settings setting = new Settings();
        setting.setType(FIRST_TYPE);
        setting.setTime((int) (System.currentTimeMillis()/1000l));

        // Insert/create (C)RUD
        long id = newSetting(tableSettings, setting);

        //query/read C(R)UD
        setting = ReadFirstSetting(tableSettings,FIRST_TYPE,id);

        //update CR(U)D
        setting.setType(SECOND_TYPE);
        int rowsAffected = tableSettings.update(
                TableSettings.getContentValues(setting),
                TableSettings._ID + "=?",
                new String[] {Long.toString(id)}
        );

        assertEquals("Failed to update setting",1,rowsAffected);

        //query/read C(R)UD
        setting = ReadFirstSetting(tableSettings,SECOND_TYPE,id);

        //delete CRU(D)
        rowsAffected = tableSettings.delete(
                TableSettings._ID + "=?",
                new String[]{Long.toString(id)}
        );
        assertEquals("Failed to delete setting",1,rowsAffected);

        Cursor cursor = tableSettings.query(TableSettings.ALL_COLUMNS,null,null,null,null,null);
        assertEquals("Categories found after deletion",0,cursor.getCount());
    }

    private long newSetting(TableSettings tableSettings, Settings setting){
        long id = tableSettings.insert(
                        TableSettings.getContentValues(setting)
        );

        assertNotEquals("Failed to create setting",-1,id);

        return id;
    }

    @NonNull
    private Settings ReadFirstSetting(TableSettings tableSettings, String expectedType, long expectedID) {
        Cursor cursor = tableSettings.query(TableSettings.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Failed to read settings", 1, cursor.getCount());

        assertTrue("Failed to read first setting", cursor.moveToNext());

        Settings setting = TableSettings.getCurrentSettings(cursor);
        assertEquals("Incorrect setting type", expectedType, setting.getType());
        assertEquals("Incorrect setting id", expectedID, setting.getId());
        return setting;
    }

    private Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }
}
