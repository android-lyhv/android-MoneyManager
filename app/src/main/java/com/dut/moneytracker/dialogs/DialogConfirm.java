package com.dut.moneytracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.dut.moneytracker.R;

import org.androidannotations.annotations.EFragment;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 14/03/2017.
 */
@EFragment
public class DialogConfirm extends DialogFragment {
    String message;
    private static DialogConfirm sInstance;

    public static DialogConfirm getInstance() {
        if (sInstance == null) {
            sInstance = DialogConfirm_.builder().build();
        }
        return sInstance;
    }

    public interface ClickListener {
        void onClickResult(boolean value);
    }

    ClickListener clickListener;

    public void registerClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clickListener.onClickResult(true);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}
