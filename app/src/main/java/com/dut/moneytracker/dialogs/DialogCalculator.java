package com.dut.moneytracker.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.currency.CurrencyExpression;

import java.util.Locale;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */

public class DialogCalculator extends DialogFragment implements View.OnClickListener {
    public interface ResultListener {

        void onResult(String amount);
    }

    private ResultListener resultListener;

    private TextView tvOk;
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
    private TextView tvCalClean;
    private ImageView imgCalBack;
    private TextView tvCalDot;
    private TextView tvAmount;
    private StringBuilder stringBuilder = new StringBuilder();
    private String amount;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_calculator, container, false);
        initView(view);
        return view;
    }

    public void registerResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void initView(View view) {
        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(this);
        tvCal0 = (TextView) view.findViewById(R.id.tvCal0);
        tvCal0.setOnClickListener(this);
        tvCal1 = (TextView) view.findViewById(R.id.tvCal1);
        tvCal1.setOnClickListener(this);
        tvCal2 = (TextView) view.findViewById(R.id.tvCal2);
        tvCal2.setOnClickListener(this);
        tvCal3 = (TextView) view.findViewById(R.id.tvCal3);
        tvCal3.setOnClickListener(this);
        tvCal4 = (TextView) view.findViewById(R.id.tvCal4);
        tvCal4.setOnClickListener(this);
        tvCal5 = (TextView) view.findViewById(R.id.tvCal5);
        tvCal5.setOnClickListener(this);
        tvCal6 = (TextView) view.findViewById(R.id.tvCal6);
        tvCal6.setOnClickListener(this);
        tvCal7 = (TextView) view.findViewById(R.id.tvCal7);
        tvCal7.setOnClickListener(this);
        tvCal8 = (TextView) view.findViewById(R.id.tvCal8);
        tvCal8.setOnClickListener(this);
        tvCal9 = (TextView) view.findViewById(R.id.tvCal9);
        tvCal9.setOnClickListener(this);
        tvCalClean = (TextView) view.findViewById(R.id.tvCalClean);
        tvCalClean.setOnClickListener(this);
        imgCalBack = (ImageView) view.findViewById(R.id.imgCalBack);
        imgCalBack.setOnClickListener(this);
        tvCalDot = (TextView) view.findViewById(R.id.tvCalDot);
        tvCalDot.setOnClickListener(this);
        tvAmount = (TextView) view.findViewById(R.id.tvAmount);
        tvAmount.setOnClickListener(this);
        tvAmount.setText(String.format(Locale.US, "%s", amount));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvOk:
                String value = tvAmount.getText().toString().trim();
                if (!CurrencyExpression.getInstance().isValidateMoney(value)) {
                    return;
                }
                resultListener.onResult(value);
                dismiss();
                break;
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
                setValue("6");
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
            case R.id.tvCalClean:
                tvAmount.setText("");
                break;
            case R.id.imgCalBack:
                String current = tvAmount.getText().toString();
                if (!TextUtils.isEmpty(current)) {
                    tvAmount.setText(current.substring(0, current.length() - 1));
                }
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
