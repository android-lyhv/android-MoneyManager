package com.dut.moneytracker.dialogs;

import android.app.DialogFragment;
import android.text.TextUtils;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.currency.CurrencyExpression;
import com.dut.moneytracker.currency.CurrencyUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */
@EFragment(R.layout.dialog_calculator)
public class DialogCalculator extends DialogFragment {
    private static DialogCalculator dialogCalculator;

    public static DialogCalculator getInstance() {
        if (dialogCalculator == null) {
            dialogCalculator = DialogCalculator_.builder().build();
        }
        return dialogCalculator;
    }

    public static final int MAX = 12;

    public interface ResultListener {
        void onResult(String amount);
    }

    private ResultListener resultListener;
    @ViewById(R.id.tvAmount)
    TextView tvAmount;
    private String mAmount = "";

    public void registerResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void setAmount(String amount) {
        if (TextUtils.isEmpty(amount) || CurrencyUtils.getInstance().getFloatMoney(amount) == 0) {
            return;
        }
        mAmount = amount;
    }

    @AfterViews
    void init() {
        tvAmount.setText(mAmount);
    }

    @Click(R.id.tvOk)
    void onClickOk() {
        String value = tvAmount.getText().toString().trim();
        if (!CurrencyExpression.getInstance().isValidateMoney(value)) {
            return;
        }
        resultListener.onResult(value);
        dismiss();
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

    @Click(R.id.tvCalClean)
    void onClickClean() {
        tvAmount.setText("");
    }

    private void onSetValueAmount(String chart) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tvAmount.getText().toString());
        stringBuilder.append(chart);
        if (CurrencyExpression.getInstance().isValidateTypeMoney(stringBuilder.toString())) {
            if (stringBuilder.toString().length() > MAX) {
                return;
            }
            tvAmount.setText(stringBuilder.toString());
        }
    }
}
