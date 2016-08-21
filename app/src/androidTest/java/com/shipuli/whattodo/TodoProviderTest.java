package com.shipuli.whattodo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.util.Log;

import com.shipuli.whattodo.database.TodoContentProvider;
import com.shipuli.whattodo.database.TodoTable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Tests for TodoContentProvider
 */

@RunWith(AndroidJUnit4.class)
public class TodoProviderTest extends ProviderTestCase2<TodoContentProvider> {

    public TodoProviderTest() {
        super(TodoContentProvider.class, "com.shipuli.whattodo.todoprovider");
    }

    private ContentResolver resolver;

    @Before
    @Override
    public void setUp() throws Exception {
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
        resolver = getMockContentResolver();

        ContentValues nTodo = new ContentValues();
        nTodo.put(TodoTable.COLUMN_DESCRIPTION, "mock data");
        nTodo.put(TodoTable.COLUMN_DEADLINE, "mock deadline");
        Uri nUri = resolver.insert(TodoContentProvider.CONTENT_URI, nTodo);
    }

    @Test
    public void insertTest() {
        ContentValues nTodo = new ContentValues();

        nTodo.put(TodoTable.COLUMN_DESCRIPTION, "Testi 1");
        nTodo.put(TodoTable.COLUMN_DEADLINE, "Testi 2");

        Uri nUri = resolver.insert(TodoContentProvider.CONTENT_URI, nTodo);
        assertTrue(nUri != null);
        Log.d(TodoProviderTest.class.getName(), nUri.toString());
    }

    @Test
    public void queryTest() {
        String[] projection = {TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_DEADLINE};
        String selection = TodoTable.COLUMN_DESCRIPTION + " = ?";
        String[] selArgs = {"mock data"};
        Cursor results = resolver.query(TodoContentProvider.CONTENT_URI, projection, selection,
                selArgs, null);
        assertTrue(results != null);
        assertTrue(results.getCount() > 0);
        results.moveToFirst();
        String resDesc = results.getString(results.getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION));
        Log.d(TodoProviderTest.class.getName(), "This came out: " + resDesc);
        assertEquals("mock data", resDesc);
        results.close();
    }
}
