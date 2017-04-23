package com.dut.moneytracker.adapter.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.dut.moneytracker.objects.Filter;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 29/03/2017.
 */

public class BasePagerLoopAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
    private final int MAX_PAGE = 100000;
    private final int CENTER = MAX_PAGE / 2;
    private int positionSelected;
    private ViewPager mViewPager;
    private Filter mFilter;
    private Filter mLastFilter;

    public BasePagerLoopAdapter(FragmentManager fm) {
        super(fm);
    }

    public void init(ViewPager viewPager, Filter filter) {
        mFilter = filter;
        mLastFilter = filter;
        mViewPager = viewPager;
        mViewPager.setAdapter(this);
        mViewPager.addOnPageChangeListener(this);
        targetCenterPager();
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

    public void targetCenterPager() {
        mViewPager.setCurrentItem(CENTER);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    public int getCenter() {
        return CENTER;
    }

    public Filter getFilter() {
        return mFilter;
    }

    public void setFilter(Filter mFilter) {
        this.mFilter = mFilter;
    }

    public Filter getLastFilter() {
        return mLastFilter;
    }

    public void setLastFilter(Filter mLastFilter) {
        this.mLastFilter = mLastFilter;
    }
}
