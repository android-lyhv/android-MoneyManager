package com.dut.moneytracker.ui.charts.objects;

import android.content.Context;
import android.graphics.Color;

import com.dut.moneytracker.R;
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
    private List<ValueLineChart> mValueLineCharts = new ArrayList<>();
    private LineData mLineData;
    private LineDataSet mLineDataSet;
    private LineChart mChart;
    private String mColorChart;
    private Context mContext;

    public LineChartMoney(Context context, LineChart chart) {
        mChart = chart;
        mContext = context;
        onSetupChart();
    }

    public void setColorChart(String mColorChart) {
        this.mColorChart = mColorChart;
    }

    public void updateNewValueLineChart(List<ValueLineChart> valueLineCharts) {
        mValueLineCharts.clear();
        mValueLineCharts.addAll(valueLineCharts);
        int size = mValueLineCharts.size();
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Entry entry = new Entry();
            entry.setX(i);
            entry.setY(CurrencyUtils.getInstance().getFloatMoney(mValueLineCharts.get(size - i - 1).getAmount()));
            entries.add(entry);
        }

        mLineDataSet = new LineDataSet(entries, mContext.getString(R.string.label_linechart));
        mLineDataSet.setCircleColor(Color.parseColor(mColorChart));
        mLineDataSet.setColor(Color.parseColor(mColorChart));
        mLineDataSet.setDrawValues(false);
        mLineData = new LineData(mLineDataSet);
        mChart.setData(mLineData);
    }

    private void onSetupChart() {
        mChart.getAxisLeft().setDrawGridLines(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDescription(null);
        mChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter());
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    public void notifyDataSetChanged() {
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    private class MyXAxisValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValueLineCharts.get((int) (axis.getAxisMaximum() - (int) value)).getLabel();
        }
    }
}
