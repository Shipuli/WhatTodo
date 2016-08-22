package com.shipuli.whattodo.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shipuli.whattodo.R;
import com.shipuli.whattodo.database.TodoContentProvider;
import com.shipuli.whattodo.database.TodoTable;

/**
 * Fragment which displays todo list.
 */
public class TodoFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    CursorAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new CursorAdapter(getActivity(), null, 2) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.todo_fragment_list_item, parent,
                        false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView addDesc = (TextView) view.findViewById(R.id.todo_description);
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION));
                addDesc.setText(desc);
            }
        };
        this.setListAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);

    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), TodoContentProvider.CONTENT_URI,
                null, null, null, null);

    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader loader) {
        mAdapter.swapCursor(null);
    }
}
