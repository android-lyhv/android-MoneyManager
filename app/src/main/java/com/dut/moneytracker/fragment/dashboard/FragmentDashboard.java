package com.dut.moneytracker.fragment.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.MainActivity;
import com.dut.moneytracker.adapter.BaseViewPagerAdapter;
import com.dut.moneytracker.fragment.BaseFragment;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.utils.ArgbEvaluatorColor;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */

public class FragmentDashboard extends BaseFragment implements TabLayout.OnTabSelectedListener {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private int positionAccountSelected;
    private List<Account> mAccounts;
    private BaseViewPagerAdapter mViewPagerTabAccountAdapter;
    private FragmentParentExchangeTab mFragmentParentExchangeTab;
    private MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccounts = AccountManager.getInstance().getListAccount();
    }

    private void onLoadFragmentAccount() {
        mViewPagerTabAccountAdapter.clearFragment();
        int size = mAccounts.size();
        if (size > 1) {
            ArgbEvaluatorColor.getInstance().addColorCode(getResources().getColor(R.color.colorPrimaryDark));
            mFragmentParentExchangeTab = new FragmentParentExchangeTab();
            mFragmentParentExchangeTab.registerCardAccountListener(new FragmentParentExchangeTab.CardAccountListener() {
                @Override
                public void onClickCardAccount(int position) {
                    mViewPager.setCurrentItem(position);
                }
            });
            mFragmentParentExchangeTab.setAccounts(mAccounts);
            mViewPagerTabAccountAdapter.addFragment(mFragmentParentExchangeTab, getString(R.string.tablyout_text_all_account));
        }
        for (int i = 0; i < size; i++) {
            ArgbEvaluatorColor.getInstance().addColorCode(Color.parseColor(mAccounts.get(i).getColorCode()));
            FragmentChildExchangeTab mFragmentChildExchangeTab = new FragmentChildExchangeTab();
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.extra_account), mAccounts.get(i));
            mFragmentChildExchangeTab.setArguments(bundle);
            mViewPagerTabAccountAdapter.addFragment(mFragmentChildExchangeTab, mAccounts.get(i).getName());
        }
        mViewPagerTabAccountAdapter.notifyDataSetChanged();
    }

    private void setUpViewpager() {
        mViewPagerTabAccountAdapter = new BaseViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerTabAccountAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(this);
        onLoadFragmentAccount();
    }

    public void notifyDataSetChanged() {
        mViewPagerTabAccountAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_account, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        setUpViewpager();
    }

    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        positionAccountSelected = tab.getPosition() == 0 ? 0 : tab.getPosition() - 1;
        mainActivity.registerAccount(mAccounts.get(positionAccountSelected));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
