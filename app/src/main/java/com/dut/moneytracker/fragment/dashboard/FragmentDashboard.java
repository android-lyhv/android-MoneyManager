package com.dut.moneytracker.fragment.dashboard;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.MainActivity;
import com.dut.moneytracker.activities.MainActivity_;
import com.dut.moneytracker.adapter.BaseViewPagerAdapter;
import com.dut.moneytracker.fragment.BaseFragment;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */
@EFragment(R.layout.fragment_exchange_account)
public class FragmentDashboard extends BaseFragment implements TabLayout.OnTabSelectedListener, FragmentParentTab.CardAccountListener {
    @ViewById(R.id.tabLayout)
    TabLayout mTabLayout;
    @ViewById(R.id.viewpager)
    ViewPager mViewPager;
    private List<Account> mAccounts;
    private BaseViewPagerAdapter mViewPagerTabAccountAdapter;

    @AfterViews
    void init() {
        setUpViewpager();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mAccounts = AccountManager.getInstance().getListAccount();
                initLoadListFragmentAccount();
            }
        });

    }

    private void setUpViewpager() {
        mViewPagerTabAccountAdapter = new BaseViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerTabAccountAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(this);
    }

    private void initLoadListFragmentAccount() {
        mViewPagerTabAccountAdapter.clearFragment();
        int size = mAccounts.size();
        if (size > 1) {
            FragmentParentTab mFragmentParentTab = FragmentParentTab_.builder().build();
            mFragmentParentTab.registerCardAccountListener(this);
            mViewPagerTabAccountAdapter.addFragment(mFragmentParentTab, getString(R.string.tablyout_text_all_account));
        }
        for (int i = 0; i < size; i++) {
            FragmentChildTab mFragmentChildTab = FragmentChildTab_.builder().mAccount(mAccounts.get(i)).build();
            mViewPagerTabAccountAdapter.addFragment(mFragmentChildTab, mAccounts.get(i).getName());
        }
        mViewPagerTabAccountAdapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mViewPagerTabAccountAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int positionAccountSelected = tab.getPosition() == 0 ? 0 : tab.getPosition() - 1;
        ((MainActivity_) getActivity()).registerAccount(mAccounts.get(positionAccountSelected));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity_) getActivity()).checkFragmentDashboard();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).checkFragmentDashboard();
    }

    @Override
    public void onClickCardAccount(int position) {
        mViewPager.setCurrentItem(position);
    }
}
