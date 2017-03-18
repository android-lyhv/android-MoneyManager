package com.dut.moneytracker.fragment.dashboard;

import com.dut.moneytracker.objects.Exchange;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */

public interface TabAccountListener {
    void onLoadChart();

    void onLoadExchanges();

    void onLoadAmount();

    void onShowDetailExchange(Exchange exchange);
}
