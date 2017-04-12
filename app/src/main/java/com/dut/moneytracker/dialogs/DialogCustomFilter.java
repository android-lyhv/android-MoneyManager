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
@EFragment(R.layout.dialog_custom_filter)
public class DialogCustomFilter extends DialogFragment {
    private static final String TAG = DialogCustomFilter.class.getSimpleName();
    @ViewById(R.id.tvToDate)
    TextView mTvToDate;
    @ViewById(R.id.tvFromDate)
    TextView mTvFromDate;
    FilterListener mFilterListener;
    private DayPicker mDayPicker;
    private Date mFromDate = new Date();
    private Date mToDate = new Date();

    @AfterViews
    void init() {
        if (mFromDate == null || mToDate == null) {
            mToDate = new Date();
            mFromDate = DateTimeUtils.getInstance().getNextDate(mToDate, -1);
        }
        mTvFromDate.setText(DateTimeUtils.getInstance().getStringFullDate(mFromDate));
        mTvToDate.setText(DateTimeUtils.getInstance().getStringFullDate(mToDate));
    }

    public interface FilterListener {
        void onResultDate(Date fromDate, Date toDate);
    }

    public void registerFilterListener(FilterListener filterListener, Date lastFromDate, Date lastToDate) {
        mFilterListener = filterListener;
        mFromDate = lastFromDate;
        mToDate = lastToDate;
    }

    @Click(R.id.tvFromDate)
    void onCLickFromDate() {
        mDayPicker = DayPicker_.builder().build();
        mDayPicker.show(getChildFragmentManager(), TAG);
        mDayPicker.registerPicker(new DayPicker.DatePickerListener() {
            @Override
            public void onResultDate(Date date) {
                mFromDate = date;
                mTvFromDate.setText(DateTimeUtils.getInstance().getStringFullDate(mFromDate));
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
                mToDate = date;
                mTvToDate.setText(DateTimeUtils.getInstance().getStringFullDate(mToDate));
            }
        });
    }

    @Click(R.id.tvConfirm)
    void onClickConfirm() {
        if (!DateTimeUtils.getInstance().isValidateFromDateToDate(mFromDate, mToDate)) {
            Toast.makeText(getContext(), "Date not validate!", Toast.LENGTH_SHORT).show();
            return;
        }
        mFilterListener.onResultDate(mFromDate, mToDate);
        dismiss();
    }

    @Click(R.id.tvCancel)
    void onClickCancel() {
        dismiss();
    }
}
