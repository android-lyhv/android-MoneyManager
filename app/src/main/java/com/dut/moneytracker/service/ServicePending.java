package com.dut.moneytracker.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.realms.DebitManager;
import com.dut.moneytracker.models.realms.ExchangeLoopManager;
import com.dut.moneytracker.ui.MainActivity;

/**
 * Copyright@ AsianTech.Inc
 * Created by Ly Ho V. on 18/05/2017
 */
public class ServicePending extends Service {
    private static final int ID = 6969;
    private static final long DELAY = 2000L;
    private Handler mHandler;
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ExchangeLoopManager.getInstance().onGenerateNewExchange();
                DebitManager.getInstance().onCheckEndDateDebit(getApplicationContext());
                mHandler.postDelayed(this, DELAY);
            }
        }, DELAY);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && !isRunning) {
            isRunning = true;
            startBackground();
        }
        return START_STICKY;
    }

    public void startBackground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.img_wallet)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;
        startForeground(ID, notification);
    }
}
