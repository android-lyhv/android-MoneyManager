package com.dut.moneytracker.ui.debit;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.DebitType;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.dialogs.DialogCalculator;
import com.dut.moneytracker.dialogs.DialogInput;
import com.dut.moneytracker.dialogs.DialogInput_;
import com.dut.moneytracker.dialogs.DialogPickAccount;
import com.dut.moneytracker.models.realms.DebitManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Debit;
import com.dut.moneytracker.ui.base.SpinnerTypeDebitManager;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.dut.moneytracker.view.DayPicker;
import com.dut.moneytracker.view.DayPicker_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.Locale;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 13/04/2017.
 */
@EActivity(R.layout.activity_add_debit)
@OptionsMenu(R.menu.menu_add_exchange)
public class ActivityAddDebit extends AppCompatActivity {
    private static final String TAG = ActivityAddDebit.class.getSimpleName();
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.tvName)
    TextView mTvName;
    @ViewById(R.id.tvAmount)
    TextView mTvAmount;
    @ViewById(R.id.tvDescriptionDebit)
    TextView tvDescriptionDebit;
    @ViewById(R.id.tvAccount)
    TextView mTvAccountName;
    @ViewById(R.id.tvStartDate)
    TextView mTvStartDate;
    @ViewById(R.id.tvEndDate)
    TextView mTvEndDate;
    @ViewById(R.id.spinnerDebit)
    AppCompatSpinner mSpinnerDebit;
    private SpinnerTypeDebitManager mSpinnerTypeDebitManager;
    private Debit mNewDebit;
    private DialogCalculator mDialogCalculator;
    DialogPickAccount mDialogPickAccount;

    @AfterViews
    void init() {
        mSpinnerTypeDebitManager = new SpinnerTypeDebitManager(this, mSpinnerDebit);
        initDialog();
        initBaseDebit();
        initView();
    }

    private void initDialog() {
        mDialogCalculator = DialogCalculator.getInstance();
        mDialogPickAccount = DialogPickAccount.getInstance();
    }

    private void initView() {
        setTitle(getString(R.string.toobar_tilte_new_debit));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
        mSpinnerTypeDebitManager.registerSelectedItem(new SpinnerTypeDebitManager.ItemSelectedListener() {
            @Override
            public void onResultTypeDebit(int type) {
                mNewDebit.setTypeDebit(type);
                onChangeAmount(type);
            }
        });
        mTvStartDate.setText(DateTimeUtils.getInstance().getStringDateUs(mNewDebit.getStartDate()));
        mTvEndDate.setText(DateTimeUtils.getInstance().getStringDateUs(mNewDebit.getEndDate()));
    }

    private void initBaseDebit() {
        mNewDebit = new Debit();
        mNewDebit.setId(DebitManager.getInstance().getNewIdDebit());
        mNewDebit.setTypeDebit(mSpinnerTypeDebitManager.getTypeItemSelected());
        mNewDebit.setStartDate(new Date());
        mNewDebit.setEndDate(new Date());
    }

    @OptionsItem(android.R.id.home)
    void onCLickClose() {
        finish();
    }

    @OptionsItem(R.id.actionAdd)
    void onClickSave() {
        if (TextUtils.isEmpty(mNewDebit.getAmount())) {
            Toast.makeText(this, "Nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mNewDebit.getName())) {
            Toast.makeText(this, "Nhhập tên", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mNewDebit.getIdAccount())) {
            Toast.makeText(this, "Chọn tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!DateTimeUtils.getInstance().isSameDate(mNewDebit.getStartDate(), mNewDebit.getEndDate())) {
            if (mNewDebit.getStartDate().after(mNewDebit.getEndDate())) {
                Toast.makeText(this, "Nhập sai ngày", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //Debit
        DebitManager.getInstance().insertOrUpdateDebit(mNewDebit);
        finish();
    }

    @Click(R.id.rlName)
    void onClickName() {
        DialogInput.getInstance().register(new DialogInput.DescriptionListener() {
            @Override
            public void onResult(String content) {
                mNewDebit.setName(content);
                mTvName.setText(content);
            }
        });
        DialogInput.getInstance().show(getSupportFragmentManager(), null);
    }

    @Click(R.id.rlAmount)
    void onCLickAmount() {
        String amount = mNewDebit.getAmount();
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }
        if (amount.startsWith("-")) {
            amount = amount.substring(1);
        }
        mDialogCalculator.show(getFragmentManager(), null);
        mDialogCalculator.setAmount(amount);
        mDialogCalculator.registerResultListener(new DialogCalculator.ResultListener() {
            @Override
            public void onResult(String amount) {
                if (mNewDebit.getTypeDebit() == DebitType.BORROWED) {
                    mNewDebit.setAmount(amount);
                } else {
                    mNewDebit.setAmount(String.format(Locale.US, "-%s", amount));
                }
                mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mNewDebit.getAmount(), CurrencyUtils.DEFAULT_CURRENCY_CODE));
            }
        });
    }

    private void onChangeAmount(int type) {
        String amount = mNewDebit.getAmount();
        if (TextUtils.isEmpty(amount)) {
            return;
        }
        if (amount.startsWith("-")) {
            amount = amount.substring(1);
        }
        if (type == DebitType.BORROWED) {
            mNewDebit.setAmount(amount);
            mTvAmount.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        } else {
            mTvAmount.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
            mNewDebit.setAmount(String.format(Locale.US, "-%s", amount));
        }
        mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mNewDebit.getAmount(), CurrencyUtils.DEFAULT_CURRENCY_CODE));
    }

    @Click(R.id.rlDescription)
    void onCLickDescription() {
        DialogInput dialogInput = DialogInput_.builder().build();
        dialogInput.register(new DialogInput.DescriptionListener() {
            @Override
            public void onResult(String content) {
                mNewDebit.setDescription(content);
                tvDescriptionDebit.setText(content);
            }
        });
        dialogInput.show(getSupportFragmentManager(), null);
    }

    @Click(R.id.rlAccount)
    void onCLickAccount() {
        mDialogPickAccount.registerPickAccount(new DialogPickAccount.AccountListener() {
            @Override
            public void onResultAccount(Account account) {
                mNewDebit.setIdAccount(account.getId());
                mTvAccountName.setText(account.getName());
            }
        }, false, mNewDebit.getIdAccount());
        mDialogPickAccount.show(getFragmentManager(), null);
    }

    @Click(R.id.rlStartDate)
    void onClickDate() {
        DayPicker dayPicker = DayPicker_.builder().build();
        dayPicker.registerPicker(new DayPicker.DatePickerListener() {
            @Override
            public void onResultDate(Date date) {
                mNewDebit.setStartDate(date);
                mTvStartDate.setText(DateTimeUtils.getInstance().getStringDateUs(date));
            }
        });
        dayPicker.show(getSupportFragmentManager(), null);
    }

    @Click(R.id.rlEndDate)
    void onClickEndDate() {
        DayPicker dayPicker = DayPicker_.builder().build();
        dayPicker.registerPicker(new DayPicker.DatePickerListener() {
            @Override
            public void onResultDate(Date date) {
                mNewDebit.setEndDate(date);
                mTvEndDate.setText(DateTimeUtils.getInstance().getStringDateUs(date));
            }
        });
        dayPicker.show(getSupportFragmentManager(), null);
    }
}
