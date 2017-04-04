package com.dut.moneytracker.fragment.loopexchange;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.ActivityAddLoopExchange_;
import com.dut.moneytracker.activities.ActivityDetailLoopExchange_;
import com.dut.moneytracker.activities.MainActivity_;
import com.dut.moneytracker.adapter.loop.LoopExchangeAdapter;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.fragment.base.BaseFragment;
import com.dut.moneytracker.models.realms.ExchangeLoopManager;
import com.dut.moneytracker.objects.ExchangeLooper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 19/03/2017.
 */
@EFragment(R.layout.fragment_default_exchange)
public class FragmentLoopExchange extends BaseFragment implements LoopExchangeAdapter.ClickItemListener {
    private static final String TAG = FragmentLoopExchange.class.getSimpleName();
    @ViewById(R.id.recyclerDefaultExchange)
    RecyclerView mRecyclerView;
    private LoopExchangeAdapter mAdapter;

    @AfterViews
    void init() {
        initLoopAdapter();
        loadListLoopExchange();
    }

    private void initLoopAdapter() {
        mAdapter = new LoopExchangeAdapter(getContext(), new ArrayList());
        mAdapter.registerItemClick(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadListLoopExchange() {
        List<ExchangeLooper> exchangeLoopers = ExchangeLoopManager.getInstance().getListLoopExchange();
        mAdapter.addItems(exchangeLoopers);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity_) getActivity()).checkFragmentLoop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity_) getActivity()).checkFragmentLoop();
    }

    @Click(R.id.fab)
    void onClickAddExchange() {
        ActivityAddLoopExchange_.intent(FragmentLoopExchange.this).startForResult(RequestCode.ADD_LOOP_EXCHANGE);
    }

    @OnActivityResult(RequestCode.ADD_LOOP_EXCHANGE)
    void onResultAdd(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        ExchangeLooper exchangeLooper = data.getParcelableExtra(getString(R.string.extra_loop_exchange));
        if (exchangeLooper != null) {
            mAdapter.add(exchangeLooper);
        }
    }

    @OnActivityResult(RequestCode.DETAIL_LOOP_EXCHANGE)
    void onResultDetail() {
        List<ExchangeLooper> exchangeLoopers = ExchangeLoopManager.getInstance().getListLoopExchange();
        mAdapter.setObjects(exchangeLoopers);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickItem(int position) {
        ActivityDetailLoopExchange_.intent(FragmentLoopExchange.this).mExchangeLoop((ExchangeLooper) mAdapter.getItem(position))
                .startForResult(RequestCode.DETAIL_LOOP_EXCHANGE);
    }

    @Override
    public void onClickSwichItem(int position) {

    }
}
