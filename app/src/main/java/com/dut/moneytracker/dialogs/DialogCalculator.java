package com.dut.moneytracker.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dut.moneytracker.R;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */

public class DialogCalculator extends DialogFragment implements View.OnClickListener {
    public interface ResultListener {

        void onResult(float amount);
    }

    private ResultListener resultListener;

    private TextView tvOk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculator, container, false);
        initView(view);
        return view;
    }

    public void registerResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void initView(View view) {
        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvOk:
                resultListener.onResult(0f);
                dismiss();
                break;
        }
    }

}
