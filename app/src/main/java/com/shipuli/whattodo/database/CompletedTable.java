package com.shipuli.whattodo.database;

/**
 * Class that defines Completed tables constants
 */
public class CompletedTable {
    public static final String TABLE_NAME = "completed";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_FINISHED = "finished";
    public static final String COMPLETED_TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_FINISHED + " INTEGER);";
}
