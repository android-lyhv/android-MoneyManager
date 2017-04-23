package com.dut.moneytracker.adapter.chart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.dut.moneytracker.adapter.base.BasePagerLoopAdapter;
import com.dut.moneytracker.models.realms.FilterManager;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.ui.charts.category.FragmentChartCategory_;


/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 05/04/2017.
 */

public class ChartCategoryPagerAdapter extends BasePagerLoopAdapter {

    public ChartCategoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Filter filter = FilterManager.getInstance().changeFilter(getFilter(), position - getCenter());
        setLastFilter(filter);
        return FragmentChartCategory_.builder().mFilter(filter).build();
    }

}
