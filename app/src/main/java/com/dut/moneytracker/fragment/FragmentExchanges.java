package com.dut.moneytracker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.ActivityDetailExchange;
import com.dut.moneytracker.activities.MainActivity;
import com.dut.moneytracker.adapter.exchanges.ExchangeRecyclerAdapter;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.fragment.dashboard.FragmentChildExchangeTab;
import com.dut.moneytracker.models.FilterManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */

public class FragmentExchanges extends BaseFragment implements View.OnClickListener {
    private static final String TAG = FragmentChildExchangeTab.class.getSimpleName();
    private LinearLayout llPrevious;
    private LinearLayout llNext;
    private TextView tvInformationExchange;
    private RecyclerView recyclerExchange;
    private ExchangeRecyclerAdapter mExchangeRecyclerAdapter;
    private List<Exchange> mExchanges = new ArrayList<>();
    private Filter mFilter;

    public void setExchanges(List<Exchange> exchanges, Filter filter) {
        mExchanges = exchanges;
        mFilter = filter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        upDateLabel();
        onLoadRecyclerView();
    }

    private void initView(View view) {
        llPrevious = (LinearLayout) view.findViewById(R.id.llPrevious);
        llNext = (LinearLayout) view.findViewById(R.id.llNext);
        tvInformationExchange = (TextView) view.findViewById(R.id.tvInformationExchange);
        llPrevious.setOnClickListener(this);
        llNext.setOnClickListener(this);
        recyclerExchange = (RecyclerView) view.findViewById(R.id.recyclerExchange);
    }

    private void onLoadRecyclerView() {
        mExchangeRecyclerAdapter = new ExchangeRecyclerAdapter(getContext(), mExchanges);
        recyclerExchange.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerExchange.setAdapter(mExchangeRecyclerAdapter);
    }

    public void onShowDetailExchange(Exchange exchange) {
        Intent intent = new Intent(getActivity(), ActivityDetailExchange.class);
        intent.putExtra(getString(R.string.extra_account), exchange);
        startActivityForResult(intent, RequestCode.DETAIL_EXCHANGE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPrevious:
                mFilter = FilterManager.getInstance().changeFilter(mFilter, -1);
                updateListExchanges(mFilter);
                break;
            case R.id.llNext:
                mFilter = FilterManager.getInstance().changeFilter(mFilter, 1);
                updateListExchanges(mFilter);
                break;
        }
    }

    public void updateListExchanges(Filter filter) {
        mFilter = filter;
        mExchanges = ExchangeManger.getInstance().getExchanges(mFilter);
        mExchangeRecyclerAdapter.addItems(mExchanges);
        upDateLabel();
    }

    public void upDateLabel() {
        String label = FilterManager.getInstance().getLabel(mFilter, mExchanges);
        tvInformationExchange.setText(label);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).checkStatus();
    }
}
