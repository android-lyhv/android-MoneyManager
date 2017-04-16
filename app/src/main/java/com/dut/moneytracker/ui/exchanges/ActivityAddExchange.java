package com.dut.moneytracker.ui.exchanges;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.currency.CurrencyExpression;
import com.dut.moneytracker.dialogs.DialogCalculator;
import com.dut.moneytracker.dialogs.DialogPickAccount;
import com.dut.moneytracker.maps.GoogleLocation;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.Place;
import com.dut.moneytracker.ui.category.ActivityPickCategory;
import com.dut.moneytracker.ui.interfaces.AddListener;
import com.dut.moneytracker.utils.DialogUtils;
import com.google.android.gms.common.api.GoogleApiClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 23/02/2017.
 */
@EActivity(R.layout.activity_add_exchange)
@OptionsMenu(R.menu.menu_add_exchange)
public class ActivityAddExchange extends AppCompatActivity implements AddListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = ActivityAddExchange.class.getSimpleName();
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.tvAmount)
    TextView tvAmount;
    @ViewById(R.id.btnTransfer)
    Button btnTransfer;
    @ViewById(R.id.btnIncome)
    Button btnIncome;
    @ViewById(R.id.btnExpenses)
    Button btnExpenses;
    @ViewById(R.id.tvAccountName)
    TextView tvAccountName;
    @ViewById(R.id.tvCategoryName)
    TextView tvCategoryName;
    @ViewById(R.id.tvStatus)
    TextView tvStatus;
    @ViewById(R.id.tvTitleFromAccount)
    TextView tvTitleFromAccount;
    @ViewById(R.id.tvTitleToAccount)
    TextView tvTitleToAccount;
    //Model
    @Extra
    Account mAccount;
    private Exchange mExchange;
    private String mIdCategory;
    private String mNameCategory;
    private String mNameAccountTransfer;

    @AfterViews
    void init() {
        initDataExchange();
        initView();
    }

    private void initView() {
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
        setTitle(getString(R.string.activity_add_exchange_title));
        setSupportActionBar(mToolbar);
        btnExpenses.setAlpha(1f);
        btnIncome.setAlpha(0.5f);
        btnTransfer.setAlpha(0.5f);
        tvAccountName.setText(mAccount.getName());
        mNameCategory = getString(R.string.unknown);
        mNameAccountTransfer = getString(R.string.unknown);
    }

    private void initDataExchange() {
        mExchange = new Exchange();
        mExchange.setId(UUID.randomUUID().toString());
        mExchange.setTypeExchange(ExchangeType.EXPENSES);
        mExchange.setIdAccount(mAccount.getId());
        mExchange.setCurrencyCode(mAccount.getCurrencyCode());
    }

    @OptionsItem(android.R.id.home)
    void onClose() {
        finish();
    }

    @OptionsItem(R.id.actionAdd)
    void onSave() {
        switch (mExchange.getTypeExchange()) {
            case ExchangeType.INCOME:
            case ExchangeType.EXPENSES:
                onAddExpensesOrIncome();
                break;
            case ExchangeType.TRANSFER:
                onAddTransfer();
                break;
        }
    }

    @Click(R.id.tvCal0)
    void onClickTvCalZero() {
        onSetValueAmount("0");
    }

    @Click(R.id.tvCal1)
    void onClickTvCalOne() {
        onSetValueAmount("1");
    }

    @Click(R.id.tvCal2)
    void onClickTvCalTwo() {
        onSetValueAmount("2");
    }

    @Click(R.id.tvCal3)
    void onClickTvCalThree() {
        onSetValueAmount("3");
    }

    @Click(R.id.tvCal4)
    void onClickTvCalFour() {
        onSetValueAmount("4");
    }

    @Click(R.id.tvCal5)
    void onClickTvCalFive() {
        onSetValueAmount("5");
    }

    @Click(R.id.tvCal6)
    void onClickTvCalSix() {
        onSetValueAmount("6");
    }

    @Click(R.id.tvCal7)
    void onClickTvCalSeven() {
        onSetValueAmount("7");
    }

    @Click(R.id.tvCal8)
    void onClickTvCalEight() {
        onSetValueAmount("8");
    }

    @Click(R.id.tvCal9)
    void onClickTvCalNight() {
        onSetValueAmount("9");
    }

    @Click(R.id.tvCalDot)
    void onClickTvCalDot() {
        onSetValueAmount(".");
    }

    @Click(R.id.imgCalBack)
    void onClickBackNumber() {
        String current = tvAmount.getText().toString();
        if (!TextUtils.isEmpty(current)) {
            tvAmount.setText(current.substring(0, current.length() - 1));
        }
    }

    @Click(R.id.llAccount)
    void onClickPickAccount() {
        DialogPickAccount dialogPickAccount = new DialogPickAccount();
        dialogPickAccount.show(getFragmentManager(), TAG);
        dialogPickAccount.registerPickAccount(new DialogPickAccount.AccountListener() {
            @Override
            public void onResultAccount(Account account) {
                mExchange.setIdAccount(account.getId());
                tvAccountName.setText(account.getName());
            }
        }, false);
    }

    @Click(R.id.llCategory)
    void onClickPickCategory() {
        if (mExchange.getTypeExchange() == ExchangeType.TRANSFER) {
            showDialogPickAccountReceive();
        } else {
            showActivityPickCategory();
        }
    }

    @Click(R.id.tvMoreAdd)
    void onClickMoreAddExchange() {
        String textAmount = tvAmount.getText().toString().trim();
        if (TextUtils.isEmpty(textAmount)) {
            textAmount = "0";
        }
        if (mExchange.getTypeExchange() == ExchangeType.EXPENSES) {
            mExchange.setAmount(String.format(Locale.US, "-%s", textAmount));
        } else {
            mExchange.setAmount(textAmount);
        }
        ActivityAddMoreExchange_.intent(this).mExchange(mExchange).startForResult(IntentCode.MORE_ADD);
    }

    @Click(R.id.btnIncome)
    void onClickTabIncome() {
        mExchange.setTypeExchange(ExchangeType.INCOME);
        tvTitleFromAccount.setText(getString(R.string.main_account));
        tvTitleToAccount.setText(getString(R.string.category_name));
        tvCategoryName.setText(mNameCategory);
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText("+");
        btnExpenses.setAlpha(0.5f);
        btnIncome.setAlpha(1f);
        btnTransfer.setAlpha(0.5f);
    }

    @Click(R.id.btnExpenses)
    void onClickTabExpenses() {
        mExchange.setTypeExchange(ExchangeType.EXPENSES);
        tvTitleFromAccount.setText(getString(R.string.main_account));
        tvTitleToAccount.setText(getString(R.string.category_name));
        tvCategoryName.setText(mNameCategory);
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText("-");
        btnExpenses.setAlpha(1f);
        btnIncome.setAlpha(0.5f);
        btnTransfer.setAlpha(0.5f);
    }

    @Click(R.id.btnTransfer)
    void onClickTabTransfer() {
        mExchange.setTypeExchange(ExchangeType.TRANSFER);
        tvStatus.setVisibility(View.GONE);
        tvTitleFromAccount.setText(getString(R.string.account_send));
        tvTitleToAccount.setText(getString(R.string.expense_name));
        tvCategoryName.setText(mNameAccountTransfer);
        btnExpenses.setAlpha(0.5f);
        btnIncome.setAlpha(0.5f);
        btnTransfer.setAlpha(1f);
    }


    @Override
    public void onAddExpensesOrIncome() {
        if (!isAvailableIncomeAndExpense()) {
            return;
        }
        String textAmount = tvAmount.getText().toString().trim();
        if (mExchange.getTypeExchange() == ExchangeType.EXPENSES) {
            mExchange.setAmount(String.format(Locale.US, "-%s", textAmount));
        } else {
            mExchange.setAmount(textAmount);
        }
        mExchange.setIdCategory(mIdCategory);
        onRequestExchangePlace();
    }

    @Override
    public void onAddTransfer() {
        if (!isAvailableTransfer()) {
            return;
        }
        onRequestExchangePlace();
    }

    @Override
    public void onSaveDataBase() {
        if (null == mExchange.getCreated()) {
            mExchange.setCreated(new Date());
        }
        if (mExchange.getTypeExchange() == ExchangeType.TRANSFER) {
            String codeTransfer = UUID.randomUUID().toString();
            // Them giao dich account gui
            String amount = String.format(Locale.US, "-%s", tvAmount.getText().toString());
            mExchange.setAmount(amount);
            mExchange.setCodeTransfer(codeTransfer);
            ExchangeManger.getInstance().insertOrUpdate(mExchange);
            // Them giao dich account nhan
            String idTransfer = mExchange.getIdAccountTransfer();
            if (!TextUtils.equals(idTransfer, AccountManager.OUT_SIDE)) {
                String idAccount = mExchange.getIdAccount();
                mExchange.setId(UUID.randomUUID().toString());
                mExchange.setAmount(tvAmount.getText().toString());
                mExchange.setIdAccount(idTransfer);
                mExchange.setIdAccountTransfer(idAccount);
                mExchange.setCodeTransfer(codeTransfer);
                ExchangeManger.getInstance().insertOrUpdate(mExchange);
            }
        } else {
            ExchangeManger.getInstance().insertOrUpdate(mExchange);
        }
        setResult(IntentCode.ADD_NEW_EXCHANGE, new Intent());
        finish();
    }

    private void showDialogPickAccountReceive() {
        DialogPickAccount dialogPickAccount = new DialogPickAccount();
        dialogPickAccount.show(getFragmentManager(), TAG);
        dialogPickAccount.registerPickAccount(new DialogPickAccount.AccountListener() {
            @Override
            public void onResultAccount(Account account) {
                if (TextUtils.equals(mExchange.getIdAccount(), account.getId())) {
                    Toast.makeText(ActivityAddExchange.this, "Vui lòng chọn một tài khoản khác", Toast.LENGTH_SHORT).show();
                    return;
                }
                mNameAccountTransfer = account.getName();
                tvCategoryName.setText(mNameAccountTransfer);
                mExchange.setIdAccountTransfer(account.getId());
            }
        }, true);
    }


    private void showActivityPickCategory() {
        startActivityForResult(new Intent(this, ActivityPickCategory.class), IntentCode.PICK_CATEGORY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case IntentCode.PICK_CATEGORY:
                Category category = data.getParcelableExtra(getString(R.string.extra_category));
                mIdCategory = category.getId();
                mNameCategory = category.getName();
                tvCategoryName.setText(mNameCategory);
                break;
            case IntentCode.MORE_ADD:
                mExchange = data.getParcelableExtra(getString(R.string.extra_more_add));
                break;
        }
    }

    private void onSetValueAmount(String chart) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tvAmount.getText().toString());
        stringBuilder.append(chart);
        if (CurrencyExpression.getInstance().isValidateTypeMoney(stringBuilder.toString())) {
            if (stringBuilder.toString().length() > DialogCalculator.MAX) {
                return;
            }
            tvAmount.setText(stringBuilder.toString());
        }
    }

    private void onRequestExchangePlace() {
        Log.d(TAG, "onRequestExchangePlace: ");
        if (!mAccount.isSaveLocation()) {
            onSaveDataBase();
            return;
        }
        final GoogleLocation googleLocation = new GoogleLocation(getApplicationContext());
        googleLocation.registerCurrentPlaceListener(new GoogleLocation.CurrentPlaceListener() {
            @Override
            public void onResultPlace(Place place) {
                Log.d(TAG, "onResultPlace: " + place.getLatitude());
                mExchange.setPlace(place);
                onSaveDataBase();
                googleLocation.stopLocationUpDate();
                googleLocation.disConnectApiGoogle();
                DialogUtils.getInstance().dismissProgressDialog();
            }
        });
        googleLocation.connectApiGoogle();
        DialogUtils.getInstance().showProgressDialog(this, "Loading");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public boolean isAvailableTransfer() {
        if (TextUtils.isEmpty(mExchange.getIdAccountTransfer())) {
            Toast.makeText(this, "Chọn tài khoản nhận", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(tvAmount.getText().toString())) {
            Toast.makeText(this, "Nhập số tiền", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean isAvailableIncomeAndExpense() {
        String textAmount = tvAmount.getText().toString().trim();
        if (TextUtils.isEmpty(textAmount)) {
            Toast.makeText(this, "Fill the amount!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mIdCategory)) {
            Toast.makeText(this, "Please pick category", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
