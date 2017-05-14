package com.dut.moneytracker.dialogs;

import android.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.calculator.CalculatorAdapter;
import com.dut.moneytracker.currency.CurrencyExpression;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */
@EFragment(R.layout.dialog_calculator)
public class DialogCalculator extends DialogFragment implements CalculatorAdapter.ItemCalClickListener {
    @ViewById(R.id.recyclerCalculator)
    RecyclerView recyclerView;
    @ViewById(R.id.tvAmount)
    TextView tvAmount;

    public static DialogCalculator getInstance() {
        return DialogCalculator_.builder().build();
    }

    public static final int MAX = 12;

    public interface ResultListener {
        void onResult(String amount);
    }

    private ResultListener resultListener;

    private String mAmount = "";

    public void registerResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void setAmount(String amount) {
        if (TextUtils.equals("0", amount)) {
            mAmount = "";
        } else {
            mAmount = amount;
        }
    }

    @AfterViews
    void init() {
        tvAmount.setText(mAmount);
        iniRecyclerView();
    }

    private void iniRecyclerView() {
        CalculatorAdapter mCalculatorAdapter = new CalculatorAdapter(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(mCalculatorAdapter);
        mCalculatorAdapter.registerClickCal(this);
    }

    @Override
    public void onCLick(String s) {
        if (TextUtils.equals(s, "0")) {
            onSetValueAmount("0");
            return;
        }
        if (TextUtils.equals(s, "1")) {
            onSetValueAmount("1");
            return;
        }
        if (TextUtils.equals(s, "2")) {
            onSetValueAmount("2");
            return;
        }
        if (TextUtils.equals(s, "3")) {
            onSetValueAmount("3");
            return;
        }
        if (TextUtils.equals(s, "4")) {
            onSetValueAmount("4");
            return;
        }
        if (TextUtils.equals(s, "5")) {
            onSetValueAmount("5");
            return;
        }
        if (TextUtils.equals(s, "6")) {
            onSetValueAmount("6");
            return;
        }
        if (TextUtils.equals(s, "7")) {
            onSetValueAmount("7");
            return;
        }
        if (TextUtils.equals(s, "8")) {
            onSetValueAmount("8");
            return;
        }
        if (TextUtils.equals(s, "9")) {
            onSetValueAmount("9");
            return;
        }
        if (TextUtils.equals(s, ".")) {
            onSetValueAmount(".");
            return;
        }
        if (TextUtils.equals(s, "=")) {
            String value = tvAmount.getText().toString().trim();
            if (!CurrencyExpression.getInstance().isValidateMoney(value)) {
                return;
            }
            resultListener.onResult(value);
            dismiss();
        }
    }


    @Click(R.id.tvCal0)
    void onClickTvCalZero() {
        onSetValueAmount("0");
    }

    @Click(R.id.imgBack)
    void onClickBackNumber() {
        String current = tvAmount.getText().toString();
        if (!TextUtils.isEmpty(current)) {
            tvAmount.setText(current.substring(0, current.length() - 1));
        }
    }

    @Click(R.id.tvClean)
    void onClickClean() {
        tvAmount.setText("");
    }

    @Click(R.id.tvCancel)
    void onClickCancel() {
        dismiss();
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
