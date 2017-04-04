package com.dut.moneytracker.recevier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.TypeLoop;
import com.dut.moneytracker.objects.ExchangeLooper;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/04/2017.
 */

public class GenerateManager {
    private static final long PENDING_DAY = 24 * 60 * 60 * 1000L;
    private static final long PENDING_WEEK = 7 * PENDING_DAY;
    private static final long PENDING_MONTH = 30 * PENDING_DAY;
    private static final long PENDING_YEAH = 365 * PENDING_DAY;

    private Context mContext;
    private AlarmManager mAlarmManager;

    public GenerateManager(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
    }

    /**
     * sent request pending for create exchange
     *
     * @param mExchangeLooper
     */
    public void pendingGenerateExchange(ExchangeLooper mExchangeLooper) {
        Intent intent = new Intent(mContext, ReceiveGenerateExchange.class);
        intent.putExtra(mContext.getString(R.string.id_exchange_looper), mExchangeLooper.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, mExchangeLooper.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long step = Calendar.getInstance().getTimeInMillis();
        switch (mExchangeLooper.getTypeLoop()) {
            case TypeLoop.DAY:
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, PENDING_DAY + step, PENDING_DAY, pendingIntent);
                break;
            case TypeLoop.WEAK:
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, PENDING_WEEK + step, PENDING_WEEK, pendingIntent);
                break;
            case TypeLoop.MONTH:
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, PENDING_MONTH + step, PENDING_MONTH, pendingIntent);
                break;
            case TypeLoop.YEAR:
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, PENDING_YEAH + step, PENDING_YEAH, pendingIntent);
                break;
        }
    }
}
