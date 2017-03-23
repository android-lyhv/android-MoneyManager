package com.dut.moneytracker.models;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 19/03/2017.
 */

public class Service extends android.app.Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
