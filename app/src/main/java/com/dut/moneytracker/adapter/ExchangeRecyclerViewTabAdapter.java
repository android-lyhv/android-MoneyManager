package com.dut.moneytracker.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.base.BaseRecyclerAdapter;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.CategoryManager;
import com.dut.moneytracker.models.realms.DebitManager;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.dut.moneytracker.utils.ResourceUtils;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */

public class ExchangeRecyclerViewTabAdapter extends BaseRecyclerAdapter {

    public ExchangeRecyclerViewTabAdapter(Context context, RealmResults objects) {
        super(context, objects);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_exchange_dashboard, parent, false);
        return new ExchangeHolder(view);
    }

    @Override
    public int getItemCount() {
        return getSize() > 5 ? 5 : getSize();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExchangeHolder exchangeHolder = (ExchangeHolder) holder;
        exchangeHolder.onBind((Exchange) getItem(position));
    }

    public class ExchangeHolder extends RecyclerView.ViewHolder {
        private ImageView imgCategory;
        private TextView tvCategoryName;
        private TextView tvAccountName;
        private TextView tvDateCreated;
        private TextView tvAmount;

        public ExchangeHolder(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            tvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
            tvAccountName = (TextView) itemView.findViewById(R.id.tvAccountName);
            tvDateCreated = (TextView) itemView.findViewById(R.id.tvLastCreated);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
        }

        public void onBind(Exchange exchange) {
            tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(exchange.getAmount(), exchange.getCurrencyCode()));
            if (exchange.getAmount().startsWith("-")) {
                tvAmount.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
            } else {
                tvAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            }
            tvDateCreated.setText(DateTimeUtils.getInstance().getStringFullDate(exchange.getCreated()));
            // if debit exchange
            if (!TextUtils.isEmpty(exchange.getIdDebit())) {
                tvCategoryName.setText(R.string.debit_name);
                imgCategory.setImageResource(R.drawable.ic_debit);
                tvAccountName.setText(DebitManager.getInstance().getAccountNameByDebitId(exchange.getIdDebit()));
                return;
            }
            // not debit exchange
            if (exchange.getTypeExchange() == ExchangeType.INCOME || exchange.getTypeExchange() == ExchangeType.EXPENSES) {
                Category category = CategoryManager.getInstance().getCategoryById(exchange.getIdCategory());
                if (category != null) {
                    imgCategory.setImageBitmap(ResourceUtils.getInstance().getBitmap(category.getByteImage()));
                    tvCategoryName.setText(category.getName());
                }
                tvAccountName.setText(AccountManager.getInstance().getAccountNameById(exchange.getIdAccount()));
            }
            if (exchange.getTypeExchange() == ExchangeType.TRANSFER) {
                imgCategory.setImageResource(R.drawable.ic_transfer);
                tvCategoryName.setText(getContext().getResources().getString(R.string.transfer));
                tvAccountName.setText(AccountManager.getInstance().getAccountNameById(exchange.getIdAccount()));
            }
        }
    }
}
