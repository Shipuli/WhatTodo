package com.shipuli.whattodo.views;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shipuli.whattodo.R;
import com.shipuli.whattodo.database.CompletedTable;
import com.shipuli.whattodo.database.TodoContentProvider;
import com.shipuli.whattodo.database.TodoTable;
import com.shipuli.whattodo.fragments.TodoFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Adapter for RecycleView
 */
public class TodoRecycleAdapter extends RecycleCursorAdapter<TodoRecycleAdapter.TodoHolder> {

    private final TodoFragment mContainer;
    private final String[] compliments = {
            "Great Job!",
            "Good going!",
            "Ayyyyyyyy!",
            "That's the spirit!",
            "Getting stuff done!"
    };

    public TodoRecycleAdapter(TodoFragment container, Cursor c, Context context) {
        super(context, c);
        mContainer = container;
    }

    //ViewHolder for TodoRecycleView
    public static class TodoHolder extends RecyclerView.ViewHolder{
        private final TextView description;
        private final TextView deadline;
        private final ImageButton completeButton;

        public TodoHolder(View v) {
            super(v);
            description = (TextView) v.findViewById(R.id.todo_description);
            deadline = (TextView) v.findViewById(R.id.todo_deadline);
            completeButton = (ImageButton) v.findViewById(R.id.complete_todo);
        }

        public void bindTodo(Cursor todo) {
            description.setText(todo.getString(todo.getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION)));
            Date present = new Date();
            long dedis = todo.getLong(todo.getColumnIndexOrThrow(TodoTable.COLUMN_DEADLINE));
            if(dedis != Long.MAX_VALUE) {
                long until = ( dedis - present.getTime() ) / DateUtils.MINUTE_IN_MILLIS;
                if(until < 0){
                    deadline.setTextColor(ContextCompat.getColor(mContext, R.color.colorRemove));
                    deadline.setText("Deadline: Missed");
                    return;
                }
                String remaining = timeRemaining(until);
                if(remaining.contains("d")){
                    deadline.setTextColor(ContextCompat.getColor(mContext, R.color.colorComplete));
                }else if(remaining.contains("h")){
                    deadline.setTextColor(ContextCompat.getColor(mContext, R.color.colorMedium));
                }else{
                    deadline.setTextColor(ContextCompat.getColor(mContext, R.color.colorRemove));
                }
                deadline.setText("Deadline: " + remaining);
            }else{
                deadline.setTextColor(ContextCompat.getColor(mContext, R.color.colorComplete));
                deadline.setText("No hurry!");
            }
        }
        //Helper function that constructs the remaining string
        public String timeRemaining(long until) {
            String date = "";
            date += (until % 60) + "min";
            until /= 60;
            if(until >= 1) {
                date = (until % 24) + "h " + date;
                until /= 24;
                if(until >= 1) {
                    date = (until % 30) + "d " + date;
                    until /= 30;
                    if(until >= 1) {
                        date = (until % 365) + "y " + date;
                    }
                }
            }
            return date;
        }
    }

    //Function that deletes item permanently
    public void removeItem(int pos) {
        int real = (int) getItemId(pos);
        mContext.getContentResolver().delete(Uri.parse(TodoContentProvider.CONTENT_URI + "/" + real),
                null, null);
        mContainer.getLoaderManager().restartLoader(0, null, mContainer);
    }

    //Function that makes item completed
    private void completeItem(int pos) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        Cursor inserted = getCursor();
        int real = (int) getItemId(pos);
        inserted.moveToPosition(pos);
        Date present = new Date();

        //Database transaction that deletes item from TodoTable and inserts it to CompleteTable
        ops.add(ContentProviderOperation.newDelete(Uri.parse(TodoContentProvider.CONTENT_URI +
                "/" + real)).build());
        ops.add(ContentProviderOperation.newInsert(TodoContentProvider.CONTENT_URI2)
                .withValue(CompletedTable.COLUMN_DESCRIPTION, inserted.getString(
                        inserted.getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION)))
                .withValue(CompletedTable.COLUMN_FINISHED, present.getTime()).build());

        try {
            mContext.getContentResolver().applyBatch(TodoContentProvider.AUTHORITY, ops);
            String t = compliments[new Random().nextInt(compliments.length)];
            Toast toast = Toast.makeText(mContext, t, Toast.LENGTH_SHORT);
            toast.show();
        }catch (RemoteException e){

        }catch (OperationApplicationException e){

        }
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
        vh.completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeItem(pos);
            }
        });
    }

}
