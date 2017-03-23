package com.dut.moneytracker.utils;

import android.text.TextUtils;

import com.dut.moneytracker.constant.TypeView;

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
    DateFormat formatFullDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    DateFormat formatDayMonth = new SimpleDateFormat("dd/MM", Locale.US);
    DateFormat formatMonthYear = new SimpleDateFormat("MM/yyyy", Locale.US);
    DateFormat formatYear = new SimpleDateFormat("yyyy", Locale.US);
    DateFormat formatSortTime = new SimpleDateFormat("HH:mm", Locale.US);

    public static DateTimeUtils getInstance() {
        return ourInstance;
    }

    private DateTimeUtils() {
    }

    public String getStringFullDate(Date date) {
        return formatFullDate.format(date);
    }

    public String getStringTime(Date date) {
        return formatSortTime.format(date);
    }

    public String getStringDayMonth(Date date) {
        return formatDayMonth.format(date);
    }

    public String getStringMonthYear(Date date) {
        return formatMonthYear.format(date);
    }

    public String getStringYear(Date date) {
        return formatYear.format(date);
    }

    public boolean isSameDate(Date date1, Date date2) {
        return TextUtils.equals(getStringFullDate(date1), getStringFullDate(date2));
    }

    public boolean isSameMonth(Date date1, Date date2) {
        return TextUtils.equals(getStringMonthYear(date1), getStringMonthYear(date2));
    }

    public boolean isSameYear(Date date1, Date date2) {
        return TextUtils.equals(getStringYear(date1), getStringYear(date2));
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
        //Collections.reverse(dates);
        return dates;
    }

    /**
     * @param date
     * @param type Date, Month, year
     * @param step
     * @return
     */
    public Date changeDateStep(Date date, int type, int step) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        switch (type) {
            case TypeView.DAY:
                calendar.add(Calendar.DAY_OF_MONTH, step);
                break;
            case TypeView.MONTH:
                calendar.add(Calendar.MONTH, step);
                break;
            case TypeView.YEAR:
                calendar.add(Calendar.YEAR, step);
                break;
            case TypeView.WEAK:
                break;

        }
        return calendar.getTime();
    }

    public List<Date> getCurrentDaysOfWeek(Calendar calendar) {
        List<Date> dates = new ArrayList<>();
        int index = calendar.get(GregorianCalendar.DAY_OF_WEEK);
        int step = index == 1 ? 7 : index - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -step);
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dates.add(calendar.getTime());
        }
        return dates;
    }
}
