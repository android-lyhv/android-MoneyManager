package com.dut.moneytracker.ui.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.account.RecyclerAccountAdapter;
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.ui.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/04/2017.
 */
@EActivity(R.layout.activity_account)
public class ActivityAccounts extends AppCompatActivity {
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.recyclerViewAccount)
    RecyclerView mRecyclerViewAccounts;
    private RecyclerAccountAdapter mAdapter;
    private int positionAccount = -1;

    @AfterViews
    void init() {
        initToolbar();
        onLoadAccounts();
    }

    private void initToolbar() {
        setTitle(getString(R.string.manger_accounts));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void onLoadAccounts() {
        RealmResults<Account> accounts = AccountManager.getInstance().loadAccountsAsync();
        mAdapter = new RecyclerAccountAdapter(this, accounts);
        mRecyclerViewAccounts.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewAccounts.setAdapter(mAdapter);
        mRecyclerViewAccounts.addOnItemTouchListener(new ClickItemRecyclerView(this, new ClickItemListener() {
            @Override
            public void onClick(View view, int position) {
                positionAccount = position;
                ActivityDetailAccount_.intent(ActivityAccounts.this).mAccount((Account) mAdapter.getItem(position)).startForResult(IntentCode.DETAIL_ACCOUNT);
            }
        }));
        accounts.addChangeListener(new RealmChangeListener<RealmResults<Account>>() {
            @Override
            public void onChange(RealmResults<Account> element) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnActivityResult(IntentCode.DETAIL_ACCOUNT)
    void onResultEditAccount(int resultCode, Intent data) {
        if (resultCode == IntentCode.DELETE_ACCOUNT) {
            Intent intent = new Intent(MainActivity.RECEIVER_DELETE_ACCOUNT);
            intent.putExtra(getString(R.string.position_account_delete), positionAccount);
            sendBroadcast(intent);
        }
        if (resultCode == IntentCode.EDIT_ACCOUNT) {
            if (data == null) {
                return;
            }
            Account account = data.getParcelableExtra(getString(R.string.extra_account));
            AccountManager.getInstance().insertOrUpdate(account);
            //Send broadcast
            Intent intent = new Intent(MainActivity.RECEIVER_EDIT_ACCOUNT);
            intent.putExtra(getString(R.string.position_account_edit), positionAccount);
            sendBroadcast(intent);
        }
    }

    @OnActivityResult(IntentCode.ADD_NEW_ACCOUNT)
    void onResultAddNewAccount(Intent data) {
        if (data == null) {
            return;
        }
        Account account = data.getParcelableExtra(getString(R.string.extra_account));
        AccountManager.getInstance().insertOrUpdate(account);
        // send broadcast
        Intent intent = new Intent(MainActivity.RECEIVER_ADD_ACCOUNT);
        intent.putExtra(getString(R.string.extra_account), account);
        sendBroadcast(intent);
    }

    @Click(R.id.fab)
    void onClickAddNewAccount() {
        ActivityAddNewAccount_.intent(ActivityAccounts.this).startForResult(IntentCode.ADD_NEW_ACCOUNT);
    }

    @OptionsItem(android.R.id.home)
    void onClose() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
