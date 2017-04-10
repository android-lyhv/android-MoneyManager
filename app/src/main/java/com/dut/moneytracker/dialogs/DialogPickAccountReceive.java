package com.dut.moneytracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;

import org.androidannotations.annotations.EFragment;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/03/2017.
 */
@EFragment
public class DialogPickAccountReceive extends DialogFragment {
    private List<Account> accounts = AccountManager.getInstance().getAccounts();
    private AccountListener accountListener;

    public interface AccountListener {
        void onResultAccount(Account account);
    }

    public void registerPickAccount(AccountListener accountListener) {
        this.accountListener = accountListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] names = getListAccountName();
        final int[] selected = {0};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.dialog_title_account)
                .setSingleChoiceItems(names, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected[0] = which;
                    }

                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (selected[0] == names.length - 1) {
                    accountListener.onResultAccount(null);
                } else {
                    accountListener.onResultAccount(accounts.get(selected[0]));
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();
    }

    private String[] getListAccountName() {
        int size = accounts.size();
        String[] names = new String[size + 1];
        for (int i = 0; i < size; i++) {
            names[i] = accounts.get(i).getName();
        }
        names[size] = getString(R.string.account_other);
        return names;
    }
}
