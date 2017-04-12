package com.dut.moneytracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.FilterType;
import com.dut.moneytracker.objects.Filter;

import org.androidannotations.annotations.EFragment;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */
@EFragment
public class DialogPickFilter extends DialogFragment {
    private String[] mTitle = new String[]{"Tất cả", "Ngày", "Tháng", "Năm", "Khoảng thời gian"};
    private int[] id = new int[]{FilterType.ALL, FilterType.DAY, FilterType.MONTH, FilterType.YEAR, FilterType.CUSTOM};
    private Filter mFilter;
    private FilterListener mFilterListener;

    public interface FilterListener {
        void onResult(Filter filter);
    }

    public void registerFilter(Filter filter, FilterListener filterListener) {
        mFilter = filter;
        mFilterListener = filterListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_filter_title)
                .setSingleChoiceItems(mTitle, mFilter.getTypeFilter(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mFilter.setTypeFilter(id[which]);
                        mFilterListener.onResult(mFilter);
                        dismiss();
                    }
                }).setPositiveButton(R.string.dialog_filter_now_time, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mFilterListener.onResult(mFilter);
            }
        }).setNegativeButton(R.string.dialog_filter_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (mFilter.getTypeFilter() == FilterType.CUSTOM) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAlpha(0.5f);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAlpha(1f);
                }
            }
        });
        return alertDialog;
    }
}
