package com.dut.moneytracker.ui.exchangeloop;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.loop.LoopExchangeAdapter;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.models.realms.ExchangeLoopManager;
import com.dut.moneytracker.objects.ExchangeLooper;
import com.dut.moneytracker.recevier.GenerateManager;
import com.dut.moneytracker.ui.MainActivity_;
import com.dut.moneytracker.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import io.realm.RealmResults;

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
    private GenerateManager mGnerateManager;

    @AfterViews
    void init() {
        mGnerateManager = new GenerateManager(getActivity().getApplicationContext());
        initLoopAdapter();
    }

    private void initLoopAdapter() {
        RealmResults<ExchangeLooper> exchangeLoopers = ExchangeLoopManager.getInstance(getActivity().getApplicationContext()).getListLoopExchange();
        mAdapter = new LoopExchangeAdapter(getContext(), exchangeLoopers);
        mAdapter.registerItemClick(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity_) getActivity()).loadMenuItemFragmentLoop();
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
         //   mAdapter.add(exchangeLooper);
        }
    }

    @OnActivityResult(RequestCode.DETAIL_LOOP_EXCHANGE)
    void onResultDetail() {
        RealmResults<ExchangeLooper> exchangeLoopers = ExchangeLoopManager.getInstance(getActivity().getApplicationContext()).getListLoopExchange();
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
