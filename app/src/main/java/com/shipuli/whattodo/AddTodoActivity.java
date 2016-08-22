package com.shipuli.whattodo;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shipuli.whattodo.database.TodoContentProvider;
import com.shipuli.whattodo.database.TodoTable;

/**
 * Activity used to display the "add todo" form.
 */
public class AddTodoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo);

        Button btn = (Button) findViewById(R.id.add_button);
        final EditText addDesc = (EditText) findViewById(R.id.add_description);
        final EditText addDead = (EditText) findViewById(R.id.add_deadline);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues nTodo = new ContentValues();

                nTodo.put(TodoTable.COLUMN_DESCRIPTION, addDesc.getText().toString());
                nTodo.put(TodoTable.COLUMN_DEADLINE, addDead.getText().toString());

                Uri nUri = getContentResolver().insert(TodoContentProvider.CONTENT_URI, nTodo);
                finish();
            }
        });
    }

}
