package com.dut.moneytracker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.utils.StringUtils;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 24/02/2017.
 */

public class CardAccountAdapter extends RecyclerView.Adapter<CardAccountAdapter.CardHolder> {
    private final Context mContext;
    private final List<Account> mAccounts;

    public CardAccountAdapter(Context context, List<Account> accounts) {
        mContext = context;
        mAccounts = accounts;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card_account_recycler, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        holder.setView(mAccounts.get(position));
    }

    @Override
    public int getItemCount() {
        return mAccounts == null ? 0 : mAccounts.size();
    }

    final class CardHolder extends RecyclerView.ViewHolder {
        private LinearLayout llCard;
        private TextView tvAccountName;
        private TextView tvAmount;

        CardHolder(View itemView) {
            super(itemView);
            llCard = (LinearLayout) itemView.findViewById(R.id.llCard);
            tvAccountName = (TextView) itemView.findViewById(R.id.tvAccountName);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
        }

        public void setView(Account account) {
            llCard.setBackgroundColor(Color.parseColor(account.getColorCode()));
            String money = StringUtils.getInstance().getStringMoneyType(account.getAmountAvailable(), account.getCurrencyCode());
            tvAmount.setText(money);
            tvAccountName.setText(account.getName());
        }
    }
}
