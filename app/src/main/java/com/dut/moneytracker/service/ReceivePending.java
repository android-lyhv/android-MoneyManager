package com.dut.moneytracker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dut.moneytracker.models.realms.ExchangeLoopManager;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/04/2017.
 */

public class ReceivePending extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        onCheckGenerateExchange(context);
        onCheckAlarm(context);
    }

    private void onCheckAlarm(Context context) {

    }

    private void onCheckGenerateExchange(Context context) {
        ExchangeLoopManager.getInstance().onGenerateExchange(context);
    }
}
