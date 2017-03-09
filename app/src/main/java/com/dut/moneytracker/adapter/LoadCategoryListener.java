package com.dut.moneytracker.adapter;

import com.dut.moneytracker.objects.Category;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */

public interface LoadCategoryListener {
    void onLoadGroupCategory();
    void onLoadChildCategory(List<Category> categories);
}
