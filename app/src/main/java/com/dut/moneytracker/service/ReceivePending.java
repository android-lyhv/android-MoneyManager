package com.dut.moneytracker.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.realms.DebitManager;
import com.dut.moneytracker.models.realms.ExchangeLoopManager;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/04/2017.
 */

public class ReceivePending extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent == null || !TextUtils.equals(intent.getAction(), context.getString(R.string.pending))) {
            return;
        }
        ExchangeLoopManager.getInstance().onGenerateNewExchange();
        DebitManager.getInstance().onCheckEndDateDebit(context);
    }
}
