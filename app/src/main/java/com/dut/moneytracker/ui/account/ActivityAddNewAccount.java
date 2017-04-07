package com.dut.moneytracker.ui.account;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.dialogs.DialogCalculator;
import com.dut.moneytracker.dialogs.DialogPickColor;
import com.dut.moneytracker.dialogs.DialogPickColor_;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.UUID;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/04/2017.
 */
@EActivity(R.layout.activity_edit_account)
@OptionsMenu(R.menu.menu_add_exchange)
public class ActivityAddNewAccount extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, DialogPickColor.PickColorListener {
    private static final java.lang.String TAG = ActivityEditAccount.class.getSimpleName();
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.tvInitAmount)
    TextView mTvAmount;
    @ViewById(R.id.tvAccountName)
    EditText mEdtNameAccount;
    @ViewById(R.id.tvCurrency)
    TextView mTvCurrencyCode;
    @ViewById(R.id.imgColor)
    ImageView imgColor;
    @ViewById(R.id.switchLocation)
    SwitchCompat mSwitchLocation;
    Account mAccount;
    private DialogPickColor mDialogPickColor;

    @AfterViews
    void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar();
        initBaseAccount();
        initDialogPickColor();
    }

    private void initBaseAccount() {
        mAccount = new Account();
        mAccount.setId(UUID.randomUUID().toString());
        mAccount.setColorHex(getString(R.string.color_account_default));
        mAccount.setCurrencyCode("VND");
        mAccount.setName("");
        onLoadData();
    }

    private void onLoadData() {
        mEdtNameAccount.setText(mAccount.getName());
        mTvCurrencyCode.setText(mAccount.getCurrencyCode());
        mSwitchLocation.setChecked(mAccount.isSaveLocation());
        GradientDrawable shapeDrawable = (GradientDrawable) imgColor.getBackground();
        shapeDrawable.setColor(Color.parseColor(mAccount.getColorHex()));
        imgColor.setBackground(shapeDrawable);
    }

    private void initDialogPickColor() {
        mDialogPickColor = DialogPickColor_.builder().build();
        mDialogPickColor.register(this);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
    }

    @OptionsItem(R.id.actionAdd)
    void onSaveAccount() {
        String accountName = mEdtNameAccount.getText().toString();
        if (TextUtils.isEmpty(accountName)) {
            Toast.makeText(this, "Fill the name account!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAccount.setName(accountName);
        AccountManager.getInstance().insertOrUpdate(mAccount);
        setResult(ResultCode.ADD_NEW_ACCOUNT);
        finish();
    }

    @OptionsItem(android.R.id.home)
    void onClose() {
        finish();
    }

    @Click(R.id.tvInitAmount)
    void onClickInitAmount() {
        DialogCalculator dialogCalculator = new DialogCalculator();
        if (!TextUtils.isEmpty(mAccount.getInitAmount())) {
            dialogCalculator.setAmount(mAccount.getInitAmount());
        } else {
            dialogCalculator.setAmount("");
        }
        dialogCalculator.show(getFragmentManager(), TAG);
        dialogCalculator.registerResultListener(new DialogCalculator.ResultListener() {
            @Override
            public void onResult(String amount) {
                mAccount.setInitAmount(amount);
                mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyType(amount, mAccount.getCurrencyCode()));
            }
        });
    }

    @Click(R.id.imgColor)
    void onClickImgColor() {
        mDialogPickColor.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mAccount.setSaveLocation(isChecked);
        if (isChecked) {
            onRequestPermissionMap();
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
                                ActivityCompat.requestPermissions(ActivityAddNewAccount.this,
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

    @Override
    public void onResult(String colorHex) {
        mAccount.setColorHex(colorHex);
        GradientDrawable shapeDrawable = (GradientDrawable) imgColor.getBackground();
        shapeDrawable.setColor(Color.parseColor(colorHex));
        imgColor.setBackground(shapeDrawable);
    }
}