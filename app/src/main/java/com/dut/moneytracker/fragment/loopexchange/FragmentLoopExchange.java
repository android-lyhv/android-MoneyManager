package com.dut.moneytracker.fragment.loopexchange;

import android.support.v7.widget.RecyclerView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.ActivityAddLoopExchange_;
import com.dut.moneytracker.activities.MainActivity_;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.fragment.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 19/03/2017.
 */
@EFragment(R.layout.fragment_default_exchange)
public class FragmentLoopExchange extends BaseFragment {
    @ViewById(R.id.recyclerDefaultExchange)
    RecyclerView mRecyclerView;

    @AfterViews
    void init() {

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
        ActivityAddLoopExchange_.intent(this).startForResult(RequestCode.ADD_LOOP_EXCHANGE);
    }

    @OnActivityResult(RequestCode.ADD_LOOP_EXCHANGE)
    void onResult() {
        //TODO result add or edit loop exchange
    }
}
