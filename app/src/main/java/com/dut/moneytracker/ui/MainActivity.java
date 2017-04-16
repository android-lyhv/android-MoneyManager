package com.dut.moneytracker.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.dut.moneytracker.dialogs.DialogPickFilter_;
import com.dut.moneytracker.models.FilterManager;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.ui.account.ActivityAccounts_;
import com.dut.moneytracker.ui.account.ActivityDetailAccount_;
import com.dut.moneytracker.ui.base.SpinnerAccountManger;
import com.dut.moneytracker.ui.charts.exchange.FragmentChartExchangePager;
import com.dut.moneytracker.ui.charts.exchange.FragmentChartExchangePager_;
import com.dut.moneytracker.ui.dashboard.FragmentDashboard;
import com.dut.moneytracker.ui.dashboard.FragmentDashboard_;
import com.dut.moneytracker.ui.debit.FragmentDebit;
import com.dut.moneytracker.ui.debit.FragmentDebit_;
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
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

import static com.dut.moneytracker.ui.MainActivity.FragmentTag.DEFAULT;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainListener, NavigationView.OnNavigationItemSelectedListener {
    public static final String RECEIVER_DELETE_ACCOUNT = "RECEIVER_DELETE_ACCOUNT";
    public static final String RECEIVER_ADD_ACCOUNT = "RECEIVER_ADD_ACCOUNT";
    public static final String RECEIVER_EDIT_ACCOUNT = "RECEIVER_EDIT_ACCOUNT";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String DASHBOARD = "DASHBOARD";
    private static final String CHART = "CHART";
    private static final String EXCHANGE_RECORDS = "EXCHANGE";

    public enum FragmentTag {
        DEFAULT, DASHBOARD, RECORDS, EXCHANGE_LOOPS, PROFILE, CHART_INCOME, CHART_EXPENSES, ACCOUNT, DEBIT
    }

    FragmentTag mFragmentTag = DEFAULT;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @ViewById(R.id.nav_view)
    NavigationView mNavigationView;
    private CircleImageView mImgUserLogo;
    private TextView mTvUserName;
    private TextView mTvEmail;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.spinner)
    AppCompatSpinner spinner;
    @ViewById(R.id.imgDateFilter)
    ImageView imgDateFilter;
    @ViewById(R.id.imgSettingAccount)
    ImageView imgSettingAccount;
    @ViewById(R.id.imgSortingDebit)
    ImageView imgSorDebit;
    private DialogPickFilter mDialogPickFilterTime;
    private DialogCustomFilter mDialogCustomFilter;
    //model
    FragmentManager mFragmentManager = getSupportFragmentManager();
    FragmentDashboard mFragmentDashboard;
    FragmentLoopExchange mFragmentLoopExchange;
    FragmentExchangesPager mFragmentExchangesPager;
    FragmentChartExchangePager mFragmentChartExchangePager;
    FragmentDebit mFragmentDebit;
    SpinnerAccountManger mSpinnerAccount;
    private Account mAccount;
    private int positionAccount;
    private Filter mFilter;
    private Handler mHandler = new Handler();
    private BroadcastReceiver mReceiverAccountsChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            initSpinnerAccount();
            if (intent == null || !isFragmentDashboard() || mFragmentDashboard == null) {
                return;
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.equals(intent.getAction(), RECEIVER_DELETE_ACCOUNT)) {
                        int positionDelete = intent.getIntExtra(getString(R.string.position_account_delete), -1);
                        reloadDeleteTabAccount(positionDelete);
                    }
                    if (TextUtils.equals(intent.getAction(), RECEIVER_ADD_ACCOUNT)) {
                        Account account = intent.getParcelableExtra(getString(R.string.extra_account));
                        reloadAddTabAccount(account);
                    }
                    if (TextUtils.equals(intent.getAction(), RECEIVER_EDIT_ACCOUNT)) {
                        int positionEdit = intent.getIntExtra(getString(R.string.position_account_edit), -1);
                        reloadEditTabAccount(positionEdit);
                    }
                }
            }, FragmentDashboard.DELAY);
        }

        private void reloadDeleteTabAccount(int positionDelete) {
            mFragmentDashboard.deleteFragmentAccount(positionDelete);
        }

        private void reloadAddTabAccount(Account account) {
            mFragmentDashboard.addFragmentAccount(account);
        }

        private void reloadEditTabAccount(final int positionEdit) {
            mFragmentDashboard.notifyDataSetChanged(positionEdit);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ADD_ACCOUNT);
        intentFilter.addAction(RECEIVER_DELETE_ACCOUNT);
        intentFilter.addAction(RECEIVER_EDIT_ACCOUNT);
        registerReceiver(mReceiverAccountsChange, intentFilter);
    }

    @AfterViews
    void init() {
        mDialogPickFilterTime = DialogPickFilter_.builder().build();
        mDialogCustomFilter = DialogCustomFilter_.builder().build();
        initView();
        initHeaderView();
        onLoadProfile();
        onLoadFragmentDashboard();
    }

    private void initHeaderView() {
        View header = mNavigationView.getHeaderView(0);
        mTvUserName = (TextView) header.findViewById(R.id.tvUserName);
        mTvEmail = (TextView) header.findViewById(R.id.tvEmail);
        mImgUserLogo = (CircleImageView) header.findViewById(R.id.imgProfile);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationView.setCheckedItem(0);
                mFragmentTag = FragmentTag.PROFILE;
                onCloseNavigation();
            }
        });
    }

    private void initView() {
        mNavigationView.setNavigationItemSelectedListener(this);
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
                    case RECORDS:
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
                    case DEBIT:
                        onLoadFragmentDebit();
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
        initSpinnerAccount();
    }


    // Change filter account
    private void initSpinnerAccount() {
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

    public void registerAccount(Account account, int position) {
        mAccount = account;
        positionAccount = position;
    }


    private void onLoadProfile() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Glide.with(this).load(firebaseAuth.getCurrentUser().getPhotoUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .into(mImgUserLogo);
        mTvUserName.setText(String.valueOf(firebaseAuth.getCurrentUser().getDisplayName()));
        mTvEmail.setText(String.valueOf(firebaseAuth.getCurrentUser().getEmail()));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (isFragmentDashboard()) {
                super.onBackPressed();
            } else {
                onLoadFragmentDashboard();
            }
        }
    }

    @Click(R.id.imgDateFilter)
    void onClickFilter() {
        mDialogPickFilterTime.show(getFragmentManager(), TAG);
        mDialogPickFilterTime.registerFilter(mFilter, new DialogPickFilter.FilterListener() {
            @Override
            public void onResult(Filter filter) {
                mFilter = filter;
                if (mFilter.getTypeFilter() == FilterType.CUSTOM) {
                    onShowDialogFilterCustom();
                } else {
                    mFilter.setDateFilter(new Date());
                    reloadFragmentFilter();
                }
            }
        });
    }

    @Click(R.id.imgSettingAccount)
    void onSettingAccount() {
        ActivityDetailAccount_.intent(this).mAccount(mAccount).startForResult(RequestCode.DETAIL_ACCOUNT);
    }

    @Click(R.id.imgSortingDebit)
    void onClickSortDebit() {
        //TODO short
    }

    @OnActivityResult(RequestCode.DETAIL_ACCOUNT)
    void onResultEditAccount(int resultCode, Intent data) {
        if (resultCode == ResultCode.DELETE_ACCOUNT) {
            Intent intent = new Intent(MainActivity.RECEIVER_DELETE_ACCOUNT);
            intent.putExtra(getString(R.string.position_account_delete), positionAccount);
            sendBroadcast(intent);
        }
        if (resultCode == ResultCode.EDIT_ACCOUNT) {
            if (data == null) {
                return;
            }
            Account account = data.getParcelableExtra(getString(R.string.extra_account));
            AccountManager.getInstance().insertOrUpdate(account);
            // Send broadcast;
            Intent intent = new Intent(RECEIVER_EDIT_ACCOUNT);
            intent.putExtra(getString(R.string.position_account_edit), positionAccount);
            sendBroadcast(intent);
        }
    }

    private void onCloseNavigation() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_account:
                mFragmentTag = FragmentTag.ACCOUNT;
                break;
            case R.id.nav_dashboard:
                mFragmentTag = FragmentTag.DASHBOARD;
                break;
            case R.id.nav_record:
                mFragmentTag = FragmentTag.RECORDS;
                break;
            case R.id.nav_exchange_loop:
                mFragmentTag = FragmentTag.EXCHANGE_LOOPS;
                break;
            case R.id.nav_chart_income:
                mFragmentTag = FragmentTag.CHART_INCOME;
                break;
            case R.id.nav_chart_expense:
                mFragmentTag = FragmentTag.CHART_EXPENSES;
                break;
            case R.id.nav_chart_category:
                break;
            case R.id.nav_debit:
                mFragmentTag = FragmentTag.DEBIT;
                break;
            case R.id.nav_location:
                break;
            case R.id.nav_setting:
                break;
        }
        onCloseNavigation();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ResultCode.PROFILE:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    /**
     * onLoad Fragment Debit
     */
    private void onLoadFragmentDebit() {
        mFragmentDebit = FragmentDebit_.builder().build();
        onReplaceFragment(mFragmentDebit, null);
    }

    /**
     * load piechart
     *
     * @param typeChart
     */
    public void onLoadFragmentChart(int typeChart) {
        mFilter = FilterManager.getInstance().getFilterDefault();
        mSpinnerAccount.setSelectItem(null);
        mFragmentChartExchangePager = FragmentChartExchangePager_.builder().mFilter(mFilter).mChartType(typeChart).build();
        onReplaceFragment(mFragmentChartExchangePager, CHART);
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
        onReplaceFragment(mFragmentExchangesPager, EXCHANGE_RECORDS);
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
        imgSorDebit.setVisibility(View.GONE);
    }

    @Override
    public void loadMenuItemFragmentExchanges() {
        setTitle(null);
        spinner.setVisibility(View.VISIBLE);
        imgDateFilter.setVisibility(View.VISIBLE);
        imgSettingAccount.setVisibility(View.GONE);
        imgSorDebit.setVisibility(View.GONE);
    }

    @Override
    public void loadMenuItemFragmentLoop() {
        setTitle(null);
        spinner.setVisibility(View.GONE);
        imgDateFilter.setVisibility(View.GONE);
        imgSettingAccount.setVisibility(View.GONE);
        imgSorDebit.setVisibility(View.GONE);
    }

    @Override
    public void loadMenuItemFragmentChart() {
        setTitle(null);
        spinner.setVisibility(View.VISIBLE);
        imgDateFilter.setVisibility(View.VISIBLE);
        imgSettingAccount.setVisibility(View.GONE);
        imgSorDebit.setVisibility(View.GONE);
    }

    @Override
    public void loadMenuItemFragmentDebit() {
        setTitle(getString(R.string.toobar_title_debit));
        spinner.setVisibility(View.GONE);
        imgDateFilter.setVisibility(View.GONE);
        imgSettingAccount.setVisibility(View.GONE);
        imgSorDebit.setVisibility(View.VISIBLE);
    }

    public void onShowDialogFilterCustom() {
        mDialogCustomFilter.show(mFragmentManager, TAG);
        mDialogCustomFilter.registerFilterListener(new DialogCustomFilter.FilterListener() {
            @Override
            public void onResultDate(Date fromDate, Date toDate) {
                mFilter.setFormDate(fromDate);
                mFilter.setToDate(toDate);
                mFilter.setTypeFilter(FilterType.CUSTOM);
                reloadFragmentFilter();
            }
        }, mFilter.getFormDate(), mFilter.getToDate());
    }

    private void reloadFragmentFilter() {
        if (isFragmentChart()) {
            mFragmentChartExchangePager.onReloadFragmentPager();
        }
        if (isFragmentExchangeRecord()) {
            mFragmentExchangesPager.onReloadFragmentPager();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAccount != null) {
            mAccount.removeAllChangeListeners();
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    public boolean isFragmentDashboard() {
        Fragment fragment = mFragmentManager.findFragmentByTag(DASHBOARD);
        return null != fragment;
    }

    public boolean isFragmentChart() {
        Fragment fragment = mFragmentManager.findFragmentByTag(CHART);
        return null != fragment;
    }

    public boolean isFragmentExchangeRecord() {
        Fragment fragment = mFragmentManager.findFragmentByTag(EXCHANGE_RECORDS);
        return null != fragment;
    }
}
