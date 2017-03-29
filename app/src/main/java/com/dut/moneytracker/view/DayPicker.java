package com.dut.moneytracker.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.dut.moneytracker.utils.DateTimeUtils;

import org.androidannotations.annotations.EFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Copyright @2016 AsianTech Inc.
 * Created by HaloSumiu on 4/23/2016.
 */
@EFragment
public class DayPicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public interface DatePickerListener {
        void onResultDate(Date date);
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
        datePickerListener.onResultDate(DateTimeUtils.getInstance().getDate(year, monthOfYear, dayOfMonth));
    }
}
