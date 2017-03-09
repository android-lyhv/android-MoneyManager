package com.dut.moneytracker.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */
public class DateTimeUtils {
    private static DateTimeUtils ourInstance = new DateTimeUtils();

    public static DateTimeUtils getInstance() {
        return ourInstance;
    }

    private DateTimeUtils() {
    }

    public String getStringDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return dateFormat.format(date);
    }
}
