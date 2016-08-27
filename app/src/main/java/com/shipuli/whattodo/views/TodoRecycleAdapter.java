package com.shipuli.whattodo.views;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shipuli.whattodo.R;
import com.shipuli.whattodo.database.TodoContentProvider;
import com.shipuli.whattodo.database.TodoTable;
import com.shipuli.whattodo.fragments.TodoFragment;

/**
 * Adapter for RecycleView
 */
public class TodoRecycleAdapter extends RecycleCursorAdapter<TodoRecycleAdapter.TodoHolder> {

    TodoFragment mContainer;

    public TodoRecycleAdapter(TodoFragment container, Cursor c, Context context) {
        super(context, c);
        mContainer = container;
    }

    //ViewHolder for TodoRecycleView
    public static class TodoHolder extends RecyclerView.ViewHolder{
        private final TextView description;
        private final ImageButton deleteButton;

        public TodoHolder(View v) {
            super(v);
            description = (TextView) v.findViewById(R.id.todo_description);
            deleteButton = (ImageButton) v.findViewById(R.id.delete_todo);
        }

        public void bindTodo(Cursor todo) {
            description.setText(todo.getString(todo.getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION)));
        }
    }

    public void removeItem(int pos) {
        int real = (int) getItemId(pos);
        mContext.getContentResolver().delete(Uri.parse(TodoContentProvider.CONTENT_URI + "/" + real),
                null, null);
        mContainer.getLoaderManager().restartLoader(0, null, mContainer);
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup container, int vtype) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.todo_fragment_list_item,
                container, false);
        return new TodoHolder(v);
    }

    public void onBindViewHolder(TodoHolder vh, Cursor c, final int pos) {
        vh.bindTodo(c);
        vh.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(pos);
            }
        });
    }

}
