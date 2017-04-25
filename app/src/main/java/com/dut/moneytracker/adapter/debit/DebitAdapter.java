package com.dut.moneytracker.adapter.debit;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.base.BaseRecyclerAdapter;
import com.dut.moneytracker.constant.DebitType;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Debit;
import com.dut.moneytracker.utils.DateTimeUtils;

import java.math.BigDecimal;
import java.util.Locale;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 13/04/2017.
 */

public class DebitAdapter extends BaseRecyclerAdapter {
    public interface ClickDebitListener {
        void onClickDetail(Debit debit);

        void onClickViewExchange(Debit debit);

        void onClickCheckDebit(Debit debit);
    }

    private ClickDebitListener mClickDebitListener;

    public void registerClickDebit(ClickDebitListener clickDebitListener) {
        mClickDebitListener = clickDebitListener;
    }

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

    class ItemDebitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout llDebit;
        TextView tvTitleDebit;
        TextView tvDescriptionDebit;
        TextView tvStartDebit;
        TextView tvEndDate;
        TextView tvRemindAmount;
        ImageView imgViewExchangeDebit;
        ImageView imgCheckDebit;
        ProgressBar progressBarPartial;

        public ItemDebitViewHolder(View itemView) {
            super(itemView);
            llDebit = (LinearLayout) itemView.findViewById(R.id.llDebit);
            llDebit.setOnClickListener(this);
            tvTitleDebit = (TextView) itemView.findViewById(R.id.tvTitleDebit);
            tvDescriptionDebit = (TextView) itemView.findViewById(R.id.tvDescriptionDebit);
            tvStartDebit = (TextView) itemView.findViewById(R.id.tvStartDebit);
            tvEndDate = (TextView) itemView.findViewById(R.id.tvEndDebit);
            tvRemindAmount = (TextView) itemView.findViewById(R.id.tvRemindAmount);
            imgViewExchangeDebit = (ImageView) itemView.findViewById(R.id.imgViewExchangeDebit);
            imgCheckDebit = (ImageView) itemView.findViewById(R.id.imgCheckDebit);
            progressBarPartial = (ProgressBar) itemView.findViewById(R.id.progressBarPartial);
            imgViewExchangeDebit.setOnClickListener(this);
            imgCheckDebit.setOnClickListener(this);
        }

        private void onBind(Debit debit) {
            if (debit.getTypeDebit() == DebitType.LEND) {
                tvTitleDebit.setText(String.format(Locale.US, "%s -> Tôi", debit.getName()));
            } else {
                tvTitleDebit.setText(String.format(Locale.US, "Tôi -> %s", debit.getName()));
            }
            tvDescriptionDebit.setText(debit.getDescription());
            tvStartDebit.setText(DateTimeUtils.getInstance().getStringDateUs(debit.getStartDate()));
            tvEndDate.setText(String.format(Locale.US, "Hết hạn %s", DateTimeUtils.getInstance().getStringDateUs(debit.getEndDate())));
            onLoadRemindAmount(debit);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgViewExchangeDebit:
                    mClickDebitListener.onClickViewExchange((Debit) getItem(getAdapterPosition()));
                    break;
                case R.id.imgCheckDebit:
                    mClickDebitListener.onClickCheckDebit((Debit) getItem(getAdapterPosition()));
                    break;
                case R.id.llDebit:
                    mClickDebitListener.onClickDetail((Debit) getItem(getAdapterPosition()));
            }
        }

        private void onLoadRemindAmount(Debit debit) {
            String amount = ExchangeManger.getInstance().getAmountExchangeByDebit(debit.getId());
            BigDecimal bigDecimal = new BigDecimal(debit.getAmount());
            bigDecimal = bigDecimal.add(new BigDecimal(amount));
            String value = CurrencyUtils.getInstance().getStringMoneyFormat(bigDecimal.toString(), CurrencyUtils.DEFAULT_CURRENCY_CODE);
            if (value.startsWith("-")) {
                value = value.substring(1);
            }
            tvRemindAmount.setText(String.format(Locale.US, "Còn lại: %s", value));
            if (debit.isClose()) {
                tvRemindAmount.setText(R.string.debit_colse);
                tvRemindAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                progressBarPartial.setProgress(progressBarPartial.getMax());
            } else {
                loadProgressBar(amount, debit.getAmount());
            }
        }

        private void loadProgressBar(String amountPartial, String amountDebit) {
            float amount = CurrencyUtils.getInstance().getFloatMoney(amountPartial);
            float amountTotal = CurrencyUtils.getInstance().getFloatMoney(amountDebit);
            int distance = (int) (Math.abs(amount / amountTotal) * 100);
            if (amount != 0 && distance == 0) {
                distance = 1;
            }
            if (distance == progressBarPartial.getMax()) {
                tvRemindAmount.setText(R.string.debit_colse);
                tvRemindAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                imgCheckDebit.setEnabled(false);
            }
            progressBarPartial.setProgress(distance);
        }

    }
}
