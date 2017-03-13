package com.dut.moneytracker.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */
public class DateTimeUtils {
    private static final String TAG = DateTimeUtils.class.getSimpleName();
    private static DateTimeUtils ourInstance = new DateTimeUtils();
    private static final String DEFAULT_DATE = "Hôm nay";
    DateFormat formatLongDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    DateFormat formatSortDate = new SimpleDateFormat("dd/MM", Locale.US);
    DateFormat formatSortTime = new SimpleDateFormat("HH:mm", Locale.US);

    public static DateTimeUtils getInstance() {
        return ourInstance;
    }

    private DateTimeUtils() {
    }

    public String getStringDate(Date date) {
        return formatLongDate.format(date);
    }
    public String getSortStringTime(Date date) {
        return formatSortTime.format(date);
    }

    public String getSortStringDate(Date date) {
        return formatSortDate.format(date);
    }

    public boolean isSameDate(Date date1, Date date2) {
        return TextUtils.equals(getStringDate(date1), getStringDate(date2));
    }

    public List<Date> getListLastDay(int numberDate) {
        Date today = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        List<Date> dates = new ArrayList<>();
        dates.add(calendar.getTime());
        for (int i = 0; i < numberDate - 1; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            dates.add(calendar.getTime());
        }
        return dates;
    }
}
