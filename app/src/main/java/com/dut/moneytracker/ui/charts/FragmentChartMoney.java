package com.dut.moneytracker.ui.charts;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.FilterType;
import com.dut.moneytracker.constant.PieChartType;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.FilterManager;
import com.dut.moneytracker.models.charts.MoneyChartManager;
import com.dut.moneytracker.models.charts.PieChartMoney;
import com.dut.moneytracker.models.charts.ValuePieChart;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.ui.base.BaseFragment;
import com.github.mikephil.charting.charts.PieChart;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 05/04/2017.
 */
@EFragment(R.layout.fragment_card_chart_exchange)
public class FragmentChartMoney extends BaseFragment implements ChartMoneyListener {
    private static final String TAG = FragmentChartMoney.class.getSimpleName();
    @ViewById(R.id.llPrevious)
    LinearLayout llPrevious;
    @ViewById(R.id.llNext)
    LinearLayout llNext;
    @ViewById(R.id.tvInformationExchange)
    TextView tvInformationExchange;
    @ViewById(R.id.tvMinMoney)
    TextView mTvMinMoney;
    @ViewById(R.id.tvAverageMoney)
    TextView mTvAverageMoney;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;
    @ViewById(R.id.tvMaxMoney)
    TextView mTvMaxMoney;
    @ViewById(R.id.pieChart)
    PieChart mPieChart;
    @FragmentArg
    Filter mFilter;
    @FragmentArg
    int mChartType;
    private List<ValuePieChart> mValuePieCharts;
    private PieChartMoney mPieChartMoney;

    @AfterViews
    void init() {
        switch (mChartType) {
            case PieChartType.INCOME:
                mTvTitle.setText(getString(R.string.income_name));
                break;
            case PieChartType.EXPENSES:
                mTvTitle.setText(getString(R.string.expense_name));
                break;
        }
        changeDateLabel();
        initChart();
    }

    void initChart() {
        mPieChartMoney = new PieChartMoney(getContext(), mPieChart);
        mValuePieCharts = ExchangeManger.getInstance().getFilterValuePieCharts(mFilter, mChartType);
        MoneyChartManager.getInstance().onLoadInforPieChart(mValuePieCharts, this);
        mPieChartMoney.updateValuePieChart(mValuePieCharts);
        mPieChartMoney.notifyDataSetChanged();
    }


    @Click(R.id.llNext)
    void onClickNext() {
        sendBroadcastFilter(1);
    }

    @Click(R.id.llPrevious)
    void onClickPrev() {
        sendBroadcastFilter(-1);
    }


    public void changeDateLabel() {
        String label = FilterManager.getInstance().getLabel(mFilter, null);
        if (mFilter.getViewType() == FilterType.CUSTOM || mFilter.getViewType() == FilterType.ALL) {
            llNext.setVisibility(View.GONE);
            llPrevious.setVisibility(View.GONE);
        } else {
            llNext.setVisibility(View.VISIBLE);
            llPrevious.setVisibility(View.VISIBLE);
        }
        tvInformationExchange.setText(label);
    }

    private void sendBroadcastFilter(int step) {
        Intent intent = new Intent(getContext().getString(R.string.broadcast_filter));
        intent.putExtra(getContext().getString(R.string.step_filter), step);
        getContext().sendBroadcast(intent);
    }

    @Override
    public void onResultMultiMoney(String minMoney, String averageMoney, String maxMoney) {
        mTvMinMoney.setText(CurrencyUtils.getInstance().getStringMoneyFormat(minMoney, "VND"));
        mTvAverageMoney.setText(CurrencyUtils.getInstance().getStringMoneyFormat(averageMoney, "VND"));
        mTvMaxMoney.setText(CurrencyUtils.getInstance().getStringMoneyFormat(maxMoney, "VND"));
    }

    @Override
    public void onResultTotal(String money) {
        mPieChartMoney.updateTotal(money);
        mPieChartMoney.notifyDataSetChanged();
    }
}
