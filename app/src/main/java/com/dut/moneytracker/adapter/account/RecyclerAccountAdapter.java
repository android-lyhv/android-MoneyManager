package com.dut.moneytracker.adapter.account;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.BaseRecyclerAdapter;
import com.dut.moneytracker.objects.Account;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/04/2017.
 */

public class RecyclerAccountAdapter extends BaseRecyclerAdapter {
    public RecyclerAccountAdapter(Context context, List objects) {
        super(context, objects);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_account, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AccountViewHolder) holder).onBind((Account) getItem(position));
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgColor;
        private TextView tvNameAccount;

        public AccountViewHolder(View itemView) {
            super(itemView);
            imgColor = (ImageView) itemView.findViewById(R.id.imgColor);
            tvNameAccount = (TextView) itemView.findViewById(R.id.tvNameAccount);
        }

        public void onBind(Account account) {
            tvNameAccount.setText(account.getName());
            GradientDrawable shapeDrawable = (GradientDrawable) imgColor.getBackground();
            shapeDrawable.setColor(Color.parseColor(account.getColorHex()));
            imgColor.setBackground(shapeDrawable);
        }
    }
}
