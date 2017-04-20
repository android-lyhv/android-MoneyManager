package com.dut.moneytracker.ui.charts.category;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.exchanges.ExchangeRecodesAdapter;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.Filter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/04/2017.
 */
@EActivity(R.layout.activity_list_exchange_debit)
public class ActivityExchangesCategory extends AppCompatActivity {
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.recyclerExchange)
    RecyclerView recyclerExchange;
    @Extra
    Filter mFilter;
    @Extra
    Category mCategory;
    private ExchangeRecodesAdapter mAdapter;

    @AfterViews
    void init() {
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        setTitle(getString(R.string.toobar_title_exchange_cagegory));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
    }

    private void initRecyclerView() {
        final RealmResults<Exchange> mExchanges = ExchangeManger.getInstance().getExchanges(mFilter, mCategory);
        mAdapter = new ExchangeRecodesAdapter(this, mExchanges);
        recyclerExchange.setLayoutManager(new LinearLayoutManager(this));
        recyclerExchange.setAdapter(mAdapter);
        mExchanges.addChangeListener(new RealmChangeListener<RealmResults<Exchange>>() {
            @Override
            public void onChange(RealmResults<Exchange> element) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @OptionsItem(android.R.id.home)
    void onClickClose() {
        finish();
    }
}
