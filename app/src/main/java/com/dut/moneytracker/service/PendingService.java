package com.dut.moneytracker.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 25/04/2017.
 */
public class PendingService {
    private static PendingService ourInstance;
    private static final long TIME_REPEAT = 24 * 60 * 60 * 1000L;
    private static final int ID = 1;

    public static PendingService getInstance() {
        if (ourInstance == null) {
            ourInstance = new PendingService();
        }
        return ourInstance;
    }

    private PendingService() {
    }

    /**
     * for check pending loopExchange, alarm debit
     *
     * @param context
     */
    public void actionLoopPending(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, ReceivePending.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getStartTime(), TIME_REPEAT, pendingIntent);
    }

    public void removePending(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, ReceivePending.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    /**
     * 6:00 every days
     *
     * @return
     */
    private long getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
