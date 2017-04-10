package com.dut.moneytracker.ui.charts.objects;

import com.dut.moneytracker.ui.charts.exchange.ChartMoneyListener;

import java.math.BigDecimal;
import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/04/2017.
 */

public class MoneyChartManager {
    private static MoneyChartManager moneyChartManager;

    public static MoneyChartManager getInstance() {
        if (moneyChartManager == null) {
            moneyChartManager = new MoneyChartManager();
        }
        return moneyChartManager;
    }

    public void onLoadInforPieChart(List<ValuePieChart> valuePieCharts, ChartMoneyListener chartMoneyListener) {
        chartMoneyListener.onResultTotal(getTotalMoney(valuePieCharts));
        chartMoneyListener.onResultMultiMoney(getMinMoney(valuePieCharts), geAverageMoney(valuePieCharts), getMaxMoney(valuePieCharts));
    }

    private String getTotalMoney(List<ValuePieChart> valuePieCharts) {
        BigDecimal bigDecimal = new BigDecimal("0");
        for (ValuePieChart valueLineChart : valuePieCharts) {
            bigDecimal = bigDecimal.add(new BigDecimal(valueLineChart.getAmountString()));
        }
        return bigDecimal.toString();
    }

    private String getMaxMoney(List<ValuePieChart> valueLineCharts) {
        BigDecimal maxValue = new BigDecimal("0");
        for (ValuePieChart valueLineChart : valueLineCharts) {
            BigDecimal temp = new BigDecimal(valueLineChart.getAmountString());
            if (maxValue.compareTo(temp) == -1) {
                maxValue = temp;
            }
        }
        return maxValue.toString();
    }

    private String getMinMoney(List<ValuePieChart> valuePieCharts) {
        if (valuePieCharts.size() == 0) {
            return "0";
        }
        BigDecimal minValue = new BigDecimal(valuePieCharts.get(0).getAmountString());
        for (ValuePieChart valueLineChart : valuePieCharts) {
            BigDecimal temp = new BigDecimal(valueLineChart.getAmountString());
            if (minValue.compareTo(temp) == 1) {
                minValue = temp;
            }
        }
        return minValue.toString();
    }

    private String geAverageMoney(List<ValuePieChart> valuePieCharts) {
        if (valuePieCharts.size() == 0) {
            return "0";
        }
        BigDecimal bigDecimalTotal = new BigDecimal(getTotalMoney(valuePieCharts));
        BigDecimal bigDecimalAverage = bigDecimalTotal.divide(new BigDecimal(valuePieCharts.size()), 2);
        return bigDecimalAverage.toString();
    }
}
