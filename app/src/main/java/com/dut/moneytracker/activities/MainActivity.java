package com.dut.moneytracker.activities;

import android.content.Intent;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.interfaces.MainListener;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainListener {
    static final String TAG = MainActivity.class.getSimpleName();
    static final String DASHBOARD = "DASHBOARD";
    static final String EXCHANGES = "EXCHANGES";
    static final String LOOP = "LOOP";
    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @ViewById(R.id.fab)
    FloatingActionButton mFabAddExchange;
    @ViewById(R.id.imgUserLogo)
    CircleImageView imgUserLogo;
    @ViewById(R.id.tvUserName)
    TextView tvUserName;
    @ViewById(R.id.tvEmail)
    TextView tvEmail;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.spinner)
    AppCompatSpinner spinner;
    @ViewById(R.id.imgDateFilter)
    ImageView imgDateFilter;
    // Navigation
    @ViewById(R.id.llDashBoard)
    LinearLayout llDashboard;
    @ViewById(R.id.rlProfile)
    RelativeLayout mRlProfile;
    @ViewById(R.id.llRecode)
    LinearLayout llRecode;
    @ViewById(R.id.llDefaultExchange)
    LinearLayout llDefaultExchange;
    @ViewById(R.id.imgSettingAccount)
    ImageView imgSettingAccount;
    //model
    FragmentManager mFragmentManager;
    FragmentDashboard mFragmentDashboard;
    FragmentExchanges mFragmentExchanges;
    FragmentLoopExchange mFragmentLoopExchange;
    SpinnerAccountManger mSpinnerAccount;
    Account mAccount;
    Filter mFilter;

    @AfterViews
    void init() {
        mFragmentManager = getSupportFragmentManager();
        initData();
        initView();
        onLoadProfile();
        onLoadFragmentDashboard();
    }

    void initData() {
        mFilter = FilterManager.getInstance().getFilterDefault();
    }

    void initView() {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        initSpinner();
    }

    void initSpinner() {
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
                // mFragmentExchanges.updateListExchanges(mFilter);
            }
        });
    }

    public void registerAccount(Account account) {
        mAccount = account;
    }

    void onLoadProfile() {
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

    @Click(R.id.fab)
    void onCLickFab() {
        if (checkFragmentLoop()) {
            startActivityAddLoopExchange();
        } else {
            startActivityAddExchange();
        }
    }

    @Click(R.id.rlProfile)
    void onCLickProfile() {
        checkCloseNavigation();
        startActivityForResult(new Intent(this, UserInformationActivity.class), RequestCode.PROFILE);
    }

    @Click(R.id.imgDateFilter)
    void onClickFilter() {
        checkCloseNavigation();
        onShowDialogPickFilterDate();
    }

    @Click(R.id.llDashBoard)
    void onClickDashBoard() {
        checkCloseNavigation();
        onLoadFragmentDashboard();
    }

    @Click(R.id.llRecode)
    void onClickRecodes() {
        checkCloseNavigation();
        onLoadFragmentDefaultExchange();
    }

    @Click(R.id.llDefaultExchange)
    void onClickDefault() {
        checkCloseNavigation();
        onLoadFragmentLoopExchange();
    }

    @Click(R.id.imgSettingAccount)
    void onSettingAccount() {
        startActivityEditAccount();
    }

    private void checkCloseNavigation() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    void startActivityAddLoopExchange() {
        Intent intent = new Intent(this, ActivityAddLoopExchange.class);
        startActivity(intent);
    }

    void onShowDialogPickFilterDate() {
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

    void startActivityEditAccount() {
        Intent intent = new Intent(this, ActivityEditAccount.class);
        intent.putExtra(getString(R.string.extra_account), mAccount);
        startActivityForResult(intent, RequestCode.EDIT_ACCOUNT);
    }

    void startActivityAddExchange() {
        Intent intent = new Intent(this, ActivityAddExchange.class);
        intent.putExtra(getString(R.string.extra_account), mAccount);
        startActivityForResult(intent, RequestCode.ADD_EXCHANGE);
    }

    void onResultEditAccount(Intent data) {
        Account account = data.getParcelableExtra(getString(R.string.extra_account));
        AccountManager.getInstance().insertOrUpdate(account);
        mFragmentDashboard.notifyDataSetChanged();
    }

    void onResultAddAccount() {
        mFragmentDashboard.notifyDataSetChanged();
    }

    void onResultLogout() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    public void onLoadFragmentExchange() {
        mFragmentExchanges = new FragmentExchanges();
        requestReplaceFragment(mFragmentExchanges, EXCHANGES, true);
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

    void loadFragmentExchange(Filter filter) {
        mFragmentExchanges = new FragmentExchanges();
        List<Exchange> mExchanges = ExchangeManger.getInstance().getExchanges(filter);
        mFragmentExchanges.setExchanges(mExchanges, filter);
        requestReplaceFragment(mFragmentExchanges, EXCHANGES, true);
    }

    public void onLoadFragmentDashboard() {
        mFragmentDashboard = new FragmentDashboard();
        requestReplaceFragment(mFragmentDashboard, DASHBOARD, false);
    }

    public void onLoadFragmentLoopExchange() {
        mFragmentLoopExchange = new FragmentLoopExchange();
        requestReplaceFragment(mFragmentLoopExchange, LOOP, true);
    }

    void requestReplaceFragment(Fragment fragment, String TAG, boolean isStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContent, fragment, TAG);
        if (isStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean checkFragmentDashboard() {
        Fragment fragment = mFragmentManager.findFragmentByTag(DASHBOARD);
        if (fragment instanceof FragmentDashboard) {
            mFabAddExchange.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            imgDateFilter.setVisibility(View.GONE);
            imgSettingAccount.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkFragmentExchanges() {
        Fragment fragment = mFragmentManager.findFragmentByTag(EXCHANGES);
        if (fragment instanceof FragmentExchanges) {
            mFabAddExchange.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            imgDateFilter.setVisibility(View.VISIBLE);
            imgSettingAccount.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkFragmentLoop() {
        Fragment fragment = mFragmentManager.findFragmentByTag(LOOP);
        if (fragment instanceof FragmentLoopExchange) {
            mFabAddExchange.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            imgDateFilter.setVisibility(View.GONE);
            imgSettingAccount.setVisibility(View.GONE);
            return true;
        }
        return false;
    }
}