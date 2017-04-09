package com.dut.moneytracker.ui;

import android.content.Intent;
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
import com.dut.moneytracker.constant.FilterType;
import com.dut.moneytracker.constant.PieChartType;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.dialogs.DialogCustomFilter;
import com.dut.moneytracker.dialogs.DialogCustomFilter_;
import com.dut.moneytracker.dialogs.DialogPickFilter;
import com.dut.moneytracker.models.FilterManager;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.ui.account.ActivityAccounts_;
import com.dut.moneytracker.ui.account.ActivityEditAccount_;
import com.dut.moneytracker.ui.base.SpinnerAccountManger;
import com.dut.moneytracker.ui.charts.FragmentChartPager;
import com.dut.moneytracker.ui.charts.FragmentChartPager_;
import com.dut.moneytracker.ui.dashboard.FragmentDashboard;
import com.dut.moneytracker.ui.dashboard.FragmentDashboard_;
import com.dut.moneytracker.ui.exchangeloop.FragmentLoopExchange;
import com.dut.moneytracker.ui.exchangeloop.FragmentLoopExchange_;
import com.dut.moneytracker.ui.exchanges.FragmentExchangesPager;
import com.dut.moneytracker.ui.exchanges.FragmentExchangesPager_;
import com.dut.moneytracker.ui.interfaces.MainListener;
import com.dut.moneytracker.ui.user.LoginActivity;
import com.dut.moneytracker.ui.user.UserInformationActivity;
import com.dut.moneytracker.view.CircleImageView;
import com.google.firebase.auth.FirebaseAuth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

import static com.dut.moneytracker.ui.MainActivity.FragmentTag.DEFAULT;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String DASHBOARD = "DASHBOARD";

    public enum FragmentTag {
        DEFAULT, DASHBOARD, EXCHANGES, EXCHANGE_LOOPS, PROFILE, CHART_INCOME, CHART_EXPENSES, ACCOUNT
    }

    FragmentTag mFragmentTag = DEFAULT;
    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
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
    private DialogPickFilter mDialogPickFilter;
    private DialogCustomFilter mDialogCustomFilter;
    //model
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentDashboard mFragmentDashboard;
    FragmentLoopExchange mFragmentLoopExchange;
    FragmentExchangesPager mFragmentExchangesPager;
    FragmentChartPager mFragmentChartPager;
    SpinnerAccountManger mSpinnerAccount;
    private Account mAccount;
    private Filter mFilter;

    @AfterViews
    void init() {
        mDialogPickFilter = new DialogPickFilter();
        mDialogCustomFilter = DialogCustomFilter_.builder().build();
        initView();
        onLoadProfile();
        onLoadFragmentDashboard();
    }

    void initView() {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                switch (mFragmentTag) {
                    case ACCOUNT:
                        ActivityAccounts_.intent(MainActivity.this).start();
                        break;
                    case DASHBOARD:
                        onLoadFragmentDashboard();
                        break;
                    case EXCHANGES:
                        onLoadFragmentExchanges();
                        break;
                    case EXCHANGE_LOOPS:
                        onLoadFragmentLoopExchange();
                        break;
                    case CHART_INCOME:
                        onLoadFragmentChart(PieChartType.INCOME);
                        break;
                    case CHART_EXPENSES:
                        onLoadFragmentChart(PieChartType.EXPENSES);
                        break;
                    case PROFILE:
                        startActivityForResult(new Intent(MainActivity.this, UserInformationActivity.class), RequestCode.PROFILE);
                        break;
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mFragmentTag = DEFAULT;
            }
        };
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        initSpinnerFilter();
    }

    // Change filer
    private void initSpinnerFilter() {
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
                reloadFragmentFilter();
            }
        });
    }

    private void onShowDialogPickFilterDate() {
        mDialogPickFilter.show(getFragmentManager(), TAG);
        mDialogPickFilter.registerFilter(mFilter.getViewType(), new DialogPickFilter.FilterListener() {
            @Override
            public void onResult(int filterType) {
                if (filterType == FilterType.CUSTOM) {
                    onChangeFilterCustom();
                } else {
                    mFilter.setViewType(filterType);
                    mFilter.setDateFilter(new Date());
                    reloadFragmentFilter();
                }

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
            Fragment fragment = mFragmentManager.findFragmentByTag(DASHBOARD);
            if (fragment != null) {
                super.onBackPressed();
            } else {
                onLoadFragmentDashboard();
            }
        }
    }

    @Click(R.id.imgDateFilter)
    void onClickFilter() {
        onShowDialogPickFilterDate();
    }

    @Click(R.id.rlProfile)
    void onCLickProfile() {
        mFragmentTag = FragmentTag.PROFILE;
        onCloseNavigation();
    }

    @Click(R.id.llAccount)
    void onClickAccount() {
        mFragmentTag = FragmentTag.ACCOUNT;
        onCloseNavigation();
    }

    @Click(R.id.llDashBoard)
    void onClickDashBoard() {
        mFragmentTag = FragmentTag.DASHBOARD;
        onCloseNavigation();
    }

    @Click(R.id.llRecode)
    void onClickRecodes() {
        mFragmentTag = FragmentTag.EXCHANGES;
        onCloseNavigation();
    }

    @Click(R.id.llDefaultExchange)
    void onClickDefault() {
        mFragmentTag = FragmentTag.EXCHANGE_LOOPS;
        onCloseNavigation();
    }

    @Click(R.id.imgSettingAccount)
    void onSettingAccount() {
        ActivityEditAccount_.intent(this).mAccount(mAccount).startForResult(RequestCode.EDIT_ACCOUNT);
    }

    @Click(R.id.llChartIncome)
    void onClickChartIncome() {
        mFragmentTag = FragmentTag.CHART_INCOME;
        onCloseNavigation();
    }

    @Click(R.id.llChartExpense)
    void onClickChartExpense() {
        mFragmentTag = FragmentTag.CHART_EXPENSES;
        onCloseNavigation();
    }

    private void onCloseNavigation() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
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
        }
    }

    void onResultEditAccount(Intent data) {
        Account account = data.getParcelableExtra(getString(R.string.extra_account));
        AccountManager.getInstance().insertOrUpdate(account);
    }

    void onResultLogout() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * load piechart
     *
     * @param typeChart
     */
    public void onLoadFragmentChart(int typeChart) {
        mFilter = FilterManager.getInstance().getFilterDefault();
        mSpinnerAccount.setSelectItem(null);
        mFragmentChartPager = FragmentChartPager_.builder().mFilter(mFilter).mChartType(typeChart).build();
        onReplaceFragment(mFragmentChartPager, null);
    }

    /**
     * Load all records Exchange
     */
    public void onLoadFragmentExchanges() {
        mFilter = FilterManager.getInstance().getFilterDefault();
        mSpinnerAccount.setSelectItem(null);
        onLoadFragmentExchange(mFilter);
    }

    /**
     * Load all records Exchange by account
     */
    public void onLoadFragmentExchangesByAccount(String idAccount) {
        mFilter = FilterManager.getInstance().getFilterDefaultAccount(idAccount);
        mSpinnerAccount.setSelectItem(idAccount);
        onLoadFragmentExchange(mFilter);
    }

    /**
     * load filter
     *
     * @param filter
     */
    private void onLoadFragmentExchange(Filter filter) {
        mFragmentExchangesPager = FragmentExchangesPager_.builder().mFilter(filter).build();
        onReplaceFragment(mFragmentExchangesPager, null);
    }

    /**
     * Load fragment dashboard account
     */
    public void onLoadFragmentDashboard() {
        mFragmentDashboard = FragmentDashboard_.builder().build();
        onReplaceFragment(mFragmentDashboard, DASHBOARD);
    }

    /**
     * load fragment list exchanges loop
     */
    public void onLoadFragmentLoopExchange() {
        mFragmentLoopExchange = FragmentLoopExchange_.builder().build();
        onReplaceFragment(mFragmentLoopExchange, null);
    }

    private void onReplaceFragment(Fragment fragment, String TAG) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContent, fragment, TAG);
        fragmentTransaction.commit();
    }

    // Change Menu items
    @Override
    public void loadMenuItemFragmentDashboard() {
        setTitle(getString(R.string.main_account));
        spinner.setVisibility(View.GONE);
        imgDateFilter.setVisibility(View.GONE);
        imgSettingAccount.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadMenuItemFragmentExchanges() {
        setTitle(null);
        spinner.setVisibility(View.VISIBLE);
        imgDateFilter.setVisibility(View.VISIBLE);
        imgSettingAccount.setVisibility(View.GONE);
    }

    @Override
    public void loadMenuItemFragmentLoop() {
        setTitle(null);
        spinner.setVisibility(View.GONE);
        imgDateFilter.setVisibility(View.GONE);
        imgSettingAccount.setVisibility(View.GONE);
    }

    @Override
    public void loadMenuItemFragmentChart() {
        setTitle(null);
        spinner.setVisibility(View.VISIBLE);
        imgDateFilter.setVisibility(View.VISIBLE);
        imgSettingAccount.setVisibility(View.GONE);
    }

    public void onChangeFilterCustom() {
        mDialogCustomFilter.show(mFragmentManager, TAG);
        mDialogCustomFilter.registerFilterListener(new DialogCustomFilter.FilterListener() {
            @Override
            public void onResultDate(Date fromDate, Date toDate) {
                mFilter.setFormDate(fromDate);
                mFilter.setToDate(toDate);
                mFilter.setViewType(FilterType.CUSTOM);
                reloadFragmentFilter();
            }
        });
    }

    private void reloadFragmentFilter() {
        if (mFragmentChartPager != null) {
            mFragmentChartPager.onReloadFragmentPager();
        }
        if (mFragmentExchangesPager != null) {
            mFragmentExchangesPager.onReloadFragmentPager();
        }
    }
}
