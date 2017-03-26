package com.dut.moneytracker.adapter.exchanges;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.type.ExchangeType;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.realms.CategoryManager;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */

public class ItemSimpleExchange extends RecyclerView.ViewHolder {
    private TextView tvCategoryName;
    private TextView tvDescription;
    private TextView tvPymenType;
    private TextView tvAccountName;
    private TextView tvDateCreated;
    private ImageView imgCategory;
    private TextView tvAmount;

    public ItemSimpleExchange(View itemView) {
        super(itemView);
        tvAccountName = (TextView) itemView.findViewById(R.id.tvDescription);
        tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        tvPymenType = (TextView) itemView.findViewById(R.id.tvPaymentType);
        tvDateCreated = (TextView) itemView.findViewById(R.id.tvDate);
        imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
        tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
    }

    public void onBind(Context context, Exchange exchange) {
        if (exchange.getTypeExchange() == ExchangeType.INCOME || exchange.getTypeExchange() == ExchangeType.EXPENSES) {
            Category category = CategoryManager.getInstance().getCategoryById(exchange.getIdCategory());
           /* imgCategory.setImageBitmap(ResourceUtils.getInstance().getBitmap(category.getByteImage()));
            tvAccountName.setText(AccountManager.getInstance().getAccountNameById(exchange.getIdAccount()));
            tvCategoryName.setText(category.getName());
            tvDateCreated.setText(DateTimeUtils.getInstance().getStringFullDate(exchange.getCreated()));*/
            tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyType(exchange.getAmount(), exchange.getCurrencyCode()));
            if (exchange.getTypeExchange() == ExchangeType.INCOME) {
                tvAmount.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }
}
