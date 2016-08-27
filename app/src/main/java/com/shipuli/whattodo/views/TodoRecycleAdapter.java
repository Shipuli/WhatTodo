package com.shipuli.whattodo.views;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shipuli.whattodo.R;
import com.shipuli.whattodo.database.TodoTable;

/**
 * Adapter for RecycleView
 */
public class TodoRecycleAdapter extends RecycleCursorAdapter<TodoRecycleAdapter.TodoHolder> {

    public TodoRecycleAdapter(Cursor c) {
        super(c);
    }

    //ViewHolder for TodoRecycleView
    public static class TodoHolder extends RecyclerView.ViewHolder {
        private final TextView description;

        public TodoHolder(View v) {
            super(v);
            description = (TextView) v.findViewById(R.id.todo_description);
        }

        public void bindTodo(Cursor todo) {
            description.setText(todo.getString(todo.getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION)));
        }
    }

    @Override
    public TodoHolder onCreateViewHolder(ViewGroup container, int vtype) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.todo_fragment_list_item,
                container, false);
        return new TodoHolder(v);
    }

    public void onBindViewHolder(TodoHolder vh, Cursor c) {
        vh.bindTodo(c);
    }

}
