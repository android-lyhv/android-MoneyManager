package com.dut.moneytracker.adapter.exchanges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.base.BaseRecyclerAdapter;
import com.dut.moneytracker.objects.Exchange;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */

public class ExchangeRecodesAdapter extends BaseRecyclerAdapter {
    public ExchangeRecodesAdapter(Context context, RealmResults objects) {
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
