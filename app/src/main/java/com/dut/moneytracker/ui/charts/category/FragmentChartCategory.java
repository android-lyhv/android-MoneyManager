package com.dut.moneytracker.ui.charts.category;

import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 10/04/2017.
 */
@EFragment
public class FragmentChartCategory extends BaseFragment {
    @FragmentArg
    Filter mFilter;

    @AfterViews
    void init() {

    }
}
