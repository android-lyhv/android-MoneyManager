package com.dut.moneytracker.adapter.exchanges;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.CategoryManager;
import com.dut.moneytracker.models.realms.DebitManager;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.dut.moneytracker.utils.ResourceUtils;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */

public class ItemSimpleExchange extends RecyclerView.ViewHolder {
    private TextView tvCategoryName;
    private TextView tvDescription;
    private TextView tvAccountName;
    private TextView tvDateCreated;
    private ImageView imgCategory;
    private TextView tvAmount;
    private LinearLayout llNote;
    private ImageView imgLocation;

    public ItemSimpleExchange(View itemView) {
        super(itemView);
        tvAccountName = (TextView) itemView.findViewById(R.id.tvNameAccount);
        tvDescription = (TextView) itemView.findViewById(R.id.tvAccountName);
        tvDateCreated = (TextView) itemView.findViewById(R.id.tvDate);
        tvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
        imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
        tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
        llNote = (LinearLayout) itemView.findViewById(R.id.llNote);
        imgLocation = (ImageView) itemView.findViewById(R.id.imgLocation);

    }

    void onBind(Context context, Exchange exchange) {
        // Chung
        if (TextUtils.isEmpty(exchange.getDescription())) {
            llNote.setVisibility(View.GONE);
        } else {
            llNote.setVisibility(View.VISIBLE);
        }
        tvDescription.setText(exchange.getDescription());
        tvDateCreated.setText(DateTimeUtils.getInstance().getStringFullDate(exchange.getCreated()));
        String amount = exchange.getAmount();
        if (!amount.startsWith("-")) {
            tvAmount.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        } else {
            tvAmount.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
        }
        tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(exchange.getAmount(), exchange.getCurrencyCode()));
        if (exchange.getPlace() == null) {
            imgLocation.setVisibility(View.GONE);
        } else {
            imgLocation.setVisibility(View.VISIBLE);
        }
        // if debit exchange
        if (!TextUtils.isEmpty(exchange.getIdDebit())) {
            tvCategoryName.setText(R.string.debit_name);
            imgCategory.setImageResource(R.drawable.ic_debit);
            tvAccountName.setText(DebitManager.getInstance().getAccountNameByDebitId(exchange.getIdDebit()));
            return;
        }
        // not debit exchange
        tvAccountName.setText(AccountManager.getInstance().getAccountNameById(exchange.getIdAccount()));
        if (exchange.getTypeExchange() == ExchangeType.INCOME || exchange.getTypeExchange() == ExchangeType.EXPENSES) {
            Category category = CategoryManager.getInstance().getCategoryById(exchange.getIdCategory());
            imgCategory.setImageBitmap(ResourceUtils.getInstance().getBitmap(category.getByteImage()));
            tvCategoryName.setText(category.getName());
        }
        if (exchange.getTypeExchange() == ExchangeType.TRANSFER) {
            imgCategory.setImageResource(R.drawable.ic_transfer);
            tvCategoryName.setText(context.getResources().getString(R.string.transfer));
        }
    }
}
