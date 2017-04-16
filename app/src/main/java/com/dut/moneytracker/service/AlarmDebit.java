package com.dut.moneytracker.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dut.moneytracker.R;
import com.dut.moneytracker.objects.Debit;
import com.dut.moneytracker.utils.DateTimeUtils;

import static android.content.Context.ALARM_SERVICE;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/04/2017.
 */

public class AlarmDebit {
    private static AlarmDebit alarmDebit;
    private final long REPEAT_TIME = 10 * 60 * 1000;


    public static AlarmDebit getInstance() {
        if (alarmDebit == null) {
            alarmDebit = new AlarmDebit();
        }
        return alarmDebit;
    }

    private AlarmDebit() {
    }

    public void pendingAlarmDebit(Context context, Debit debit) {
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, ReceiveAlarmDebit.class);
        intent.setAction(context.getString(R.string.alarm_debit_action));
        intent.putExtra(context.getString(R.string.alarm_debit_id), debit.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, debit.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long time = DateTimeUtils.getInstance().getTimeNotification(debit.getEndDate(), 6).getTime();
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, REPEAT_TIME, pendingIntent);
    }

    public void removePendingAlarm(Context context, int id) {
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, ReceiveAlarmDebit.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        mAlarmManager.cancel(pendingIntent);
    }
}
