package com.dut.moneytracker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.MainActivity_;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 19/03/2017.
 */

public class FragmentLoopExchange extends BaseFragment {
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_default_exchange, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerDefaultExchange);
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
}
