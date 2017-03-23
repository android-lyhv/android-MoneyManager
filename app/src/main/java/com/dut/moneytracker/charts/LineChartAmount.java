package com.dut.moneytracker.charts;

import android.graphics.Color;
import android.util.Log;

import com.dut.moneytracker.currency.CurrencyUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 10/03/2017.
 */
public class LineChartAmount {
    private static final String TAG = LineChartAmount.class.getSimpleName();
    private List<ValueChartAmount> valueChartAmounts;
    private LineData mLineData;
    private LineDataSet lineDataSet;
    private String colorCode = "#FF028761";
    private int stokeCircle;
    private String label;
    private LineChart mChart;

    public LineChart getmChart() {
        return mChart;
    }

    public void setChart(LineChart mChart) {
        this.mChart = mChart;
    }

    public void setStokeCircle(int stokeCircle) {
        this.stokeCircle = stokeCircle;
    }


    public void setValueChartAmounts(List<ValueChartAmount> valueChartAmounts) {
        this.valueChartAmounts = valueChartAmounts;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public int getStokeCircle() {
        return stokeCircle;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static class Builder {

        LineChartAmount lineChartAmount;

        public Builder(LineChart lineChart) {
            lineChartAmount = new LineChartAmount();
            lineChartAmount.setChart(lineChart);
        }

        public Builder setValueChartAmounts(List<ValueChartAmount> valueChartAmounts) {
            lineChartAmount.setValueChartAmounts(valueChartAmounts);
            return this;
        }

        public Builder setColorLine(String color) {
            lineChartAmount.setColorCode(color);
            return this;
        }

        public Builder setCirleStokce(int stoke) {
            lineChartAmount.setStokeCircle(stoke);
            return this;
        }

        public Builder setLabel(String label) {
            lineChartAmount.setLabel(label);
            return this;
        }

        public LineChartAmount build() {
            return lineChartAmount;
        }

    }

    private List<Entry> getListEntry() {
        List<Entry> entries = new ArrayList<>();
        int size = valueChartAmounts.size();
        for (int i = 0; i < size; i++) {
            Log.d(TAG, "getListEntry: " + valueChartAmounts.get(i).getAmount());
            Entry entry = new Entry();
            entry.setX(i);
            entry.setY(CurrencyUtils.getInstance().getFloatMoney(valueChartAmounts.get(i).getAmount()));
            entries.add(entry);
        }
        return entries;
    }

    private void onSetupChart() {
        lineDataSet = new LineDataSet(getListEntry(), getLabel());
        lineDataSet.setCircleColor(Color.parseColor(getColorCode()));
        lineDataSet.setColor(Color.parseColor(getColorCode()));
        lineDataSet.setDrawValues(false);
        mLineData = new LineData(lineDataSet);
        mChart.setData(mLineData);
        mChart.getAxisLeft().setDrawGridLines(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter());
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    }
    public void onDraw() {
        onSetupChart();
        mChart.invalidate();
    }

    private class MyXAxisValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return valueChartAmounts.get((int) value).getLabel();
        }
    }
}
