package com.dut.moneytracker.ui.charts;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/04/2017.
 */

public interface ChartMoneyListener {
    void onResultMultiMoney(String minMoney, String averageMoney, String maxMoney);

    void onResultTotal(String money);
}
