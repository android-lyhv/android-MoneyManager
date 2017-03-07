package com.dut.moneytracker.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.dialogs.DialogCalculator;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.utils.StringUtils;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */

public class ActivityEditAccount extends AppCompatActivity implements View.OnClickListener {
    private static final java.lang.String TAG = ActivityEditAccount.class.getSimpleName();
    private Account mAccount;
    TextView mTvAmount;
    EditText mEdtNameAccount;
    TextView mTvType;
    TextView mTvCurrencyCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        initView();
        onAccountExtra();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white);
        mTvAmount = (TextView) findViewById(R.id.tvInitAmount);
        mTvAmount.setOnClickListener(this);
        mEdtNameAccount = (EditText) findViewById(R.id.tvAccountName);
        mTvType = (TextView) findViewById(R.id.tvType);
        mTvType.setOnClickListener(this);
        mTvCurrencyCode = (TextView) findViewById(R.id.tvCurrency);
        mTvCurrencyCode.setOnClickListener(this);
    }

    private void onAccountExtra() {
        mAccount = getIntent().getParcelableExtra(getString(R.string.extra_account));
        String money = StringUtils.getInstance().getStringMoneyType(mAccount.getInitAmount(), mAccount.getCurrencyCode());
        mTvAmount.setText(money);
        mEdtNameAccount.setText(mAccount.getName());
        mTvCurrencyCode.setText(mAccount.getCurrencyCode());
      //  mTvType.setText(mAccount.get);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_account, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvInitAmount:
                DialogCalculator dialogCalculator = new DialogCalculator();
                dialogCalculator.show(getFragmentManager(), TAG);
                dialogCalculator.registerResultListener(new DialogCalculator.ResultListener() {
                    @Override
                    public void onResult(float amount) {
                        Log.d(TAG, "onResult: " + amount);
                    }
                });
                break;
            case R.id.tvType:
                break;
        }
    }
}
