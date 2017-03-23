package com.dut.moneytracker.adapter.loop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dut.moneytracker.adapter.BaseRecyclerAdapter;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 20/03/2017.
 */

public class LoopExchangeAdapter extends BaseRecyclerAdapter {
    public LoopExchangeAdapter(Context context, List objects) {
        super(context, objects);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
    public class ItemLoopExchange extends RecyclerView.ViewHolder {
        public ItemLoopExchange(View itemView) {
            super(itemView);
        }
    }
}
