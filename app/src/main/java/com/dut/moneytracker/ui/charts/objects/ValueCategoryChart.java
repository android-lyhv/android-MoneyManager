package com.dut.moneytracker.ui.charts.objects;

import com.dut.moneytracker.objects.Category;

import lombok.Data;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 10/04/2017.
 */
@Data
public class ValueCategoryChart{
    private String amount;
    private Category category;
}
