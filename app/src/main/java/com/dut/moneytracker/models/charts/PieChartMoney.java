package com.dut.moneytracker.models.charts;

import android.graphics.Color;
import android.util.Log;

import com.dut.moneytracker.currency.CurrencyUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 05/04/2017.
 */

public class PieChartMoney implements OnChartValueSelectedListener {
    private static final String TAG = PieChartMoney.class.getSimpleName();
    //Data
    private List<ValuePieChart> mValuePieCharts;
    private String mTotal = "0";
    //Chart
    private PieChart mChart;
    private List<PieEntry> mPieEntries = new ArrayList<>();
    private PieDataSet mPieDataSet;
    private PieData mPieData;

    public PieChartMoney(PieChart chart) {
        mChart = chart;
        configChart();
    }

    private void configChart() {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setDrawEntryLabels(false);
        mChart.getLegend().setWordWrapEnabled(true);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);
        mPieDataSet = new PieDataSet(mPieEntries, null);
        mPieDataSet.setLabel(null);
        mPieDataSet.setSliceSpace(1);
        mPieDataSet.setColors(getListColor());
        mPieDataSet.setValueLinePart1OffsetPercentage(80.f);
        mPieDataSet.setValueLinePart1Length(0.2f);
        mPieDataSet.setValueLinePart2Length(0.4f);
        mPieData = new PieData(mPieDataSet);
        mPieData.setValueFormatter(new PercentFormatter());
        mPieData.setValueTextSize(11f);
        mPieData.setValueTextColor(Color.WHITE);
        mChart.setData(mPieData);
    }

    private List<Integer> getListColor() {
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        return colors;
    }

    public void updateValuePieChart(List<ValuePieChart> valuePieCharts) {
        if (mValuePieCharts == null) {
            mValuePieCharts = new ArrayList<>();
        }
        mValuePieCharts.clear();
        mValuePieCharts.addAll(valuePieCharts);
        for (ValuePieChart valuePieChart : mValuePieCharts) {
            PieEntry pieEntry = new PieEntry(valuePieChart.getAmount(), valuePieChart.getNameGroup());
            mPieEntries.add(pieEntry);
        }
    }

    public void updateTotal(String amount) {
        mTotal = amount;
        mChart.setCenterText("Tổng\n" + CurrencyUtils.getInstance().getStringMoneyType(mTotal, "VND"));
    }

    public void notifyDataSetChanged() {
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d(TAG, "onValueSelected: ");
    }

    @Override
    public void onNothingSelected() {
        Log.d(TAG, "onNothingSelected: ");
    }
}
