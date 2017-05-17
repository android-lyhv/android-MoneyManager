package com.dut.moneytracker.ui.exchangeloop;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.loop.LoopExchangeAdapter;
import com.dut.moneytracker.models.realms.ExchangeLoopManager;
import com.dut.moneytracker.objects.ExchangeLooper;
import com.dut.moneytracker.ui.MainActivity_;
import com.dut.moneytracker.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 19/03/2017.
 */
@EFragment(R.layout.fragment_loop_exchange)
public class FragmentLoopExchange extends BaseFragment implements LoopExchangeAdapter.ClickItemListener {
    private static final String TAG = FragmentLoopExchange.class.getSimpleName();
    @ViewById(R.id.recyclerDefaultExchange)
    RecyclerView mRecyclerView;
    private LoopExchangeAdapter mAdapter;
    private RealmResults<ExchangeLooper> mExchangeLoops;

    @AfterViews
    void init() {
        initLoopAdapter();
    }

    private void initLoopAdapter() {
        mExchangeLoops = ExchangeLoopManager.getInstance().onLoadSyncExchangeLooper();
        setUpAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity_) getActivity()).loadMenuItemFragmentLoop();
    }

    @Click(R.id.fab)
    void onClickAddExchange() {
        ActivityAddLoopExchange_.intent(FragmentLoopExchange.this).start();
    }

    @Override
    public void onClickItem(int position) {
        ActivityDetailLoopExchange_.intent(FragmentLoopExchange.this).mExchangeLoop((ExchangeLooper) mAdapter.getItem(position)).start();
    }

    public void onFilterLoopExchange(int idFilter) {
        mExchangeLoops = ExchangeLoopManager.getInstance().onLoadSyncExchangeLooper(idFilter);
        setUpAdapter();
    }

    private void setUpAdapter() {
        mAdapter = new LoopExchangeAdapter(getContext(), mExchangeLoops);
        mAdapter.registerItemClick(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mExchangeLoops.addChangeListener(new RealmChangeListener<RealmResults<ExchangeLooper>>() {
            @Override
            public void onChange(RealmResults<ExchangeLooper> element) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
