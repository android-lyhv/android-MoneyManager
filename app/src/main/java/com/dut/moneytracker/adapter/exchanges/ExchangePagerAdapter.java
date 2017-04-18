package com.dut.moneytracker.adapter.exchanges;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.dut.moneytracker.adapter.base.BasePagerLoopAdapter;
import com.dut.moneytracker.ui.exchanges.FragmentExchanges_;
import com.dut.moneytracker.models.realms.FilterManager;
import com.dut.moneytracker.objects.Filter;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 21/03/2017.
 */

public class ExchangePagerAdapter extends BasePagerLoopAdapter {
    public ExchangePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Filter filter = FilterManager.getInstance().changeFilter(getFilter(), position - getCenter());
        return FragmentExchanges_.builder().mFilter(filter).build();
    }
}
