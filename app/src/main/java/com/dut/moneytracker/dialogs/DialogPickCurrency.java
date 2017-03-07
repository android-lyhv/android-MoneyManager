package com.dut.moneytracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.CurrencyManager;
import com.dut.moneytracker.objects.Currency;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */

public class DialogPickCurrency extends DialogFragment {
    private List<Currency> currencies = CurrencyManager.getInstance().getCurrencyCodes();
    private ResultListener resultListener;

    public interface ResultListener {
        void onResultCurrencyCode(String code);
    }

    public void registerResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] list = getListCode();
        final int[] selected = {0};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.dialoag_title_currency)
                .setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected[0] = which;
                    }

                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                resultListener.onResultCurrencyCode(list[selected[0]]);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();
    }

    private String[] getListCode() {
        int size = currencies.size();
        String[] listCurrencyCode = new String[size];
        for (int i = 0; i < size; i++) {
            listCurrencyCode[i] = currencies.get(i).getCurrencyCode();
        }
        return listCurrencyCode;
    }
}
