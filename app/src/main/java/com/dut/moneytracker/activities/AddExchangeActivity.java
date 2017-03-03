package com.dut.moneytracker.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dut.moneytracker.R;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 23/02/2017.
 */

public class AddExchangeActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_recycler_debit);
        initView();
        showView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void showView() {
        /*RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCardAccount);
        CardAccountAdapter cardAccountAdapter = new CardAccountAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(cardAccountAdapter);*/
    }
}
