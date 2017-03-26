package com.dut.moneytracker.charts;

import java.util.Date;

import lombok.Data;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 10/03/2017.
 */
@Data
public class ValueChartAmount {
    private Date date;
    private String amount;
    private String label;

    public ValueChartAmount(Date date, String amount, String label) {
        this.date = date;
        this.amount = amount;
        this.label = label;
    }
}
