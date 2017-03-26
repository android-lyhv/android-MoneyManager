package com.dut.moneytracker.adapter.exchanges;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.Calendar;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 21/03/2017.
 */

public class ExchangePagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private static final String TAG = ExchangePagerAdapter.class.getSimpleName();
    private final int MAX_PAGE = 100000;
    private final int CENTER = MAX_PAGE / 2;
    private final Calendar mCalendar = Calendar.getInstance();
    private ViewPager mViewPager;
    private int positionSelected;


    public ExchangePagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        return null;
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
}
