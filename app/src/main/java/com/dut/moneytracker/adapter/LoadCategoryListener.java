package com.dut.moneytracker.adapter;

import com.dut.moneytracker.objects.Category;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */

public interface LoadCategoryListener {
    void onLoadGroupCategory();
    void onLoadChildCategory(RealmResults<Category> categories);
}
