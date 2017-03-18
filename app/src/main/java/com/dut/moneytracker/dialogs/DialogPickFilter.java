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
    private String[] filter = new String[]{"All", "Day", "Month", "Week", "Year", "Custom"};
    private int[] id = new int[]{TypeView.ALL, TypeView.DAY, TypeView.MONTH, TypeView.WEAK, TypeView.YEAR, TypeView.CUSTOM};
    private int idFilter = 0;
    private FilterListener filterListener;

    public interface FilterListener {
        void onResult(int idFilter);
    }

    public void registerFilter(FilterListener filterListener) {
        this.filterListener = filterListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("Chọn hiển thị!")
                .setSingleChoiceItems(filter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        idFilter = id[which];
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                filterListener.onResult(idFilter);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();
    }
}
