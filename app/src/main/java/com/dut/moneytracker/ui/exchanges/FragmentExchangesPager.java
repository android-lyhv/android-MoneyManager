package com.dut.moneytracker.ui.exchanges;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.exchanges.ExchangePagerAdapter;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.ui.MainActivity_;
import com.dut.moneytracker.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 26/03/2017.
 */
@EFragment(R.layout.fragment_pager)
public class FragmentExchangesPager extends BaseFragment implements PagerFragmentListener {
    private static final String TAG = FragmentExchangesPager.class.getSimpleName();
    @ViewById(R.id.viewpager)
    ViewPager viewPager;
    @FragmentArg
    Filter mFilter;
    private int viewType;
    private ExchangePagerAdapter mPagerAdapter;
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
        ((MainActivity_) getActivity()).loadMenuItemFragmentExchanges();
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(mBroadcastReceiver);
    }

    @AfterViews
    void init() {
        initPager();
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(mBroadcastReceiver, new IntentFilter(getContext().getString(R.string.broadcast_filter)));
    }

    private void initPager() {
        viewType = mFilter.getViewType();
        mPagerAdapter = new ExchangePagerAdapter(getChildFragmentManager());
        mPagerAdapter.init(viewPager, mFilter);
    }

    @Override
    public void nextPager() {
        mPagerAdapter.nextPager();
    }

    @Override
    public void prevPager() {
        mPagerAdapter.prevPager();
    }

    public void onReloadFragmentPager() {
        if (viewType != mFilter.getViewType()) {
            viewType = mFilter.getViewType();
            mPagerAdapter.centerPager();
        }
        mPagerAdapter.notifyDataSetChanged();
    }
}
