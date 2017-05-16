package com.dut.moneytracker.ui.category;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.LoadCategoryListener;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.objects.Category;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */
@EActivity(R.layout.activity_pick_category)
public class ActivityPickCategory extends AppCompatActivity implements LoadCategoryListener {
    private Toolbar mToolbar;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private boolean isViewChildCategory;
    @Extra
    int mType;

    @AfterViews
    void init() {
        if (mType== ExchangeType.INCOME){
            setTitle(getString(R.string.icome_type));
        }else {
            setTitle(getString(R.string.expresen_type));
        }
        fragmentManager = getFragmentManager();
        initView();
        onLoadGroupCategory();
    }

    @OptionsItem(android.R.id.home)
    void onClickHome() {
        if (isViewChildCategory) {
            onLoadGroupCategory();
        } else {
            this.finish();
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
    }

    @Override
    public void onLoadGroupCategory() {
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
        isViewChildCategory = false;
        FragmentGroupCategory fragmentGroupCategory = FragmentGroupCategory_.builder().mType(mType).build();
        fragmentGroupCategory.registerLoadChildCategory(this);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentCategory, fragmentGroupCategory);
        fragmentTransaction.commit();
    }

    @Override
    public void onLoadChildCategory(final RealmResults<Category> categories) {
        isViewChildCategory = true;
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left);
        final FragmentChildCategory fragmentChildCategory = new FragmentChildCategory();
        fragmentChildCategory.setCategories(categories);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentCategory, fragmentChildCategory);
        fragmentTransaction.commit();
        fragmentChildCategory.registerPickCategory(new FragmentChildCategory.PickCategoryListener() {
            @Override
            public void onResultCategory(Category category) {
                sentBackCategory(category);
                finish();
            }
        });
    }

    @Override
    public void onPickCategory(Category category) {
        sentBackCategory(category);
        finish();
    }

    private void sentBackCategory(Category category) {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.extra_category), category);
        setResult(IntentCode.PICK_CATEGORY, intent);
    }
}
