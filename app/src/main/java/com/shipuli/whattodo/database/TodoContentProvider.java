package com.shipuli.whattodo.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * WhatTodo apps Content Provider.
 * Implements CRUD functionality to todos that haven't been completed yet.
 */

public class TodoContentProvider extends ContentProvider{

    private TodoSQLHelper mHelper;

    //URI matching codes
    private static final int ALL = 1;
    private static final int SINGLE = 2;

    //Paths
    private static final String AUTHORITY = "com.shipuli.whattodo.todoprovider";
    private static final String BASE_PATH = "todo";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final UriMatcher uMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uMatcher.addURI(AUTHORITY, BASE_PATH, ALL);
        uMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SINGLE);
    }

    public boolean onCreate() {
        mHelper = new TodoSQLHelper(getContext());
        return true;
    }

    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor output;
        switch (uMatcher.match(uri)) {
            case ALL:
                output = db.query(TodoTable.TABLE_NAME, projection, null, null, null, null, sortOrder);
                break;
            case SINGLE:
                selection = addWhere(uri, selection);
                output = db.query(TodoTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return output;
    }

    public Uri insert(@NonNull Uri uri, ContentValues data) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id;
        switch (uMatcher.match(uri)) {
            case ALL:
                id = db.insert(TodoTable.TABLE_NAME, null, data);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return Uri.parse(BASE_PATH + "/" + id);
    }


    public int update(@NonNull Uri uri, ContentValues data, String selection, String[] selectionArgs){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int c;
        switch (uMatcher.match(uri)) {
            case ALL:
                c = db.update(TodoTable.TABLE_NAME, data, selection, selectionArgs);
                break;
            case SINGLE:
                selection = addWhere(uri, selection);
                c = db.update(TodoTable.TABLE_NAME, data, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return c;
    }

    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int c;
        switch (uMatcher.match(uri)) {
            case ALL:
                c = db.delete(TodoTable.TABLE_NAME, selection, selectionArgs);
                break;
            case SINGLE:
                selection = addWhere(uri, selection);
                c = db.delete(TodoTable.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return c;
    }

    public String getType(@NonNull Uri uri) {
        switch (uMatcher.match(uri)) {
            case ALL:
                return "vnd.android.cursor.dir/vnd.com.shipuli.provider.todo";
            case SINGLE:
                return "vnd.android.cursor.item/vnd.com.shipuli.provider.todo";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private String addWhere(Uri uri, String selection){
        String out;
        String where = TodoTable.COLUMN_ID + '=' + uri.getLastPathSegment();
        if(selection == null) {
            out = where;
        }else{
            out = selection + " OR " + where;
        }
        return out;
    }
}
