package com.dut.moneytracker.adapter.chart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.dut.moneytracker.adapter.base.BasePagerLoopAdapter;
import com.dut.moneytracker.models.FilterManager;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.ui.charts.FragmentChartMoney_;


/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 05/04/2017.
 */

public class ChartPagerAdapter extends BasePagerLoopAdapter {
    private int typePieChart;

    public void setTypePieChart(int typePieChart) {
        this.typePieChart = typePieChart;
    }

    public ChartPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Filter filter = FilterManager.getInstance().changeFilter(getFilter(), position - getCenter());
        return FragmentChartMoney_.builder().mFilter(filter).mChartType(typePieChart).build();
    }
}
