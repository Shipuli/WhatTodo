package com.shipuli.whattodo.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import com.shipuli.whattodo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 5.9.2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                android.text.format.DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker v, int hour, int minute) {
        Bundle date = getArguments();
        Calendar out = Calendar.getInstance();
        out.set(Calendar.YEAR, date.getInt("year"));
        out.set(Calendar.MONTH, date.getInt("month"));
        out.set(Calendar.DAY_OF_MONTH, date.getInt("day"));
        out.set(Calendar.HOUR_OF_DAY, hour);
        out.set(Calendar.MINUTE, minute);
        SimpleDateFormat formatter = new SimpleDateFormat("H:mm dd.MM.yyyy");
        TextView tV = (TextView) getActivity().findViewById(R.id.add_deadline);
        tV.setText(formatter.format(out.getTime()));
    }
}
