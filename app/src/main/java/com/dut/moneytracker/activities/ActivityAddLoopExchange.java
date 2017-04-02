package com.dut.moneytracker.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.dut.moneytracker.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 19/03/2017.
 */
@EActivity(R.layout.add_detail_default_exchange)
@OptionsMenu(R.menu.menu_detail_exchange)
public class ActivityAddLoopExchange extends AppCompatActivity {
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @AfterViews
    void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
    }

    @OptionsItem(android.R.id.home)
    void onClickHomeBack() {
        finish();
    }

    @OptionsItem(R.id.actionSave)
    void onClickSave() {
        finish();
    }

    @OptionsItem(R.id.actionDelete)
    void onClickDelete() {
        finish();
    }

}
