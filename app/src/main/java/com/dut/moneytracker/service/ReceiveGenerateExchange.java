package com.dut.moneytracker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.realms.ExchangeLoopManager;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/04/2017.
 */

public class ReceiveGenerateExchange extends BroadcastReceiver {
    private static final int DEFAULT_VALUE = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        int idExchangeLooper = intent.getIntExtra(context.getString(R.string.id_exchange_looper), DEFAULT_VALUE);
        if (idExchangeLooper != DEFAULT_VALUE) {
            ExchangeLoopManager.getInstance(context).generateNewExchange(idExchangeLooper);
        }
    }
}
