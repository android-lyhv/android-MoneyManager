package com.dut.moneytracker.fragment.exchanges;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.ActivityDetailExchange_;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.exchanges.ExchangeRecyclerAdapter;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.constant.TypeView;
import com.dut.moneytracker.fragment.base.BaseFragment;
import com.dut.moneytracker.models.FilterManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.Filter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

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
    private ExchangeRecyclerAdapter mExchangeRecyclerAdapter;
    private List<Exchange> mExchanges;

    @AfterViews
    void init() {
        mExchanges = ExchangeManger.getInstance().getExchanges(mFilter);
        initRecyclerView();
        updateExchangeRecyclerView();
    }


    private void initRecyclerView() {
        mExchangeRecyclerAdapter = new ExchangeRecyclerAdapter(getContext(), new ArrayList());
        recyclerExchange.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerExchange.setAdapter(mExchangeRecyclerAdapter);
        recyclerExchange.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), new ClickItemListener() {
            @Override
            public void onClick(View view, int position) {
                ActivityDetailExchange_.intent(FragmentExchanges.this).mExchange((Exchange) mExchangeRecyclerAdapter.getItem(position)).startForResult(ResultCode.DETAIL_EXCHANGE);
            }
        }));
    }

    @OnActivityResult(ResultCode.DETAIL_EXCHANGE)
    void onResult() {
        mExchangeRecyclerAdapter.notifyDataSetChanged();
    }

    @Click(R.id.llNext)
    void onClickNext() {
        sendBroadcastFilter(1);
    }

    @Click(R.id.llPrevious)
    void onClickPrev() {
        sendBroadcastFilter(-1);
    }

    private void updateExchangeRecyclerView() {
        mExchangeRecyclerAdapter.addItems(mExchanges);
        changeDateLabel();
    }

    public void changeDateLabel() {
        String label = FilterManager.getInstance().getLabel(mFilter, mExchanges);
        if (mFilter.getViewType() == TypeView.CUSTOM || mFilter.getViewType() == TypeView.ALL) {
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
