package com.dut.moneytracker.adapter.debit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.base.BaseRecyclerAdapter;
import com.dut.moneytracker.objects.Debit;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 13/04/2017.
 */

public class DebitAdapter extends BaseRecyclerAdapter {
    public DebitAdapter(Context context, RealmResults objects) {
        super(context, objects);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_debit, parent, false);
        return new ItemDebitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemDebitViewHolder) holder).onBind((Debit) getItem(position));
    }

    public class ItemDebitViewHolder extends RecyclerView.ViewHolder {

        public ItemDebitViewHolder(View itemView) {
            super(itemView);
        }

        private void onBind(Debit debit) {

        }
    }
}
