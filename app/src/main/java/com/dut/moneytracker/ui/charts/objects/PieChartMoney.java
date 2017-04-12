package com.dut.moneytracker.ui.charts.objects;

import android.content.Context;
import android.graphics.Color;

import com.dut.moneytracker.currency.CurrencyUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 05/04/2017.
 */

public class PieChartMoney implements OnChartValueSelectedListener {
    //Data
    private Context mContext;
    private List<ValuePieChart> mValuePieCharts = new ArrayList<>();
    private String mTotal = "0";
    //Chart
    private PieChart mChart;
    private List<PieEntry> mPieEntries = new ArrayList<>();
    private PieDataSet mPieDataSet;
    private PieData mPieData;

    public PieChartMoney(Context context, PieChart chart) {
        mContext = context;
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
        mChart.setCenterTextSize(13);
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);
        mPieDataSet = new PieDataSet(mPieEntries, null);
        mPieDataSet.setLabel(null);
        mPieDataSet.setSliceSpace(1);
        mPieDataSet.setValueLinePart1OffsetPercentage(80.f);
        mPieDataSet.setValueLinePart1Length(0.2f);
        mPieDataSet.setValueLinePart2Length(0.4f);
        mPieData = new PieData(mPieDataSet);
        mPieData.setValueFormatter(new PercentFormatter());
        mPieData.setValueTextSize(12f);
        mPieData.setValueTextColor(Color.WHITE);
        mChart.setData(mPieData);
    }

    private int[] getListColor() {
        int size = mValuePieCharts.size();
        int[] colors = new int[size];
        for (int i = 0; i < size; i++) {
            colors[i] = mValuePieCharts.get(i).getColorGroup();
        }
        return colors;
    }

    public void updateValuePieChart(List<ValuePieChart> valuePieCharts) {
        mValuePieCharts.clear();
        mValuePieCharts.addAll(valuePieCharts);
        for (ValuePieChart valuePieChart : mValuePieCharts) {
            PieEntry pieEntry = new PieEntry(valuePieChart.getAmount(), valuePieChart.getNameGroup());
            mPieEntries.add(pieEntry);
        }
        mPieDataSet.setColors(getListColor(), mContext);
    }

    public void updateTotal(String amount) {
        mTotal = amount;
        String title = String.format(Locale.US, "Tổng\n%s", CurrencyUtils.getInstance().getStringMoneyFormat(mTotal, "VND"));
        mChart.setCenterText(title);
    }

    public void notifyDataSetChanged() {
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        int index = (int) h.getX();
        String title = String.format(Locale.US, "%s\n%s", mValuePieCharts.get(index).getNameGroup(), CurrencyUtils.getInstance().getStringMoneyFormat(mValuePieCharts.get(index).getAmountString(), "VND"));
        mChart.setCenterText(title);
    }

    @Override
    public void onNothingSelected() {
        mChart.setCenterText("Tổng\n" + CurrencyUtils.getInstance().getStringMoneyFormat(mTotal, "VND"));
    }
}
