package com.dut.moneytracker.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dut.moneytracker.R;

import static android.content.Context.ALARM_SERVICE;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 25/04/2017.
 */
public class AlarmPending {
    private static AlarmPending ourInstance;
    private static final long TIME_REPEAT = 60 * 1000L;
    private static final int ID = 2722;

    public static AlarmPending getInstance() {
        if (ourInstance == null) {
            ourInstance = new AlarmPending();
        }
        return ourInstance;
    }

    private AlarmPending() {
    }

    /**
     * for check pending loopExchange, alarm debit
     *
     * @param context
     */
    public void startPendingReceive(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, ReceivePending.class);
        intent.setAction(context.getString(R.string.pending));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TIME_REPEAT, pendingIntent);
    }

    public void removePendingReceive(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, ReceivePending.class);
        intent.setAction(context.getString(R.string.pending));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public void startPendingService(Context context) {
        context.startService(new Intent(context, ServicePending.class));
    }

    public void stopPendingService(Context context) {
        context.stopService(new Intent(context, ServicePending.class));
    }
}
