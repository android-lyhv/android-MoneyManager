package com.dut.moneytracker.ui.charts.objects;

import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;

import io.realm.RealmResults;
import lombok.Data;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 10/04/2017.
 */
@Data
public class ValueCategoryChart {
    private String amount;
    private Category category;
    private RealmResults<Exchange> exchanges;
}
