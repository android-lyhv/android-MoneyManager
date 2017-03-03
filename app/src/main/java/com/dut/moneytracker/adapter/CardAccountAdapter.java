package com.dut.moneytracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dut.moneytracker.R;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 24/02/2017.
 */

public class CardAccountAdapter extends RecyclerView.Adapter {
    private Context mContext;

    public CardAccountAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card_account_recycler, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class CardHolder extends RecyclerView.ViewHolder {

         CardHolder(View itemView) {
            super(itemView);
        }
    }
}
