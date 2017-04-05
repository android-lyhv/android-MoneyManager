package com.dut.moneytracker.ui.dashboard;

import com.dut.moneytracker.objects.Exchange;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */

interface TabAccountListener {
    void onLoadChart();

    void onLoadExchanges();

    void onShowAmount();

    void onShowDetailExchange(Exchange exchange);
}
