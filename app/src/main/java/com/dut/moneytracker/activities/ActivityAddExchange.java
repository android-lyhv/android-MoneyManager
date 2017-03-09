package com.dut.moneytracker.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.currency.CurrencyExpression;
import com.dut.moneytracker.dialogs.DialogPickAccount;
import com.dut.moneytracker.dialogs.DialogPickCurrency;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;

import java.util.Date;
import java.util.UUID;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 23/02/2017.
 */

public class ActivityAddExchange extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ActivityAddExchange.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView tvCal0;
    private TextView tvCal1;
    private TextView tvCal2;
    private TextView tvCal3;
    private TextView tvCal4;
    private TextView tvCal5;
    private TextView tvCal6;
    private TextView tvCal7;
    private TextView tvCal8;
    private TextView tvCal9;
    private ImageView imgCalBack;
    private TextView tvCalDot;
    private TextView tvAmount;
    private Button btnTransfer;
    private Button btnIncome;
    private Button btnExpenses;
    private TextView tvCurrency;
    private LinearLayout llAccount;
    private LinearLayout llCategory;
    private TextView tvAccountName;
    private TextView tvCategoryName;
    private TextView tvDetail;
    private LinearLayout llInformation;
    private TextView tvStatus;
    private StringBuilder stringBuilder;


    //Model
    private Account mAccount;
    private Exchange mExchange;
    private String idCategory = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exchange);
        initView();
        onGetAccountExtra();
        initExchange();
    }

    private void initExchange() {
        mExchange = new Exchange();
        mExchange.setId(UUID.randomUUID().toString());
        mExchange.setType(ExchangeType.EXPENSES);
        mExchange.setIdAccount(mAccount.getId());
        mExchange.setCurrencyCode(mAccount.getCurrencyCode());
        mExchange.setCreated(new Date());
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
        setTitle("");
        setSupportActionBar(mToolbar);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvCal0 = (TextView) findViewById(R.id.tvCal0);
        tvCal0.setOnClickListener(this);
        tvCal1 = (TextView) findViewById(R.id.tvCal1);
        tvCal1.setOnClickListener(this);
        tvCal2 = (TextView) findViewById(R.id.tvCal2);
        tvCal2.setOnClickListener(this);
        tvCal3 = (TextView) findViewById(R.id.tvCal3);
        tvCal3.setOnClickListener(this);
        tvCal4 = (TextView) findViewById(R.id.tvCal4);
        tvCal4.setOnClickListener(this);
        tvCal5 = (TextView) findViewById(R.id.tvCal5);
        tvCal5.setOnClickListener(this);
        tvCal6 = (TextView) findViewById(R.id.tvCal6);
        tvCal6.setOnClickListener(this);
        tvCal7 = (TextView) findViewById(R.id.tvCal7);
        tvCal7.setOnClickListener(this);
        tvCal8 = (TextView) findViewById(R.id.tvCal8);
        tvCal8.setOnClickListener(this);
        tvCal9 = (TextView) findViewById(R.id.tvCal9);
        llInformation = (LinearLayout) findViewById(R.id.llInformation);
        tvCal9.setOnClickListener(this);
        imgCalBack = (ImageView) findViewById(R.id.imgCalBack);
        imgCalBack.setOnClickListener(this);
        tvCalDot = (TextView) findViewById(R.id.tvCalDot);
        tvCalDot.setOnClickListener(this);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        btnIncome = (Button) findViewById(R.id.btnIncome);
        btnIncome.setOnClickListener(this);
        btnExpenses = (Button) findViewById(R.id.btnExpenses);
        btnExpenses.setOnClickListener(this);
        btnTransfer = (Button) findViewById(R.id.btnTransfer);
        btnTransfer.setOnClickListener(this);
        tvAccountName = (TextView) findViewById(R.id.tvAccountName);
        tvCategoryName = (TextView) findViewById(R.id.tvCategoryName);
        llAccount = (LinearLayout) findViewById(R.id.llAccount);
        llAccount.setOnClickListener(this);
        llCategory = (LinearLayout) findViewById(R.id.llCategory);
        llCategory.setOnClickListener(this);
        tvDetail = (TextView) findViewById(R.id.tvDetail);
        tvDetail.setOnClickListener(this);
        tvCurrency = (TextView) findViewById(R.id.tvTypeMoney);
        tvCurrency.setOnClickListener(this);
    }

    private void onGetAccountExtra() {
        mAccount = getIntent().getParcelableExtra(getString(R.string.extra_account));
        tvAccountName.setText(mAccount.getName());
        tvCurrency.setText(mAccount.getCurrencyCode());
        mToolbar.setBackgroundColor(Color.parseColor(mAccount.getColorCode()));
        llInformation.setBackgroundColor(Color.parseColor(mAccount.getColorCode()));
        btnExpenses.setAlpha(1f);
        btnIncome.setAlpha(0.5f);
        btnTransfer.setAlpha(0.5f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_exchange, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.actionAdd:
                onAddNewExchange();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onAddNewExchange() {
        String textAmount = tvAmount.getText().toString().trim();
        if (TextUtils.isEmpty(textAmount)) {
            Toast.makeText(this, "Fill the amount!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(idCategory)) {
            Toast.makeText(this, "Please pick category", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mExchange.getType() == ExchangeType.EXPENSES) {
            textAmount = "-" + textAmount;
        }
        mExchange.setAmount(textAmount);
        mExchange.setIdCategory(idCategory);
        AccountManager.getInstance().addExchange(mExchange.getIdAccount(), mExchange);
        setResult(ResultCode.ADD_EXCHANGE, new Intent());
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCal0:
                setValue("0");
                break;
            case R.id.tvCal1:
                setValue("1");
                break;
            case R.id.tvCal2:
                setValue("2");
                break;
            case R.id.tvCal3:
                setValue("3");
                break;
            case R.id.tvCal4:
                setValue("4");
                break;
            case R.id.tvCal5:
                setValue("5");
                break;
            case R.id.tvCal6:
                setValue("6");
                break;
            case R.id.tvCal7:
                setValue("7");
                break;
            case R.id.tvCal8:
                setValue("8");
                break;
            case R.id.tvCal9:
                setValue("9");
                break;
            case R.id.tvCalDot:
                setValue(".");
                break;
            case R.id.imgCalBack:
                String current = tvAmount.getText().toString();
                if (!TextUtils.isEmpty(current)) {
                    tvAmount.setText(current.substring(0, current.length() - 1));
                }
                break;
            case R.id.llAccount:
                showDialogPickAccount();
                break;
            case R.id.llCategory:
                showActivityPickCategory();
                break;
            case R.id.tvDetail:
                break;
            case R.id.tvTypeMoney:
                showDialogPickCurrency();
                break;
            case R.id.btnIncome:
                mExchange.setType(ExchangeType.INCOME);
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText("+");
                btnExpenses.setAlpha(0.5f);
                btnIncome.setAlpha(1f);
                btnTransfer.setAlpha(0.5f);
                break;
            case R.id.btnExpenses:
                tvStatus.setVisibility(View.VISIBLE);
                mExchange.setType(ExchangeType.EXPENSES);
                tvStatus.setText("-");
                btnExpenses.setAlpha(1f);
                btnIncome.setAlpha(0.5f);
                btnTransfer.setAlpha(0.5f);
                break;
            case R.id.btnTransfer:
                tvStatus.setVisibility(View.INVISIBLE);
                mExchange.setType(ExchangeType.TRANSFER);
                btnExpenses.setAlpha(0.5f);
                btnIncome.setAlpha(0.5f);
                btnTransfer.setAlpha(1f);
                break;
        }
    }

    private void showDialogPickAccount() {
        DialogPickAccount dialogPickAccount = new DialogPickAccount();
        dialogPickAccount.show(getFragmentManager(), TAG);
        dialogPickAccount.registerPickAccount(new DialogPickAccount.AccountListener() {
            @Override
            public void onResultAccount(Account account) {
                Log.d(TAG, "onResultAccount: " + String.valueOf(account));
                mExchange.setIdAccount(account.getId());
                tvAccountName.setText(account.getName());
            }
        });
    }

    private void showDialogPickCurrency() {
        DialogPickCurrency dialogPickCurrency = new DialogPickCurrency();
        dialogPickCurrency.show(getFragmentManager(), TAG);
        dialogPickCurrency.registerResultListener(new DialogPickCurrency.ResultListener() {
            @Override
            public void onResultCurrencyCode(String code) {
                tvCurrency.setText(code);
                mExchange.setCurrencyCode(code);
            }
        });
    }

    private void showActivityPickCategory() {
        startActivityForResult(new Intent(this, ActivityPickCategory.class), RequestCode.PICK_CATEGORY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ResultCode.PICK_CATEGORY:
                Category category = data.getParcelableExtra(getString(R.string.extra_category));
                idCategory = category.getId();
                tvCategoryName.setText(category.getName());
                break;
        }
    }

    private void setValue(String chart) {
        stringBuilder = new StringBuilder();
        stringBuilder.append(tvAmount.getText().toString());
        stringBuilder.append(chart);
        if (CurrencyExpression.getInstance().isValidateTypeMoney(stringBuilder.toString())) {
            tvAmount.setText(stringBuilder.toString());
        }
    }
}
