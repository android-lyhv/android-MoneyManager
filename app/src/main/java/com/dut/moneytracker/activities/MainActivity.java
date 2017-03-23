package com.dut.moneytracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.interfaces.MainListener;
import com.dut.moneytracker.constant.ActivityStatus;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.dialogs.DialogPickFilter;
import com.dut.moneytracker.fragment.FragmentExchanges;
import com.dut.moneytracker.fragment.FragmentLoopExchange;
import com.dut.moneytracker.fragment.dashboard.FragmentDashboard;
import com.dut.moneytracker.models.FilterManager;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.view.CircleImageView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String DASHBOARD = "DASHBOARD";
    private static final String EXCHANGES = "EXCHANGES";
    private static final String DEFAULT_EXCHANGE = "DEFAULT_EXCHANGE";
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFabAddExchange;
    private RelativeLayout mRlProfile;
    private CircleImageView imgUserLogo;
    private TextView tvUserName;
    private TextView tvEmail;
    private Toolbar mToolbar;
    private AppCompatSpinner spinner;
    private ImageView imgDateFilter;
    // Navigation
    private LinearLayout llDashboard;
    private LinearLayout llRecode;
    private LinearLayout llDefaultExchange;
    //model
    private FragmentManager mFragmentManager;
    private FragmentDashboard mFragmentDashboard;
    private FragmentExchanges mFragmentExchanges;
    private FragmentLoopExchange mFragmentLoopExchange;
    private Account mAccount;
    private Filter mFilter;
    private SpinnerAccountManger mSpinnerAccount;
    private int mTargetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        initData();
        initView();
        onLoadProfile();
        onLoadFragmentDashboard();
    }

    private void initData() {
        mTargetFragment = ActivityStatus.ON_FRAGMENT_DASHBOARD;
        mFilter = FilterManager.getInstance().getFilterDefault();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mFabAddExchange = (FloatingActionButton) findViewById(R.id.fab);
        mFabAddExchange.setOnClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mRlProfile = (RelativeLayout) findViewById(R.id.rlProfile);
        mRlProfile.setOnClickListener(this);
        imgUserLogo = (CircleImageView) findViewById(R.id.imgUserLogo);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        imgDateFilter = (ImageView) findViewById(R.id.imgDateFilter);
        spinner = (AppCompatSpinner) findViewById(R.id.spinner);
        imgDateFilter.setOnClickListener(this);
        llDashboard = (LinearLayout) findViewById(R.id.llDashBoard);
        llDashboard.setOnClickListener(this);
        llRecode = (LinearLayout) findViewById(R.id.llRecode);
        llRecode.setOnClickListener(this);
        llDefaultExchange = (LinearLayout) findViewById(R.id.llDefaultExchange);
        llDefaultExchange.setOnClickListener(this);
        requestChangeToolbarStatus(mTargetFragment);
        initSpinner();
    }

    private void initSpinner() {
        mSpinnerAccount = new SpinnerAccountManger(this, spinner);
        mSpinnerAccount.registerSelectedItem(new SpinnerAccountManger.ItemSelectedListener() {
            @Override
            public void onItemSelected(String accountId) {
                if (TextUtils.isEmpty(accountId)) {
                    mFilter.setRequestByAccount(false);
                } else {
                    mFilter.setRequestByAccount(true);
                    mFilter.setAccountId(accountId);
                }
                mFragmentExchanges.updateListExchanges(mFilter);
            }
        });
    }

    public void registerAccount(Account account) {
        mAccount = account;
    }

    private void onLoadProfile() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Glide.with(this).load(firebaseAuth.getCurrentUser().getPhotoUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .into(imgUserLogo);
        tvUserName.setText(String.valueOf(firebaseAuth.getCurrentUser().getDisplayName()));
        tvEmail.setText(String.valueOf(firebaseAuth.getCurrentUser().getEmail()));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_item, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionEditAccount:
                startActivityEditAccount();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        switch (v.getId()) {
            case R.id.fab:
                if (isFragmentLoopExchange()) {
                    startActivityAddLoopExchange();
                } else {
                    startActivityAddExchange();
                }
                break;
            case R.id.rlProfile:
                startActivityForResult(new Intent(this, UserInformationActivity.class), RequestCode.PROFILE);
                break;
            case R.id.imgDateFilter:
                onShowDialogPickFilterDate();
                break;
            case R.id.llDashBoard:
                onLoadFragmentDashboard();
                break;
            case R.id.llRecode:
                onLoadFragmentDefaultExchange();
                break;
            case R.id.llDefaultExchange:
                onLoadFragmentLoopExchange();
        }
    }

    private void startActivityAddLoopExchange() {
        Intent intent = new Intent(this, ActivityAddLoopExchange.class);
        startActivity(intent);
    }

    private void onShowDialogPickFilterDate() {
        DialogPickFilter dialogPickFilter = new DialogPickFilter();
        dialogPickFilter.show(getFragmentManager(), TAG);
        dialogPickFilter.registerFilter(new DialogPickFilter.FilterListener() {
            @Override
            public void onResult(int idFilter) {
                mFilter.setViewType(idFilter);
                mFilter.setDateFilter(new Date());
                mFragmentExchanges.updateListExchanges(mFilter);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ResultCode.PROFILE:
                onResultLogout();
                break;
            case ResultCode.EDIT_ACCOUNT:
                onResultEditAccount(data);
                break;
            case ResultCode.ADD_EXCHANGE:
                onResultAddAccount();
                break;
        }
    }

    private void startActivityEditAccount() {
        Intent intent = new Intent(this, ActivityEditAccount.class);
        intent.putExtra(getString(R.string.extra_account), mAccount);
        startActivityForResult(intent, RequestCode.EDIT_ACCOUNT);
    }

    private void startActivityAddExchange() {
        Intent intent = new Intent(this, ActivityAddExchange.class);
        intent.putExtra(getString(R.string.extra_account), mAccount);
        startActivityForResult(intent, RequestCode.ADD_EXCHANGE);
    }

    private void onResultEditAccount(Intent data) {
        Account account = data.getParcelableExtra(getString(R.string.extra_account));
        AccountManager.getInstance().insertOrUpdate(account);
        mFragmentDashboard.notifyDataSetChanged();
    }

    private void onResultAddAccount() {
        mFragmentDashboard.notifyDataSetChanged();
    }

    private void onResultLogout() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onLoadFragmentExchange() {
        mFragmentExchanges = new FragmentExchanges();
        requestReplaceFragment(mFragmentExchanges, EXCHANGES, true);
        requestChangeToolbarStatus(ActivityStatus.ON_FRAGMENT_EXCHANGES);
    }

    public void onLoadFragmentDefaultExchange() {
        mFilter = FilterManager.getInstance().getFilterDefault();
        mSpinnerAccount.setSelectItem(null);
        loadFragmentExchange(mFilter);
    }

    public void onLoadFragmentDefaultExchange(String idAccount) {
        mFilter = FilterManager.getInstance().getFilterDefaultAccount(idAccount);
        mSpinnerAccount.setSelectItem(idAccount);
        loadFragmentExchange(mFilter);
    }

    private void loadFragmentExchange(Filter filter) {
        mFragmentExchanges = new FragmentExchanges();
        List<Exchange> mExchanges = ExchangeManger.getInstance().getExchanges(filter);
        mFragmentExchanges.setExchanges(mExchanges, filter);
        requestReplaceFragment(mFragmentExchanges, EXCHANGES, true);
        requestChangeToolbarStatus(ActivityStatus.ON_FRAGMENT_EXCHANGES);
    }

    @Override
    public void onLoadFragmentDashboard() {
        mFragmentDashboard = new FragmentDashboard();
        requestReplaceFragment(mFragmentDashboard, DASHBOARD, false);
        requestChangeToolbarStatus(ActivityStatus.ON_FRAGMENT_DASHBOARD);
    }

    public void onLoadFragmentLoopExchange() {
        mFragmentLoopExchange = new FragmentLoopExchange();
        requestReplaceFragment(mFragmentLoopExchange, DEFAULT_EXCHANGE, true);
    }

    private void requestReplaceFragment(Fragment fragment, String TAG, boolean isStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContent, fragment, TAG);
        if (isStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void checkFragmentDashboard() {
        Fragment fragment = mFragmentManager.findFragmentByTag(DASHBOARD);
        if (fragment instanceof FragmentDashboard) {
            mFabAddExchange.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            imgDateFilter.setVisibility(View.GONE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
            }
        }
    }

    public boolean isFragmentLoopExchange() {
        Fragment fragment = mFragmentManager.findFragmentByTag(DEFAULT_EXCHANGE);
        return fragment instanceof FragmentLoopExchange;
    }

    public void requestChangeToolbarStatus(int status) {
        switch (status) {
            case ActivityStatus.ON_FRAGMENT_DASHBOARD:
                mFabAddExchange.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                imgDateFilter.setVisibility(View.GONE);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                }
                break;
            case ActivityStatus.ON_FRAGMENT_EXCHANGES:
                spinner.setVisibility(View.VISIBLE);
                imgDateFilter.setVisibility(View.VISIBLE);
                mFabAddExchange.setVisibility(View.GONE);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                }
                break;
        }
    }
}