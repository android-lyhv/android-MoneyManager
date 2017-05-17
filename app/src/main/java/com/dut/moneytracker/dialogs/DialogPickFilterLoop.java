package com.dut.moneytracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.FilterLoop;

import org.androidannotations.annotations.EFragment;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */
@EFragment
public class DialogPickFilterLoop extends DialogFragment {
    private String[] mTitle = new String[]{"Tất cả", "Ngày", "Tuần", "Tháng", "Năm", "Thu Nhập", "Chi Tiêu"};
    private int[] idList = new int[]{FilterLoop.ALL, FilterLoop.DAY, FilterLoop.WEAK, FilterLoop.MONTH, FilterLoop.YEAR, FilterLoop.INCOME,
            FilterLoop.EXPENSES};
    private int selected = 0;
    FilterListener filterListener;

    public interface FilterListener {
        void onResult(int selectedId);
    }

    public void registerListener(FilterListener filterListener) {
        this.filterListener = filterListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_filter_title)
                .setSingleChoiceItems(mTitle, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected = which;
                        filterListener.onResult(idList[selected]);
                    }
                });
        return builder.create();
    }
}
