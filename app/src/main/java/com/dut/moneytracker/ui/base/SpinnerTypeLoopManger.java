package com.dut.moneytracker.ui.base;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.LoopType;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 16/03/2017.
 */

public class SpinnerTypeLoopManger implements Spinner.OnItemSelectedListener {
    private AppCompatSpinner mSpinner;
    private Context mContext;
    private String[] items = new String[]{"Ngày", "Tuần", "Tháng", "Năm"};
    private int[] listId = new int[]{LoopType.DAY, LoopType.DAY, LoopType.MONTH, LoopType.YEAR};

    public interface ItemSelectedListener {
        void onResultTypeLoop(int type);
    }

    private ItemSelectedListener itemSelectedListener;

    public void registerSelectedItem(ItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public SpinnerTypeLoopManger(Context context, AppCompatSpinner spinner) {
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
        itemSelectedListener.onResultTypeLoop(listId[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setSelectItem(int type) {
        mSpinner.setSelection(type);
    }
}
