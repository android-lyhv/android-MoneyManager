package com.dut.moneytracker.adapter;

import android.view.View;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 6/14/2016.
 */
 public interface ClickItemRecyclerListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
