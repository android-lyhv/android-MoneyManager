package com.dut.moneytracker.ui.charts.objects;

import lombok.Data;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 05/04/2017.
 */
@Data
public class ValuePieChart {
    private String nameGroup;
    private int colorGroup;
    private float amount;
    private String amountString;
}
