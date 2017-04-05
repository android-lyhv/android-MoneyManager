package com.dut.moneytracker.ui.base;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 16/03/2017.
 */

public class SpinnerAccountManger implements Spinner.OnItemSelectedListener {
    private AppCompatSpinner mSpinner;
    private List<Account> mAccounts;
    private Context mContext;

    public interface ItemSelectedListener {
        void onItemSelected(String accountId);
    }

    private ItemSelectedListener itemSelectedListener;

    public void registerSelectedItem(ItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public SpinnerAccountManger(Context context, AppCompatSpinner spinner) {
        mSpinner = spinner;
        mContext = context;
        initAccounts();
        onLoadItemSpinner();
    }

    private void initAccounts() {
        mAccounts = AccountManager.getInstance().getAccounts();
        if (mAccounts == null) {
            mAccounts = new ArrayList<>();
        }
    }

    private void onLoadItemSpinner() {
        String[] items = getListItemSpinner(mAccounts);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.item_selected_spiner, items);
        arrayAdapter.setDropDownViewResource(R.layout.item_list_spiner);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == mAccounts.size()) {
            itemSelectedListener.onItemSelected(null);
        } else {
            itemSelectedListener.onItemSelected(mAccounts.get(position).getId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setSelectItem(String accountId) {
        mSpinner.setSelection(getPosition(accountId));
    }

    private String[] getListItemSpinner(List<Account> accounts) {
        int size = accounts.size();
        String[] strings = new String[size + 1];
        for (int i = 0; i < size; i++) {
            strings[i] = accounts.get(i).getName();
        }
        strings[size] = mContext.getString(R.string.all_account);
        return strings;
    }

    private int getPosition(String accountId) {
        int size = mAccounts.size();
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(accountId, mAccounts.get(i).getId())) {
                return i;
            }
        }
        return size;
    }
}
