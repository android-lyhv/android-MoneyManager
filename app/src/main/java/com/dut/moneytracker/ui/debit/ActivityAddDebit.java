package com.dut.moneytracker.ui.debit;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dut.moneytracker.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 13/04/2017.
 */
@EActivity(R.layout.activity_add_debit)
@OptionsMenu(R.menu.menu_detail_exchange)
public class ActivityAddDebit extends AppCompatActivity {
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @AfterViews
    void init() {
        initView();
    }

    void initView() {
        setTitle(getString(R.string.toobar_tilte_new_debit));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
    }

    @OptionsItem(android.R.id.home)
    void onCLickClose() {
        finish();
    }

    @OptionsItem(R.id.actionDelete)
    void onCLickDelete() {

    }

    @OptionsItem(R.id.actionSave)
    void onClickSave() {

    }
}
