package com.shipuli.whattodo.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shipuli.whattodo.R;
import com.shipuli.whattodo.database.CompletedTable;
import com.shipuli.whattodo.database.TodoContentProvider;
import com.shipuli.whattodo.views.CompletedRecycleAdapter;

/**
 * Fragment to display Completed list.
 */
public class CompletedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
TabLifecycle{
    private Cursor mCursor;
    private CompletedRecycleAdapter rAdapter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.completed_fragment, container, false);
        RecyclerView recycler = (RecyclerView) root.findViewById(R.id.completed_list_view);
        rAdapter = new CompletedRecycleAdapter(this, mCursor, getContext());
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getActivity()){
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        recycler.setLayoutManager(lManager);
        recycler.scrollToPosition(0);
        recycler.setAdapter(rAdapter);

        return root;
    }

    //Lifecycle-method that works in tab layout
    public void onResumeFragment(){
        getLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(1, null, this);
    }

    /*
    The next 3 functions implement LoaderCallbacks
    onCreateLoader: Creates CursorLoader
    onLoadFinish: Updates data
    onLoaderReset: Updates view when Loader is reset
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), TodoContentProvider.CONTENT_URI2,
                null, null, null, CompletedTable.COLUMN_FINISHED + " DESC");

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
