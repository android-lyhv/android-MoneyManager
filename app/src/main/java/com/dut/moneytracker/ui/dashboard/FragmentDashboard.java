package com.dut.moneytracker.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.base.BaseViewPagerAdapter;
import com.dut.moneytracker.constant.RequestCode;
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

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */
@EFragment(R.layout.fragment_dashboard)
public class FragmentDashboard extends BaseFragment implements TabLayout.OnTabSelectedListener, FragmentParentTab.CardAccountListener
        , RealmChangeListener<RealmResults<Account>> {
    public static final String RECEIVER_ADD_DELETE_ACCOUNT = "RECEIVER_ADD_DELETE_ACCOUNT";
    public static final String RECEIVER_RELOAD_TAB_ACCOUNT = "RECEIVER_RELOAD_TAB_ACCOUNT";
    public static final long DELAY = 0L;
    public static final int LIMIT_ITEM = 5;
    @ViewById(R.id.tabLayout)
    TabLayout mTabLayout;
    @ViewById(R.id.viewpager)
    ViewPager mViewPager;
    private RealmResults<Account> mAccounts;
    private BaseViewPagerAdapter mTabAdapter;
    private int targetAccount;
    private Handler mHandler = new Handler();
    private BroadcastReceiver mReceiverAddDeleteAccount = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            if (TextUtils.equals(intent.getAction(), RECEIVER_ADD_DELETE_ACCOUNT)) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTabAdapter.notifyDataSetChanged();
                    }
                }, DELAY);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(mReceiverAddDeleteAccount, new IntentFilter(RECEIVER_ADD_DELETE_ACCOUNT));
    }

    @AfterViews
    void init() {
        mAccounts = AccountManager.getInstance().getAccounts();
        mAccounts.addChangeListener(this);
        initViewpager();
        initLoadListFragmentAccounts();
    }

    private void initViewpager() {
        mTabAdapter = new BaseViewPagerAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(1);
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
        mViewPager.setCurrentItem(tab.getPosition(),false);
        targetAccount = tab.getPosition() == 0 ? 0 : tab.getPosition() - 1;
        ((MainActivity_) getActivity()).registerAccount(mAccounts.get(targetAccount));
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
        ActivityAddExchange_.intent(this).mAccount(mAccounts.get(targetAccount)).startForResult(RequestCode.ADD_NEW_EXCHANGE);
    }

    @OnActivityResult(RequestCode.ADD_NEW_EXCHANGE)
    void onResult(Intent intent) {
        if (intent == null) {
            return;
        }
        getContext().sendBroadcast(new Intent(RECEIVER_RELOAD_TAB_ACCOUNT));
    }

    @Override
    public void onChange(RealmResults<Account> element) {
        getContext().sendBroadcast(new Intent(RECEIVER_RELOAD_TAB_ACCOUNT));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAccounts.removeAllChangeListeners();
        mHandler.removeCallbacksAndMessages(null);
    }
}
