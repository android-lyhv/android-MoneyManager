package com.dut.moneytracker.utils;

import android.text.TextUtils;
import android.util.Log;

import com.dut.moneytracker.constant.FilterType;

import java.text.DateFormat;
import java.text.ParseException;
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
    private static DateTimeUtils ourInstance;
    private static final String DEFAULT_DATE = "Hôm nay";
    private Locale mLocale;

    public static DateTimeUtils getInstance() {
        if (ourInstance == null) {
            ourInstance = new DateTimeUtils();
        }
        return ourInstance;
    }

    private DateTimeUtils() {
        mLocale = new Locale("Vietnam");
    }


    public String getStringFullDateVn(Date date) {
        DateFormat formatFullDate = new SimpleDateFormat("'ngày' dd 'tháng' M 'năm' yyyy", mLocale);
        if (isSameDate(date, new Date())) {
            return DEFAULT_DATE;
        }
        return formatFullDate.format(date);
    }

    public String getStringFullDateUs(Date date) {
        DateFormat formatFullDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        return formatFullDate.format(date);
    }

    public Date getDateFormat(String resource) {
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        try {
            return format.parse(resource);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public String getStringTime(Date date) {
        DateFormat formatSortTime = new SimpleDateFormat("HH:mm", mLocale);
        return formatSortTime.format(date);
    }

    public String getStringDayMonth(Date date) {
        DateFormat formatDayMonth = new SimpleDateFormat("'Ngày' dd 'tháng' MM", mLocale);
        return formatDayMonth.format(date);
    }

    public String getStringMonthYear(Date date) {
        DateFormat formatMonthYear = new SimpleDateFormat("'Tháng' MM 'năm' yyyy", mLocale);
        return formatMonthYear.format(date);
    }

    public String getStringDayMonthUs(Date date) {
        DateFormat formatDayMonth = new SimpleDateFormat("dd/MM", Locale.US);
        return formatDayMonth.format(date);
    }

    public String getStringDateUs(Date date) {
        DateFormat formatFullDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return formatFullDate.format(date);
    }

    public String getStringYear(Date date) {
        DateFormat formatYear = new SimpleDateFormat("'Năm' yyyy", Locale.US);
        return formatYear.format(date);
    }

    public boolean isSameDate(Date date1, Date date2) {
        return TextUtils.equals(getStringDateUs(date1), getStringDateUs(date2));
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
        return dates;
    }

    public Date getNextDate(Date currentDate, int step) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, step);
        return calendar.getTime();
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
            case FilterType.DAY:
                calendar.add(Calendar.DAY_OF_MONTH, step);
                break;
            case FilterType.MONTH:
                calendar.add(Calendar.MONTH, step);
                break;
            case FilterType.YEAR:
                calendar.add(Calendar.YEAR, step);
                break;
        }
        return calendar.getTime();
    }

    public Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public Date getDateRemindLoop(Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        return calendar.getTime();
    }

    public boolean isValidateFromDateToDate(Date fromDate, Date toDate) {
        return fromDate.before(toDate);
    }

    public Date setHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public Date setMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public Date[] geStartDateAndEndDate(Date date, int typeFilter) {
        Date[] dates = new Date[2];
        Date dateStart = new Date();
        Date dateEnd = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (typeFilter) {
            case FilterType.DAY:
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                dateStart = calendar.getTime();
                dateEnd = getEndDate(dateStart, typeFilter);
                break;
            case FilterType.MONTH:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                dateStart = calendar.getTime();
                dateEnd = getEndDate(dateStart, typeFilter);
                break;
            case FilterType.YEAR:
                calendar.set(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                dateStart = calendar.getTime();
                dateEnd = getEndDate(dateStart, typeFilter);
                break;
        }
        Log.d(TAG, "StartDate: " + dateStart);
        Log.d(TAG, "EndDate: " + dateEnd);
        dates[0] = dateStart;
        dates[1] = dateEnd;
        return dates;
    }

    private Date getEndDate(Date startDate, int typeFilter) {
        Calendar gregorianCalendar = new GregorianCalendar();
        switch (typeFilter) {
            case FilterType.DAY:
                gregorianCalendar.setTime(startDate);
                gregorianCalendar.add(Calendar.HOUR_OF_DAY, 24);
                return new Date(gregorianCalendar.getTimeInMillis() - 1);
            case FilterType.MONTH:
                gregorianCalendar.setTime(startDate);
                gregorianCalendar.add(Calendar.DAY_OF_MONTH, getMaxDaysOfMonth(startDate));
                return new Date(gregorianCalendar.getTimeInMillis() - 1);
            case FilterType.YEAR:
                gregorianCalendar.setTime(startDate);
                gregorianCalendar.add(Calendar.MONTH, 11);
                return new Date(gregorianCalendar.getTimeInMillis() - 1);
        }
        return new Date();
    }

    public int getMaxDaysOfMonth(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public Date getEndTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        Log.d(TAG, "getEndTimeOfDay: " + calendar.getTime());
        return calendar.getTime();
    }

    public Date getStartTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Log.d(TAG, "getStartTimeOfDay: " + calendar.getTime());
        return calendar.getTime();
    }

    public Date getTimeNotification(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
