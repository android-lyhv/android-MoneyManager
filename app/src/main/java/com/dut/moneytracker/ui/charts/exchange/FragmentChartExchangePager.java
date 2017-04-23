package com.dut.moneytracker.ui.charts.exchange;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.chart.ChartExchangePagerAdapter;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.ui.MainActivity_;
import com.dut.moneytracker.ui.base.BaseFragment;
import com.dut.moneytracker.ui.exchanges.PagerFragmentListener;
import com.dut.moneytracker.utils.DateTimeUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 26/03/2017.
 */
@EFragment(R.layout.fragment_pager)
public class FragmentChartExchangePager extends BaseFragment implements PagerFragmentListener {
    @ViewById(R.id.viewpager)
    ViewPager viewPager;
    @FragmentArg
    Filter mFilter;
    @FragmentArg
    int mChartType;
    private int viewType;
    private ChartExchangePagerAdapter mPagerAdapter;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), context.getString(R.string.broadcast_filter))) {
                int stepFilter = intent.getIntExtra(context.getString(R.string.step_filter), 0);
                if (stepFilter == 1) {
                    nextPager();
                }
                if (stepFilter == -1) {
                    prevPager();
                }
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity_) getActivity()).loadMenuItemFragmentChart();
    }

    @AfterViews
    void init() {
        initPager();
    }

    private void initPager() {
        viewType = mFilter.getTypeFilter();
        mPagerAdapter = new ChartExchangePagerAdapter(getChildFragmentManager());
        mPagerAdapter.init(viewPager, mFilter);
        mPagerAdapter.setTypePieChart(mChartType);
    }

    @Override
    public void nextPager() {
        mPagerAdapter.nextPager();
    }

    @Override
    public void prevPager() {
        mPagerAdapter.prevPager();
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(mBroadcastReceiver, new IntentFilter(getContext().getString(R.string.broadcast_filter)));
    }

    public void onReloadFragmentPager() {
        if (viewType != mFilter.getTypeFilter() || DateTimeUtils.getInstance().isSameDate(mFilter.getDateFilter(), new Date())) {
            viewType = mFilter.getTypeFilter();
            mPagerAdapter.targetCenterPager();
        }
        mPagerAdapter.notifyDataSetChanged();
    }

    public void setLastFilter() {
        mFilter = mPagerAdapter.getLastFilter();
    }
    public void resetFilter(Filter filter) {
        mFilter = filter;
    }
}
