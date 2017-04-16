package com.dut.moneytracker.ui.exchanges;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.exchanges.ExchangeRecodesAdapter;
import com.dut.moneytracker.constant.FilterType;
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.models.FilterManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */
@EFragment(R.layout.fragment_exchange)
public class FragmentExchanges extends BaseFragment {
    @ViewById(R.id.llPrevious)
    LinearLayout llPrevious;
    @ViewById(R.id.llNext)
    LinearLayout llNext;
    @ViewById(R.id.tvInformationExchange)
    TextView tvInformationExchange;
    @ViewById(R.id.recyclerExchange)
    RecyclerView recyclerExchange;
    @FragmentArg
    Filter mFilter;
    private ExchangeRecodesAdapter mAdapter;
    private RealmResults<Exchange> mExchanges;
    private int positionItem;

    @AfterViews
    void init() {
        initRecyclerView();
        changeDateLabel();
    }


    private void initRecyclerView() {
        mExchanges = ExchangeManger.getInstance().getExchanges(mFilter);
        mAdapter = new ExchangeRecodesAdapter(getContext(), mExchanges);
        recyclerExchange.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerExchange.setAdapter(mAdapter);
        recyclerExchange.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), new ClickItemListener() {
            @Override
            public void onClick(View view, int position) {
                positionItem = position;
                ActivityDetailExchange_.intent(FragmentExchanges.this).mExchange((Exchange) mAdapter.getItem(position)).startForResult(IntentCode.DETAIL_EXCHANGE);
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
                ExchangeManger.getInstance().insertOrUpdate(exchange);
                break;
            case IntentCode.DELETE_EXCHANGE:
                ExchangeManger.getInstance().deleteExchangeById(((Exchange) mAdapter.getItem(positionItem)).getId());
        }
    }

    @Click(R.id.llNext)
    void onClickNext() {
        sendBroadcastFilter(1);
    }

    @Click(R.id.llPrevious)
    void onClickPrev() {
        sendBroadcastFilter(-1);
    }


    public void changeDateLabel() {
        String label = FilterManager.getInstance().getLabel(mFilter, mExchanges);
        if (mFilter.getTypeFilter() == FilterType.CUSTOM || mFilter.getTypeFilter() == FilterType.ALL) {
            llNext.setVisibility(View.GONE);
            llPrevious.setVisibility(View.GONE);
        } else {
            llNext.setVisibility(View.VISIBLE);
            llPrevious.setVisibility(View.VISIBLE);
        }
        tvInformationExchange.setText(label);
    }

    private void sendBroadcastFilter(int step) {
        Intent intent = new Intent(getContext().getString(R.string.broadcast_filter));
        intent.putExtra(getContext().getString(R.string.step_filter), step);
        getContext().sendBroadcast(intent);
    }
}
