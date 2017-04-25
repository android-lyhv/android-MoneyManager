package com.dut.moneytracker.ui.account;

import android.Manifest;
import android.content.Intent;
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
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.dialogs.DialogCalculator;
import com.dut.moneytracker.dialogs.DialogConfirm;
import com.dut.moneytracker.dialogs.DialogPickColor;
import com.dut.moneytracker.dialogs.DialogPickColor_;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.utils.NetworkUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */
@EActivity(R.layout.activity_detail_account)
@OptionsMenu(R.menu.menu_edit_account)
public class ActivityDetailAccount extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, DialogPickColor.PickColorListener {
    private static final java.lang.String TAG = ActivityDetailAccount.class.getSimpleName();
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.tvInitAmount)
    TextView mTvAmount;
    @ViewById(R.id.tvAccountName)
    EditText mEdtNameAccount;
    @ViewById(R.id.imgColor)
    ImageView imgColor;
    @ViewById(R.id.switchLocation)
    SwitchCompat mSwitchLocation;
    @Extra
    Account mAccount;
    private DialogPickColor mDialogPickColor;
    private DialogCalculator mDialogCalculator;

    @AfterViews
    void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setTitle(getString(R.string.toolbar_title_detail_account));
        initDialog();
        initToolbar();
        onLoadData();
        initDialogPickColor();
    }

    private void initDialog() {
        mDialogPickColor = DialogPickColor_.builder().build();
        mDialogCalculator = DialogCalculator.getInstance();
    }

    private void initDialogPickColor() {
        mDialogPickColor = DialogPickColor_.builder().build();
        mDialogPickColor.register(this);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
    }

    private void onLoadData() {
        if (!TextUtils.isEmpty(mAccount.getInitAmount())) {
            mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mAccount.getInitAmount(), CurrencyUtils.DEFAULT_CURRENCY_CODE));
        }
        mEdtNameAccount.setText(mAccount.getName());
        mSwitchLocation.setChecked(mAccount.isSaveLocation());
        mSwitchLocation.setOnCheckedChangeListener(this);
        GradientDrawable shapeDrawable = (GradientDrawable) imgColor.getBackground();
        shapeDrawable.setColor(Color.parseColor(mAccount.getColorHex()));
        imgColor.setBackground(shapeDrawable);
    }

    @OptionsItem(R.id.actionSave)
    void onSaveAccount() {
        String accountName = mEdtNameAccount.getText().toString();
        if (TextUtils.isEmpty(accountName)) {
            Toast.makeText(this, "Nhập tên tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mAccount.getInitAmount())) {
            Toast.makeText(this, "Nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!AccountManager.getInstance().isNameAccountAvailable(accountName, mAccount.getId())) {
            Toast.makeText(this, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }
        mAccount.setName(accountName);
        // Sending
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.extra_account), mAccount);
        setResult(IntentCode.EDIT_ACCOUNT, intent);
        finish();
    }

    @OptionsItem(R.id.actionDelete)
    void onDeleteAccount() {
        if (!NetworkUtils.getInstance().isConnectNetwork(this)) {
            Toast.makeText(this, "Mất kết nối internet!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mAccount.isDefault()) {
            Toast.makeText(this, R.string.account_default, Toast.LENGTH_SHORT).show();
            return;
        }
        DialogConfirm.getInstance().registerClickListener(new DialogConfirm.ClickListener() {
            @Override
            public void onClickResult(boolean value) {
                if (value) {
                    AccountManager.getInstance().onDeleteAccount(mAccount.getId());
                    setResult(IntentCode.DELETE_ACCOUNT);
                    finish();
                }
            }
        });
        DialogConfirm.getInstance().setMessage(getString(R.string.message_delete_account));
        DialogConfirm.getInstance().show(getSupportFragmentManager(), null);
    }

    @OptionsItem(android.R.id.home)
    void onClose() {
        finish();
    }

    @Click(R.id.tvInitAmount)
    void onClickInitAmount() {
        if (!TextUtils.isEmpty(mAccount.getInitAmount())) {
            mDialogCalculator.setAmount(mAccount.getInitAmount());
        } else {
            mDialogCalculator.setAmount("");
        }
        mDialogCalculator.show(getFragmentManager(), TAG);
        mDialogCalculator.registerResultListener(new DialogCalculator.ResultListener() {
            @Override
            public void onResult(String amount) {
                mAccount.setInitAmount(amount);
                mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(amount, CurrencyUtils.DEFAULT_CURRENCY_CODE));
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
            case IntentCode.PERMISSION_LOCATION: {
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
                                ActivityCompat.requestPermissions(ActivityDetailAccount.this,
                                        new String[]{Manifest.permission
                                                .ACCESS_FINE_LOCATION},
                                        IntentCode.PERMISSION_LOCATION);
                            }
                        }).show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        IntentCode.PERMISSION_LOCATION);
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
