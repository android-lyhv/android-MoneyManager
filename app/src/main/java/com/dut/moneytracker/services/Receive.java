package com.dut.moneytracker.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/04/2017.
 */

public class Receive extends BroadcastReceiver {
    private static final String TAG = Receive.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceiveAAAAAAAAA: ");
    }
}
