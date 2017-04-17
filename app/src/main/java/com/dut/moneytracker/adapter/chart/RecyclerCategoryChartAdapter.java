package com.dut.moneytracker.adapter.chart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.ui.charts.objects.ValueCategoryChart;
import com.dut.moneytracker.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 17/04/2017.
 */

public class RecyclerCategoryChartAdapter extends RecyclerView.Adapter<RecyclerCategoryChartAdapter.ItemCategoryHolder> {
    private Context mContext;
    private List<ValueCategoryChart> mValueCategoryCharts;
    private String totals = "";

    public RecyclerCategoryChartAdapter(Context context, List<ValueCategoryChart> valueCategoryCharts) {
        mContext = context;
        mValueCategoryCharts = valueCategoryCharts;
    }

    @Override
    public ItemCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chart_category, parent, false);
        return new ItemCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemCategoryHolder holder, int position) {
        if (position == 0) {
            totals = getItem(position).getAmount().substring(1);
        }
        holder.onBind(getItem(position));
    }

    public void addAll(List<ValueCategoryChart> valueCategoryCharts) {
        if (mValueCategoryCharts == null) {
            mValueCategoryCharts = new ArrayList<>();
        }
        mValueCategoryCharts.clear();
        mValueCategoryCharts.addAll(valueCategoryCharts);
    }

    @Override
    public int getItemCount() {
        return mValueCategoryCharts == null ? 0 : mValueCategoryCharts.size();
    }

    public ValueCategoryChart getItem(int position) {
        return mValueCategoryCharts.get(position);
    }

    protected class ItemCategoryHolder extends RecyclerView.ViewHolder {
        private ImageView imgCategory;
        private TextView tvCategoryName;
        private TextView tvAmount;
        private ProgressBar mProgressBar;

        public ItemCategoryHolder(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            tvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }

        public void onBind(ValueCategoryChart valueChartCategory) {
            imgCategory.setImageBitmap(ResourceUtils.getInstance().getBitmap(valueChartCategory.getCategory().getByteImage()));
            tvCategoryName.setText(valueChartCategory.getCategory().getName());
            String amount = valueChartCategory.getAmount();
            if (amount.startsWith("-")) {
                amount = amount.substring(1);
            }
            tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(amount, CurrencyUtils.DEFAULT_CURRENCY_CODE));
            loadProgressBar(amount);
        }

        private void loadProgressBar(String amountPartial) {
            float amount = CurrencyUtils.getInstance().getFloatMoney(amountPartial);
            float amountTotal = CurrencyUtils.getInstance().getFloatMoney(totals);
            int distance = (int) (Math.abs(amount / amountTotal) * 100);
            if (amount != 0 && distance == 0) {
                distance = 1;
            }
            mProgressBar.setProgress(distance);
        }
    }
}
