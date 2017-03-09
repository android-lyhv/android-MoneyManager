package com.dut.moneytracker.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.BaseViewPagerAdapter;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.fragment.FragmentAccount;
import com.dut.moneytracker.fragment.FragmentAllAccount;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.utils.ArgbEvaluatorColor;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFabAddExchange;
    private RelativeLayout mRlProfile;
    private ImageView imgUserLogo;
    private TextView tvUserName;
    private TextView tvEmail;
    private ViewPager mViewPagerAccount;
    private TabLayout mTabLayout;
    private FrameLayout mFrameLayout;
    private Toolbar toolbar;
    private int positionTabSelect;
    private List<Account> mAccounts;
    private BaseViewPagerAdapter mBaseViewPagerAdapter;
    private FragmentAllAccount fragmentAllAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        onLoadProfile();
        onLoadDataAccount();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFabAddExchange = (FloatingActionButton) findViewById(R.id.fab);
        mFabAddExchange.setOnClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mRlProfile = (RelativeLayout) findViewById(R.id.rlProfile);
        mRlProfile.setOnClickListener(this);
        imgUserLogo = (ImageView) findViewById(R.id.imgUserLogo);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        mViewPagerAccount = (ViewPager) findViewById(R.id.viewpager);
        mViewPagerAccount.addOnPageChangeListener(this);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.addOnTabSelectedListener(this);
        mFrameLayout = (FrameLayout) findViewById(R.id.flContent);
    }

    private void onLoadDataAccount() {
        mBaseViewPagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager());
        mAccounts = AccountManager.getInstance().getListAccount();
        int size = mAccounts.size();
        if (size > 1) {
            ArgbEvaluatorColor.getInstance().addColorCode(Color.parseColor("#FF028761"));
            fragmentAllAccount = new FragmentAllAccount();
            fragmentAllAccount.registerCardAccountListener(new FragmentAllAccount.CardAccountListener() {
                @Override
                public void onClickCardAccount(int position) {
                    mViewPagerAccount.setCurrentItem(position);
                }
            });
            fragmentAllAccount.setAccounts(mAccounts);
            mBaseViewPagerAdapter.addFragment(fragmentAllAccount, getString(R.string.tablyout_text_all_account));
        }
        for (int i = 0; i < size; i++) {
            ArgbEvaluatorColor.getInstance().addColorCode(Color.parseColor(mAccounts.get(i).getColorCode()));
            FragmentAccount mFragmentAccount = new FragmentAccount();
            mFragmentAccount.setAccount(mAccounts.get(i));
            mBaseViewPagerAdapter.addFragment(mFragmentAccount, mAccounts.get(i).getName());
        }
        mViewPagerAccount.setAdapter(mBaseViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPagerAccount);
        mViewPagerAccount.setCurrentItem(positionTabSelect);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionEditAccount:
                onEditAccount();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivityAddExchange();
                break;
            case R.id.rlProfile:
                startActivityForResult(new Intent(this, UserInformationActivity.class), RequestCode.PROFILE);
                break;
        }
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
            case ResultCode.ADD_EXCHANGE:
                onLoadDataAccount();
                break;
        }
    }

    private void onEditAccount() {
        Intent intent = new Intent(this, ActivityEditAccount.class);
        intent.putExtra(getString(R.string.extra_account), mAccounts.get(positionTabSelect));
        startActivityForResult(intent, RequestCode.EDIT_ACCOUNT);
    }

    private void startActivityAddExchange() {
        Intent intent = new Intent(this, ActivityAddExchange.class);
        intent.putExtra(getString(R.string.extra_account), mAccounts.get(positionTabSelect));
        startActivityForResult(intent, RequestCode.ADD_EXCHANGE);
    }

    private void onResultEditAccount(Intent data) {
        Account account = data.getParcelableExtra(getString(R.string.extra_account));
        AccountManager.getInstance().insertOrUpdate(account);
        onLoadDataAccount();
    }

    private void onResultLogout() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        positionTabSelect = tab.getPosition() == 0 ? 0 : tab.getPosition() - 1;
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int colorCode = ArgbEvaluatorColor.getInstance().getHeaderColor(position, positionOffset);
        mTabLayout.setBackgroundColor(colorCode);
        toolbar.setBackgroundColor(colorCode);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}