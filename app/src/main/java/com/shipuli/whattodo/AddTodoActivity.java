package com.shipuli.whattodo;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shipuli.whattodo.database.TodoContentProvider;
import com.shipuli.whattodo.database.TodoTable;
import com.shipuli.whattodo.fragments.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        //Submit button setup
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues nTodo = new ContentValues();
                //process values
                nTodo.put(TodoTable.COLUMN_DESCRIPTION, addDesc.getText().toString().trim());
                SimpleDateFormat parser = new SimpleDateFormat("H:mm dd.MM.yyyy");
                parser.setLenient(false);
                long deadline;
                try {
                    deadline = parser.parse(addDead.getText().toString()).getTime();
                    nTodo.put(TodoTable.COLUMN_DEADLINE, deadline);
                }catch(ParseException e){
                    Log.d(getClass().getName(), "Unable to parse deadline.");
                    nTodo.put(TodoTable.COLUMN_DEADLINE, Long.MAX_VALUE);
                }
                //insert values
                getContentResolver().insert(TodoContentProvider.CONTENT_URI, nTodo);
                finish();
            }
        });

        //We do not want the keyboard to stay hanging so we change to the next field
        addDesc.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    hideSoftKeyboard(view);
                    addDead.requestFocus();
                    return true;
                }
                return false;
            }
        });
        addDesc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    hideSoftKeyboard(view);
                }
            }
        });
        LinearLayout l = (LinearLayout) addDesc.getParent();
        l.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideSoftKeyboard(view);
                return false;
            }
        });


        //Listeners to deadline field which open date picker dialog
        addDead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });
        addDead.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showDatePicker(view);
                }
            }
        });
        //User doesn't want the keyboard to show up
        addDead.setInputType(EditorInfo.TYPE_NULL);
    }

    public void showDatePicker(View v) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "datePicker");
    }

    public void hideSoftKeyboard(@NonNull View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


}
