package com.dut.moneytracker.fragment.exchanges;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.MainActivity_;
import com.dut.moneytracker.adapter.exchanges.ExchangePagerAdapter;
import com.dut.moneytracker.fragment.BaseFragment;
import com.dut.moneytracker.objects.Filter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 26/03/2017.
 */
@EFragment(R.layout.fragment_exchange_pager)
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
                int stepFilter = intent.getIntExtra(context.getString(R.string.step_fitler), 0);
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
        getContext().registerReceiver(mBroadcastReceiver, new IntentFilter(getContext().getString(R.string.broadcast_filter)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(mBroadcastReceiver);
    }

    @AfterViews
    void init() {
        initPager();
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity_) getActivity()).checkFragmentExchanges();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity_) getActivity()).checkFragmentExchanges();
    }

    public void onReloadFragmentPager() {
        if (viewType != mFilter.getViewType()) {
            viewType = mFilter.getViewType();
            mPagerAdapter.centerPager();
        }
        mPagerAdapter.notifyDataSetChanged();
    }
}
