package com.shipuli.whattodo.views;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.RecyclerView;

/**
 * Abstract adapter for linking RecycleView to Cursors
 */
public abstract class RecycleCursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private Cursor mCursor;
    static Context mContext;
    private Boolean mDataValid;
    private int mRowId;

    public RecycleCursorAdapter(Context context, Cursor c) {
        mContext = context;
        mCursor = c;
        mDataValid = mCursor != null;
        mRowId = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        setHasStableIds(true);
    }

    public Cursor getCursor(){
        return mCursor;
    }

    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor, int pos);

    @Override
    public void onBindViewHolder(VH viewHolder, int pos) {
        if(!mDataValid){
            throw new IllegalStateException("RecycleCursorAdapter doesn't have cursor.");
        }
        mCursor.moveToPosition(pos);
        onBindViewHolder(viewHolder, mCursor, pos);
    }

    @Override
    public int getItemCount() {
        return mDataValid ? mCursor.getCount() : 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int pos) {
        if(mDataValid && mCursor.moveToPosition(pos)){
            return mCursor.getLong(mRowId);
        }
        return 0;
    }

    public void changeCursor(Cursor c) {
        Cursor old = swapCursor(c);
        if(old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor c) {
        if(c == mCursor){
            return null;
        }
        final Cursor out = mCursor;
        mCursor = c;
        if(mCursor != null){
            mRowId = mCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        }else{
            mRowId = -1;
            mDataValid = false;
        }
        return out;
    }

}
