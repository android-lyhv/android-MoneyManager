package com.dut.moneytracker.ui.charts.objects;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 03/05/2017.
 */

public class MyPercentFormatter extends PercentFormatter {

    public MyPercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0.00");
    }

    public MyPercentFormatter(DecimalFormat format) {
        super(format);
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if (value <= 2) {
            return "";
        }
        return super.getFormattedValue(value, entry, dataSetIndex, viewPortHandler);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (value <= 2) {
            return "";
        }
        return super.getFormattedValue(value, axis);
    }

    @Override
    public int getDecimalDigits() {
        return super.getDecimalDigits();
    }
}
