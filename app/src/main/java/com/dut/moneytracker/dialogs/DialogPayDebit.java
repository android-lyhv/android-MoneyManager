package com.dut.moneytracker.dialogs;

import android.support.v4.app.DialogFragment;

import com.dut.moneytracker.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 27/03/2017.
 */
@EFragment(R.layout.dialog_pay_debit)
public class DialogPayDebit extends DialogFragment {
    public interface ClickListener {
        void onClickPartial();

        void onClickFinishDebit();
    }

    public ClickListener mClickListener;

    @AfterViews
    void init() {
    }

    public interface DescriptionListener {
        void onResult(String content);
    }

    public void register(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    DescriptionListener descriptionListener;

    @Click(R.id.tvConfirm)
    void onClickConfirm() {
        mClickListener.onClickFinishDebit();
        dismiss();
    }

    @Click(R.id.tvCancel)
    void onClickCancel() {

        dismiss();
    }
    @Click(R.id.tvPay)
    void onClickPay() {
        mClickListener.onClickPartial();
        dismiss();
    }
}
