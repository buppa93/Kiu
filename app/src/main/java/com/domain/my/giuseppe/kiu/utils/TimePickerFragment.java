package com.domain.my.giuseppe.kiu.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by giuseppe on 17/05/16.
 */
@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    TextView timeTxtView;

    public TimePickerFragment(View v)
    {
        timeTxtView = (TextView) v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        String time;
        if(hourOfDay<10)
            time = "0"+hourOfDay+":";
        else
            time = hourOfDay+":";
        if(minute<10)
            time = time + "0";
        time = time + minute;
        timeTxtView.setText(time);
    }
}
