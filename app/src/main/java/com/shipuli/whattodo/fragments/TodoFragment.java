package com.shipuli.whattodo.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shipuli.whattodo.R;
import com.shipuli.whattodo.database.TodoContentProvider;
import com.shipuli.whattodo.views.TodoRecycleAdapter;

/**
 * Fragment which displays todo list.
 */
public class TodoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    Cursor mCursor;
    TodoRecycleAdapter rAdapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.todo_fragment, container, false);
        recycler = (RecyclerView) root.findViewById(R.id.recycler_list_view);
        rAdapter = new TodoRecycleAdapter(mCursor);
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        recycler.scrollToPosition(0);
        recycler.setAdapter(rAdapter);

        return root;
    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), TodoContentProvider.CONTENT_URI,
                null, null, null, null);

    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        rAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        rAdapter.changeCursor(null);
    }
}
