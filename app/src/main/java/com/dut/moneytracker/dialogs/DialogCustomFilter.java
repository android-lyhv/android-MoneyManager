package com.dut.moneytracker.dialogs;

import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.dut.moneytracker.view.DayPicker;
import com.dut.moneytracker.view.DayPicker_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 27/03/2017.
 */
@EFragment(R.layout.custom_filter)
public class DialogCustomFilter extends DialogFragment {
    private static final String TAG = DialogCustomFilter.class.getSimpleName();
    @ViewById(R.id.tvToDate)
    TextView mTvToDate;
    @ViewById(R.id.tvFromDate)
    TextView mTvFromDate;
    FilterListener mFilterListener;
    private DayPicker mDayPicker;
    private Date fromDate;
    private Date toDate;

    @AfterViews
    void init() {
        mTvFromDate.setText(DateTimeUtils.getInstance().getStringFullDate(new Date()));
        mTvToDate.setText(DateTimeUtils.getInstance().getStringFullDate(new Date()));
    }

    public interface FilterListener {
        void onResultDate(Date fromDate, Date toDate);
    }

    public void registerFilterListener(FilterListener mFilterListener) {
        this.mFilterListener = mFilterListener;
    }

    @Click(R.id.tvFromDate)
    void onCLickFromDate() {
        mDayPicker = DayPicker_.builder().build();
        mDayPicker.show(getChildFragmentManager(), TAG);
        mDayPicker.registerPicker(new DayPicker.DatePickerListener() {
            @Override
            public void onResultDate(Date date) {
                fromDate = date;
                mTvFromDate.setText(DateTimeUtils.getInstance().getStringFullDate(fromDate));
            }
        });

    }

    @Click(R.id.tvToDate)
    void onCLickToDate() {
        mDayPicker = DayPicker_.builder().build();
        mDayPicker.show(getChildFragmentManager(), TAG);
        mDayPicker.registerPicker(new DayPicker.DatePickerListener() {
            @Override
            public void onResultDate(Date date) {
                toDate = date;
                mTvToDate.setText(DateTimeUtils.getInstance().getStringFullDate(toDate));
            }
        });
    }

    @Click(R.id.tvConfirm)
    void onClickConfirm() {
        if (!DateTimeUtils.getInstance().isValidateFromDateToDate(fromDate, toDate)) {
            Toast.makeText(getContext(), "Date not validate!", Toast.LENGTH_SHORT).show();
            return;
        }
        mFilterListener.onResultDate(fromDate, toDate);
        dismiss();
    }

    @Click(R.id.tvCancel)
    void onClickCancel() {
        dismiss();
    }
}
