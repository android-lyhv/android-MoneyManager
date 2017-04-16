package com.dut.moneytracker.ui.category;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.LoadCategoryListener;
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.objects.Category;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */

public class ActivityPickCategory extends AppCompatActivity implements LoadCategoryListener {
    private static final String TAG = ActivityPickCategory.class.getSimpleName();
    private Toolbar mToolbar;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private boolean isViewChildCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_category);
        fragmentManager = getFragmentManager();
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isViewChildCategory) {
                    onLoadGroupCategory();
                } else {
                    this.finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onLoadGroupCategory();
    }

    @Override
    public void onLoadGroupCategory() {
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
        isViewChildCategory = false;
        FragmentGroupCategory fragmentGroupCategory = new FragmentGroupCategory();
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
                Log.d(TAG, "onResultCategory: ");
                sentBackCategory(category);
                finish();
            }
        });
    }

    private void sentBackCategory(Category category) {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.extra_category), category);
        setResult(IntentCode.PICK_CATEGORY, intent);
    }
}
