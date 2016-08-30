package com.shipuli.whattodo.views;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shipuli.whattodo.R;
import com.shipuli.whattodo.database.CompletedTable;
import com.shipuli.whattodo.fragments.CompletedFragment;

/**
 * Adapter for Complete Fragment
 */
public class CompletedRecycleAdapter extends RecycleCursorAdapter<CompletedRecycleAdapter.CompletedHolder> {
    private final CompletedFragment mContainer;

    public CompletedRecycleAdapter(CompletedFragment container, Cursor c, Context context) {
        super(context, c);
        mContainer = container;
    }

    public class CompletedHolder extends RecyclerView.ViewHolder {
        private final TextView description;
        public CompletedHolder(View v) {
            super(v);
            description = (TextView) v.findViewById(R.id.completed_description);
        }

        public void bindCompleted(Cursor c) {
            description.setText(c.getString(c.getColumnIndexOrThrow(CompletedTable.COLUMN_DESCRIPTION)));
        }
    }

    @Override
    public CompletedHolder onCreateViewHolder(ViewGroup container, int type) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.completed_fragment_list_item,
                container, false);
        return new CompletedHolder(v);
    }

    public void onBindViewHolder(CompletedHolder vh, Cursor c, int pos) {
        vh.bindCompleted(c);
    }

}
