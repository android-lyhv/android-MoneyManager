package com.dut.moneytracker.charts;

import com.dut.moneytracker.utils.DateTimeUtils;

import java.util.Date;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 10/03/2017.
 */

public class ValueChartAmount {
    private Date date;
    private String amount;
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ValueChartAmount(Date date, String amount, String label) {
        this.date = date;
        this.amount = amount;
        this.label = label;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return DateTimeUtils.getInstance().getStringDate(date) + ":  " + amount;
    }
}
