package tiago.homemanagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TasklistDbTest {
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
    public void tasklistCRUDtest() {
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(getContext());

        SQLiteDatabase db = openHelper.getWritableDatabase();

        TableTasklist tableTasklist = new TableTasklist(db);

        TaskItem taskItem = new TaskItem();
        taskItem.setName("Sala");

        // Insert/create (C)RUD
        long id = newTaskItem(tableTasklist, taskItem);

        //query/read C(R)UD
        taskItem = ReadFirstTaskItem(tableTasklist,id,"Sala");

        //update CR(U)D
        taskItem.setName("Cozinha");
        int rowsAffected = tableTasklist.update(
                TableTasklist.getContentValues(taskItem),
                TableTasklist._ID + "=?",
                new String[] {Long.toString(id)}
        );

        assertEquals("Failed to update task",1,rowsAffected);

        //query/read C(R)UD
        taskItem = ReadFirstTaskItem(tableTasklist,id,"Cozinha");

        //delete CRU(D)
        rowsAffected = tableTasklist.delete(
                TableSettings._ID + "=?",
                new String[]{Long.toString(id)}
        );
        assertEquals("Failed to delete task",1,rowsAffected);

        Cursor cursor = tableTasklist.query(TableTasklist.ALL_COLUMNS,null,null,null,null,null);
        assertEquals("Categories found after deletion",0,cursor.getCount());
    }

    private long newTaskItem(TableTasklist tableTasklist, TaskItem taskItem){
        long id = tableTasklist.insert(
                TableTasklist.getContentValues(taskItem)
        );

        assertNotEquals("Failed to create task",-1,id);

        return id;
    }

    @NonNull
    private TaskItem ReadFirstTaskItem(TableTasklist tableTasklist, long expectedID, String expectedName) {
        Cursor cursor = tableTasklist.query(TableTasklist.ALL_COLUMNS, null, null, null, null, null);
        assertEquals("Failed to read tasklist", 1, cursor.getCount());

        assertTrue("Failed to read first task", cursor.moveToNext());

        TaskItem taskItem = TableTasklist.getCurrentTaskItem(cursor);

        assertEquals("Incorrect task id", expectedID, taskItem.getId());
        assertEquals("Incorrect task name", expectedName, taskItem.getName());
        return taskItem;
    }

    private Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }

}
