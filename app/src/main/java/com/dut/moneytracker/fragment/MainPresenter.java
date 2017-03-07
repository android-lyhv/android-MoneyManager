package com.dut.moneytracker.fragment;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
public class MainPresenter {
    private static MainPresenter ourInstance = new MainPresenter();

    public static MainPresenter getInstance() {
        return ourInstance;
    }

    private MainPresenter() {
    }
}
