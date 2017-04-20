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
public class DialogPickAccount extends DialogFragment {
    private static DialogPickAccount sInstance;

    public static DialogPickAccount getInstance() {
        if (sInstance == null) {
            sInstance = DialogPickAccount_.builder().build();
        }
        return sInstance;
    }

    private List<Account> mAccounts;
    private AccountListener mAccountListener;

    public interface AccountListener {
        void onResultAccount(Account account);
    }

    /**
     * @param accountListener
     * @param status          = false - list not about account out side
     *                        true - list with account out side
     *                        list without currentIdAccount
     */
    public void registerPickAccount(AccountListener accountListener, boolean status, String currentIdAccount) {
        if (status) {
            mAccounts = AccountManager.getInstance().getAccounts(currentIdAccount);
        } else {
            mAccounts = AccountManager.getInstance().getAccountsNotOutside(currentIdAccount);
        }
        mAccountListener = accountListener;
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
                mAccountListener.onResultAccount(mAccounts.get(selected[0]));
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
        int size = mAccounts.size();
        String[] names = new String[size];
        for (int i = 0; i < size; i++) {
            names[i] = mAccounts.get(i).getName();
        }
        return names;
    }
}
