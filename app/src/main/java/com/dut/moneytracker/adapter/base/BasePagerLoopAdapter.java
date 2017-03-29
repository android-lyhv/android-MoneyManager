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
    private ViewPager mViewPager;
    private int positionSelected;
    private Filter mFilter;
    public BasePagerLoopAdapter(FragmentManager fm) {
        super(fm);
    }
    public void init(ViewPager viewPager, Filter filter) {
        mFilter = filter;
        setViewPager(viewPager);
        getViewPager().setAdapter(this);
        getViewPager().addOnPageChangeListener(this);
        centerPager();
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

    public void centerPager() {
        mViewPager.setCurrentItem(CENTER);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    public int getMaxPager() {
        return MAX_PAGE;
    }

    public int getCenter() {
        return CENTER;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }

    public int getPositionSelected() {
        return positionSelected;
    }

    public void setPositionSelected(int positionSelected) {
        this.positionSelected = positionSelected;
    }

    public Filter getFilter() {
        return mFilter;
    }
}
