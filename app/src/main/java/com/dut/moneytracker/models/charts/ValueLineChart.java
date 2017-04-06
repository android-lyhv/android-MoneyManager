package com.dut.moneytracker.models.charts;

import java.util.Date;

import lombok.Data;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 10/03/2017.
 */
@Data
public class ValueLineChart {
    private Date date;
    private String amount;
    private String label;

    public ValueLineChart(Date date, String amount, String label) {
        this.date = date;
        this.amount = amount;
        this.label = label;
    }
}
