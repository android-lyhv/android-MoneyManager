package com.dut.moneytracker.adapter.exchanges;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.dut.moneytracker.fragment.exchanges.FragmentExchanges_;
import com.dut.moneytracker.models.FilterManager;
import com.dut.moneytracker.objects.Filter;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 21/03/2017.
 */

public class ExchangePagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
    private static final String TAG = ExchangePagerAdapter.class.getSimpleName();
    private final int MAX_PAGE = 100000;
    private final int CENTER = MAX_PAGE / 2;
    private ViewPager mViewPager;
    private Filter mFilter;
    private int positionSelected;


    public ExchangePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void init(ViewPager viewPager, Filter filter) {
        mFilter = filter;
        mViewPager = viewPager;
        mViewPager.setAdapter(this);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(CENTER);
    }

    @Override
    public Fragment getItem(int position) {
        Filter filter = FilterManager.getInstance().changeFilter(mFilter, position - CENTER);
        return FragmentExchanges_.builder().mFilter(filter).build();
    }

    @Override
    public int getCount() {
        return MAX_PAGE;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: " + position);
        positionSelected = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void nextPager() {
        mViewPager.setCurrentItem(positionSelected + 1);
    }

    public void prevPager() {
        mViewPager.setCurrentItem(positionSelected - 1);
    }

    public void centerPager() {
        mViewPager.setCurrentItem(CENTER);
    }
}
