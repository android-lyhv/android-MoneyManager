package com.dut.moneytracker.adapter.exchanges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.BaseRecyclerAdapter;
import com.dut.moneytracker.objects.Exchange;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */

public class ExchangeRecyclerAdapter extends BaseRecyclerAdapter {
    public ExchangeRecyclerAdapter(Context context, List objects) {
        super(context, objects);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_exchange_content, parent, false);
        return new ItemSimpleExchange(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemSimpleExchange itemSimpleExchange = (ItemSimpleExchange) holder;
        itemSimpleExchange.onBind(getContext(), (Exchange) getItem(position));
    }
}