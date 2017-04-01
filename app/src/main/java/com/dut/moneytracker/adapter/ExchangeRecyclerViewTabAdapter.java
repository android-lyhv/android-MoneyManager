package com.dut.moneytracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.type.ExchangeType;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.CategoryManager;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.dut.moneytracker.utils.ResourceUtils;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */

public class ExchangeRecyclerViewTabAdapter extends BaseRecyclerAdapter {
    private static final String TAG = ExchangeRecyclerViewTabAdapter.class.getSimpleName();

    public ExchangeRecyclerViewTabAdapter(Context context, List objects) {
        super(context, objects);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_exchange_dashboard, parent, false);
        return new ExchangeHolder(view);
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
            tvAccountName = (TextView) itemView.findViewById(R.id.tvDescription);
            tvDateCreated = (TextView) itemView.findViewById(R.id.tvCreated);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
        }

        public void onBind(Exchange exchange) {
            if (exchange.getTypeExchange() == ExchangeType.INCOME || exchange.getTypeExchange() == ExchangeType.EXPENSES) {
                Category category = CategoryManager.getInstance().getCategoryById(exchange.getIdCategory());
                imgCategory.setImageBitmap(ResourceUtils.getInstance().getBitmap(category.getByteImage()));
                tvAccountName.setText(AccountManager.getInstance().getAccountNameById(exchange.getIdAccount()));
                tvCategoryName.setText(category.getName());
                tvDateCreated.setText(DateTimeUtils.getInstance().getStringFullDate(exchange.getCreated()));
                tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyType(exchange.getAmount(), exchange.getCurrencyCode()));
                if (exchange.getTypeExchange() == ExchangeType.INCOME) {
                    tvAmount.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                }
            }
            if (exchange.getTypeExchange() == ExchangeType.TRANSFER) {
                imgCategory.setImageResource(R.drawable.ic_transfer);
                tvCategoryName.setText(getContext().getResources().getString(R.string.transfer));
                tvDateCreated.setText(DateTimeUtils.getInstance().getStringFullDate(exchange.getCreated()));
                tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyType(exchange.getAmount(), exchange.getCurrencyCode()));
                tvAccountName.setText(AccountManager.getInstance().getAccountNameById(exchange.getIdAccount()));
                if (CurrencyUtils.getInstance().getFloatMoney(exchange.getAmount()) > 0) {
                    tvAmount.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        }
    }
}
