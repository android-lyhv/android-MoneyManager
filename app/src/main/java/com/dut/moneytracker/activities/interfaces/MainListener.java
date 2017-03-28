package com.dut.moneytracker.activities.interfaces;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */

public interface MainListener {

    boolean checkFragmentDashboard();

    boolean checkFragmentExchanges();

    boolean checkFragmentLoop();

    void onChangeFilter();
}
