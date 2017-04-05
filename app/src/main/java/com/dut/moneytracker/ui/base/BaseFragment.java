package com.dut.moneytracker.ui.base;


import android.support.v4.app.Fragment;

import org.androidannotations.annotations.EFragment;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 23/02/2017.
 */
@EFragment
public abstract class BaseFragment extends Fragment {
    @Override
    public String toString() {
        return "MoneyTacker";
    }
}
