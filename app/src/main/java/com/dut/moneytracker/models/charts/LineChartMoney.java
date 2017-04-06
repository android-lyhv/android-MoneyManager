package com.dut.moneytracker.models.charts;

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
public class LineChartMoney {
    private static final String TAG = LineChartMoney.class.getSimpleName();
    private List<ValueLineChart> valueLineCharts;
    private LineData mLineData;
    private LineDataSet mDataSet;
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


    public void setValueLineCharts(List<ValueLineChart> valueLineCharts) {
        this.valueLineCharts = valueLineCharts;
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

        LineChartMoney lineChartMoney;

        public Builder(LineChart lineChart) {
            lineChartMoney = new LineChartMoney();
            lineChartMoney.setChart(lineChart);
        }

        public Builder setValueChartAmounts(List<ValueLineChart> valueLineCharts) {
            lineChartMoney.setValueLineCharts(valueLineCharts);
            return this;
        }

        public Builder setColorLine(String color) {
            lineChartMoney.setColorCode(color);
            return this;
        }

        public Builder setCirleStokce(int stoke) {
            lineChartMoney.setStokeCircle(stoke);
            return this;
        }

        public Builder setLabel(String label) {
            lineChartMoney.setLabel(label);
            return this;
        }

        public LineChartMoney build() {
            return lineChartMoney;
        }

    }

    private List<Entry> getListEntry() {
        List<Entry> entries = new ArrayList<>();
        int size = valueLineCharts.size();
        for (int i = 0; i < size; i++) {
            Log.d(TAG, "getListEntry: " + valueLineCharts.get(i).getAmount());
            Entry entry = new Entry();
            entry.setX(i);
            entry.setY(CurrencyUtils.getInstance().getFloatMoney(valueLineCharts.get(i).getAmount()));
            entries.add(entry);
        }
        return entries;
    }

    private void onSetupChart() {
        mDataSet = new LineDataSet(getListEntry(), getLabel());
        mDataSet.setCircleColor(Color.parseColor(getColorCode()));
        mDataSet.setColor(Color.parseColor(getColorCode()));
        mDataSet.setDrawValues(false);
        mLineData = new LineData(mDataSet);
        mChart.setData(mLineData);
        mChart.getAxisLeft().setDrawGridLines(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDescription(null);
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
            return valueLineCharts.get((int) value).getLabel();
        }
    }
}
