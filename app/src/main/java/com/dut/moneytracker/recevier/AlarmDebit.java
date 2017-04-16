package com.dut.moneytracker.recevier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dut.moneytracker.R;
import com.dut.moneytracker.objects.Debit;

import static android.content.Context.ALARM_SERVICE;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/04/2017.
 */

public class AlarmDebit {
    private static AlarmDebit alarmDebit;
    private final long REPEAT_TIME = 10 * 1000;
    private static Context mContext;
    private static AlarmManager mAlarmManager;

    public static AlarmDebit getInstance() {
        if (alarmDebit == null) {
            alarmDebit = new AlarmDebit();
        }
        return alarmDebit;
    }

    private AlarmDebit() {
    }

    public void pendingAlarmDebit(Context context, Debit debit) {
        if (mContext == null) {
            mContext = context;
            mAlarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        }
        Intent intent = new Intent(mContext, ReceiveAlarmDebit.class);
        intent.setAction(mContext.getString(R.string.alarm_debit_action));
        intent.putExtra(mContext.getString(R.string.alarm_debit_id), debit.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, debit.getEndDate().getTime(), REPEAT_TIME, pendingIntent);
    }

    public void removePendingAlarm(int id) {
        Intent intent = new Intent(mContext, ReceiveAlarmDebit.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        mAlarmManager.cancel(pendingIntent);
    }
}
