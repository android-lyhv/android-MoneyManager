package com.dut.moneytracker.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by HaloSumiu on 4/23/2016.
 */
public class DayPicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public interface DatePickerListener {
        void onResultYear(int year);

        void onResultMonthOfYear(int month);

        void onResultDayOfMonth(int day);

        void onResultStringDate(String Date);
    }

    public DatePickerListener datePickerListener;

    public void registerPicker(DatePickerListener datePickerListener) {
        this.datePickerListener = datePickerListener;
    }

    public DayPicker() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear+1;
        datePickerListener.onResultYear(year);
        datePickerListener.onResultMonthOfYear(month);
        datePickerListener.onResultDayOfMonth(dayOfMonth);
        String stringMonth = month < 10 ? String.format(Locale.US, "0%s", month) : String.valueOf(month);
        String stringDay = dayOfMonth < 10 ? String.format(Locale.US, "0%s", dayOfMonth) : String.valueOf(dayOfMonth);
        String date = String.format(Locale.US, "%s/%s/%s", stringDay, stringMonth, year);
        datePickerListener.onResultStringDate(date);
    }
}
