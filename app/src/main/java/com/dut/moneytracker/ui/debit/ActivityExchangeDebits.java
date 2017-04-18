package com.dut.moneytracker.ui.debit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.exchanges.ExchangeRecodesAdapter;
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Debit;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.ui.exchanges.ActivityDetailExchange_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/04/2017.
 */
@EActivity(R.layout.activity_list_exchange_debit)
public class ActivityExchangeDebits extends AppCompatActivity {
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.recyclerExchange)
    RecyclerView recyclerExchange;
    @Extra
    Debit mDebit;
    private ExchangeRecodesAdapter mAdapter;
    private int positionItem;

    @AfterViews
    void init() {
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        setTitle(getString(R.string.toobar_title_exchange_debit));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
    }

    private void initRecyclerView() {
        RealmResults<Exchange> mExchanges = ExchangeManger.getInstance().onLoadExchangeByDebit(mDebit.getId());
        mAdapter = new ExchangeRecodesAdapter(this, mExchanges);
        recyclerExchange.setLayoutManager(new LinearLayoutManager(this));
        recyclerExchange.setAdapter(mAdapter);
        recyclerExchange.addOnItemTouchListener(new ClickItemRecyclerView(this, new ClickItemListener() {
            @Override
            public void onClick(View view, int position) {
                positionItem = position;
                ActivityDetailExchange_.intent(ActivityExchangeDebits.this).mExchange((Exchange) mAdapter.getItem(position)).startForResult(IntentCode.DETAIL_EXCHANGE);
            }
        }));
        mExchanges.addChangeListener(new RealmChangeListener<RealmResults<Exchange>>() {
            @Override
            public void onChange(RealmResults<Exchange> element) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnActivityResult(IntentCode.DETAIL_EXCHANGE)
    void onResult(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Exchange exchange = data.getParcelableExtra(getString(R.string.extra_edit_exchange));
        switch (resultCode) {
            case IntentCode.EDIT_EXCHANGE:
                ExchangeManger.getInstance().insertOrUpdate(getApplicationContext(),exchange);
                break;
            case IntentCode.DELETE_EXCHANGE:
                ExchangeManger.getInstance().deleteExchangeById(((Exchange) mAdapter.getItem(positionItem)).getId());
        }
    }

    @OptionsItem(android.R.id.home)
    void onClickClose() {
        finish();
    }
}
