package com.shipuli.whattodo.fragments;


import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
public class TodoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, TabLifecycle{

    private Cursor mCursor;
    private TodoRecycleAdapter rAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this); //Initiate Loader
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.todo_fragment, container, false);

        //Initiate recycleview
        RecyclerView recycler = (RecyclerView) root.findViewById(R.id.todo_list_view);
        rAdapter = new TodoRecycleAdapter(this, mCursor, getContext());
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getActivity()){
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        recycler.setLayoutManager(lManager);
        recycler.scrollToPosition(0);
        recycler.setAdapter(rAdapter);

        //ItemTouchHelper that implements animation and functionality to delete by swiping
        //Src:https://github.com/nemanja-kovacevic/recycler-view-swipe-to-delete/blob/master/app/
        //    src/main/java/net/nemanjakovacevic/recyclerviewswipetodelete/MainActivity.java
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT){

            Drawable background;
            Drawable logo;
            Boolean initiated = false;
            final int margin = 10;

            private void init() {
                background = new ColorDrawable(Color.RED);
                logo = ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_24dp);
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                rAdapter.removeItem(viewHolder.getAdapterPosition());
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView r, RecyclerView.ViewHolder vh, float dX,
                                    float dY, int aState, boolean isActive) {
                View i = vh.itemView;
                if(vh.getAdapterPosition() == -1) {
                    return;
                }
                if(!initiated){
                    init();
                }

                //Draw red background
                int transparency = (int) dX / 3;
                transparency = transparency < 255 ? transparency : 255;
                background.setAlpha(transparency);
                background.setBounds(i.getLeft(), i.getTop() + margin , i.getLeft() + (int) dX,
                        i.getBottom() - margin);
                background.draw(c);

                //Draw trash can -icon
                int itemHeight = i.getBottom() - i.getTop();
                int intrinsicWidth = logo.getIntrinsicWidth();
                int intrinsicHeight = logo.getIntrinsicHeight();
                int tL = i.getLeft() + margin;
                int tR = i.getLeft() + margin + intrinsicWidth;
                int tT = i.getTop() + (itemHeight - intrinsicHeight)/2;
                int tB = tT + intrinsicHeight;
                logo.setBounds(tL, tT, tR, tB);
                logo.draw(c);

                //Draw Delete-text
                Paint pain = new Paint();
                int tS = 58;
                pain.setColor(Color.WHITE);
                pain.setTextSize(tS);
                c.drawText("Delete", tR + margin, i.getTop() + (itemHeight + (tS - margin)) / 2 , pain);

                super.onChildDraw(c, r, vh, dX, dY, aState, isActive);
            }

        });
        helper.attachToRecyclerView(recycler);

        return root;
    }

    //onResume function for tab layout
    public void onResumeFragment(){
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    /*
    The next 3 functions implement LoaderCallbacks
    onCreateLoader: Creates CursorLoader
    onLoadFinish: Updates data
    onLoaderReset: Updates view when Loader is reset
     */

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