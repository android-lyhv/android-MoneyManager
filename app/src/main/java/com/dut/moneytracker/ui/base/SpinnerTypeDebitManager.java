package com.dut.moneytracker.ui.base;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.DebitType;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 16/03/2017.
 */

public class SpinnerTypeDebitManager implements Spinner.OnItemSelectedListener {
    private AppCompatSpinner mSpinner;
    private Context mContext;
    private String[] items = new String[]{"Cho Vay", "Đi Vay"};
    private int[] listId = new int[]{DebitType.LEND, DebitType.BORROWED};
    private int itemSelected;

    public interface ItemSelectedListener {
        void onResultTypeDebit(int type);
    }

    private ItemSelectedListener itemSelectedListener;

    public void registerSelectedItem(ItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public SpinnerTypeDebitManager(Context context, AppCompatSpinner spinner) {
        mSpinner = spinner;
        mContext = context;
        onLoadItemSpinner();
    }


    private void onLoadItemSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, items);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemSelected = position;
        itemSelectedListener.onResultTypeDebit(listId[itemSelected]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setSelectItem(int typeDebit) {
        int index = 0;
        int length = listId.length;
        for (int i = 0; i < length; i++) {
            if (typeDebit == listId[i]) {
                index = i;
                break;
            }
        }
        mSpinner.setSelection(index);
    }

    public int getTypeItemSelected() {
        return listId[itemSelected];
    }
}
