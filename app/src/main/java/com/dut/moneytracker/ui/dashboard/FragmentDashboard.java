package com.dut.moneytracker.ui.dashboard;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.base.BaseViewPagerAdapter;
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.ui.MainActivity_;
import com.dut.moneytracker.ui.base.BaseFragment;
import com.dut.moneytracker.ui.exchanges.ActivityAddExchange_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */
@EFragment(R.layout.fragment_dashboard)
public class FragmentDashboard extends BaseFragment implements TabLayout.OnTabSelectedListener, FragmentParentTab.CardAccountListener {
    public static final int MAX_DAY = 30;
    @ViewById(R.id.tabLayout)
    TabLayout mTabLayout;
    @ViewById(R.id.viewpager)
    ViewPager mViewPager;
    private RealmResults<Account> mAccounts;
    private BaseViewPagerAdapter mTabAdapter;
    private int targetAccount;
    private Handler mHandler = new Handler();

    @AfterViews
    void init() {
        mAccounts = AccountManager.getInstance().getAccountsNotOutside();
        initViewpager();
        initLoadListFragmentAccounts();
    }

    private void initViewpager() {
        mTabAdapter = new BaseViewPagerAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(this);
    }

    private void initLoadListFragmentAccounts() {
        int size = mAccounts.size();
        if (size > 1) {
            FragmentParentTab mFragmentParentTab = FragmentParentTab_.builder().build();
            mFragmentParentTab.registerCardAccountListener(this);
            mTabAdapter.addFragment(mFragmentParentTab, getString(R.string.title_all_account));
        }
        for (int i = 0; i < size; i++) {
            FragmentChildTab mFragmentChildTab = FragmentChildTab_.builder().mAccount(mAccounts.get(i)).build();
            mTabAdapter.addFragment(mFragmentChildTab, mAccounts.get(i).getName());
        }
        mTabAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition(), false);
        targetAccount = tab.getPosition() == 0 ? 0 : tab.getPosition() - 1;
        ((MainActivity_) getActivity()).registerAccount(mAccounts.get(targetAccount), targetAccount);
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
        ((MainActivity_) getActivity()).loadMenuItemFragmentDashboard();
    }

    @Override
    public void onClickCardAccount(int position) {
        mViewPager.setCurrentItem(position, false);
    }

    @Click(R.id.fab)
    void onClickAddExchange() {
        ActivityAddExchange_.intent(this).mAccount(mAccounts.get(targetAccount)).startForResult(IntentCode.ADD_NEW_EXCHANGE);
    }

    @OnActivityResult(IntentCode.ADD_NEW_EXCHANGE)
    void onResultAddNewExchange(int resultCode) {
        if (resultCode == IntentCode.ADD_NEW_EXCHANGE) {
            final Intent intent = new Intent();
            intent.setAction(getString(R.string.receiver_add_new_exchange));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getContext().sendBroadcast(intent);
                }
            });
        }
    }

    public void deleteFragmentAccount(int position) {
        mTabAdapter.removeFragment(++position);
        if (mTabAdapter.getCount() == 2) {
            mTabAdapter.removeFragment(0);
        }
        mTabAdapter.notifyDataSetChanged();
    }

    public void addFragmentAccount(Account account) {
        if (mTabAdapter.getCount() == 1) {
            FragmentParentTab mFragmentParentTab = FragmentParentTab_.builder().build();
            mFragmentParentTab.registerCardAccountListener(this);
            mTabAdapter.addPositionFragment(0, mFragmentParentTab, getString(R.string.title_all_account));
        }
        FragmentChildTab mFragmentChildTab = FragmentChildTab_.builder().mAccount(account).build();
        mTabAdapter.addFragment(mFragmentChildTab, account.getName());
        mTabAdapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(int position) {
        mTabAdapter.setTittlePosition(position + 1, mAccounts.get(position).getName());
        mTabAdapter.notifyDataSetChanged();
        mViewPager.invalidate();
    }
}
