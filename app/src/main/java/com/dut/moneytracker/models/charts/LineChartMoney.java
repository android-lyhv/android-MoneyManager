package com.dut.moneytracker.models.charts;

import android.content.Context;
import android.support.v4.content.ContextCompat;

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
    private static final String TAG = LineChartMoney.class.getSimpleName();
    private List<ValueLineChart> mValueLineCharts = new ArrayList<>();
    private LineData mLineData;
    private LineDataSet mLineDataSet;
    private LineChart mChart;
    private List<Entry> mEntries = new ArrayList<>();
    private int mColorChart;
    private Context mContext;

    public LineChartMoney(Context context, LineChart chart, int colorChart) {
        mChart = chart;
        mContext = context;
        mColorChart = colorChart;
        onSetupChart();
    }

    public void updateNewValueLineChart(List<ValueLineChart> valueLineCharts) {
        mValueLineCharts.clear();
        mValueLineCharts.addAll(valueLineCharts);
        int size = mValueLineCharts.size();
        for (int i = 0; i < size; i++) {
            Entry entry = new Entry();
            entry.setX(i);
            entry.setY(CurrencyUtils.getInstance().getFloatMoney(mValueLineCharts.get(i).getAmount()));
            mEntries.add(entry);
        }
        mLineDataSet = new LineDataSet(mEntries, mContext.getString(R.string.label_linechart));
        mLineDataSet.setCircleColor((ContextCompat.getColor(mContext, mColorChart)));
        mLineDataSet.setColor(ContextCompat.getColor(mContext, mColorChart));
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
            return mValueLineCharts.get((int) value).getLabel();
        }
    }
}
