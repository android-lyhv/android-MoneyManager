package com.dut.moneytracker.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.dialogs.DialogCalculator;
import com.dut.moneytracker.objects.Account;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */

public class ActivityEditAccount extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final java.lang.String TAG = ActivityEditAccount.class.getSimpleName();
    TextView mTvAmount;
    EditText mEdtNameAccount;
    TextView mTvCurrencyCode;
    private SwitchCompat mSwitchLocation;
    private Account mAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        mTvCurrencyCode = (TextView) findViewById(R.id.tvCurrency);
        mTvCurrencyCode.setOnClickListener(this);
        mSwitchLocation = (SwitchCompat) findViewById(R.id.switchLocation);
        mSwitchLocation.setOnCheckedChangeListener(this);
    }

    private void onAccountExtra() {
        mAccount = getIntent().getParcelableExtra(getString(R.string.extra_account));
        mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyType(mAccount.getInitAmount(), mAccount.getCurrencyCode()));
        mEdtNameAccount.setText(mAccount.getName());
        mTvCurrencyCode.setText(mAccount.getCurrencyCode());
        mSwitchLocation.setChecked(mAccount.isSaveLocation());
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
            case R.id.actionSave:
                onSaveDataAccount();
                break;
            case R.id.actionDelete:
                //TODO
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
                dialogCalculator.setAmount(mAccount.getInitAmount());
                dialogCalculator.show(getFragmentManager(), TAG);
                dialogCalculator.registerResultListener(new DialogCalculator.ResultListener() {
                    @Override
                    public void onResult(String amount) {
                        mAccount.setInitAmount(amount);
                        mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyType(amount, mAccount.getCurrencyCode()));
                    }
                });
                break;
            case R.id.tvType:
                break;
        }
    }

    private void onSaveDataAccount() {
        String accountName = mEdtNameAccount.getText().toString();
        if (TextUtils.isEmpty(accountName)) {
            Toast.makeText(this, "Fill the name account!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAccount.setName(accountName);
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.extra_account), mAccount);
        setResult(ResultCode.EDIT_ACCOUNT, intent);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            onRequestPermissionMap();
        } else {
            mAccount.setSaveLocation(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestCode.PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mAccount.setSaveLocation(true);
                }
            }

        }
    }

    public void onRequestPermissionMap() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(ActivityEditAccount.this,
                                        new String[]{Manifest.permission
                                                .ACCESS_FINE_LOCATION},
                                        RequestCode.PERMISSION_LOCATION);
                            }
                        }).show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        RequestCode.PERMISSION_LOCATION);
            }
        } else {
            mAccount.setSaveLocation(true);
        }
    }

}
