package com.dut.moneytracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.dut.moneytracker.constant.TypeView;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */

public class DialogPickFilter extends DialogFragment {
    private String[] filter = new String[]{"Tất cả", "Ngày", "Tuần", "Tháng", "Năm", "Khoảng thời gian"};
    private int[] id = new int[]{TypeView.ALL, TypeView.DAY, TypeView.WEAK, TypeView.MONTH, TypeView.YEAR, TypeView.CUSTOM};
    private int mIdFilter = 0;
    private FilterListener filterListener;

    public interface FilterListener {
        void onResult(int idFilter);
    }

    public void registerFilter(int idFilter, FilterListener filterListener) {
        mIdFilter = idFilter;
        this.filterListener = filterListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Chọn hiển thị!")
                .setSingleChoiceItems(filter, mIdFilter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mIdFilter = id[which];
                        filterListener.onResult(mIdFilter);
                        dismiss();
                    }
                }).setPositiveButton("Bây giờ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                filterListener.onResult(mIdFilter);
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();
    }
}
