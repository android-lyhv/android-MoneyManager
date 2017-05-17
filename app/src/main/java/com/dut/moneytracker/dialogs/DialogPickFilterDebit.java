package com.dut.moneytracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.SortDebit;

import org.androidannotations.annotations.EFragment;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */
@EFragment
public class DialogPickFilterDebit extends DialogFragment {
    private String[] mTitle = new String[]{"Tất cả", "Đã đóng", "Cho Vay", "Đi Vay", "Hết hạn"};
    private int[] idList = new int[]{SortDebit.ALL, SortDebit.CLOSE, SortDebit.LEND, SortDebit.BORROWED, SortDebit.EXPIRED};
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
                        dismiss();
                    }
                });
        return builder.create();
    }
}
