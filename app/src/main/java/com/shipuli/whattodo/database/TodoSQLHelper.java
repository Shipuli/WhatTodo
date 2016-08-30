package com.shipuli.whattodo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SQLiteHelper for handling active todos.
 * Implements methods to create and upgrade the database and defines constants needed for
 * using it.
 */
public class TodoSQLHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Todo";

    TodoSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoTable.TODO_TABLE_CREATE);
        db.execSQL(CompletedTable.COMPLETED_TABLE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        Log.w(TodoSQLHelper.class.getName(), "Upgrading database from version " +  oldV + " to "
                + newV  + ". All data is lost.");
        db.execSQL("DROP TABLE IF EXISTS " + TodoTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CompletedTable.TABLE_NAME);
        onCreate(db);
    }
}
