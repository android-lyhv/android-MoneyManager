package com.dut.moneytracker.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by HaloSumiu on 4/23/2016.
 */
public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public interface TimePickerListener {
        void onResultHour(int hour);

        void onResultMinute(int minute);

        void onResultStringTime(String time);
    }

    public TimePickerListener timePickerListener;

    public void registerPicker(TimePickerListener timePickerListener) {
        this.timePickerListener = timePickerListener;
    }

    public TimePicker() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getContext(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        timePickerListener.onResultHour(hourOfDay);
        timePickerListener.onResultMinute(minute);
        String stringHour = hourOfDay < 10 ? String.format(Locale.US, "0%s", hourOfDay) : String.valueOf(hourOfDay);
        String stringMinute = minute < 10 ? String.format(Locale.US, "0%s", minute) : String.valueOf(minute);
        String time = String.format(Locale.US, "%s:%s", stringHour, stringMinute);
        timePickerListener.onResultStringTime(time);
    }
}
