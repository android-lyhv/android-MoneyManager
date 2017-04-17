package com.dut.moneytracker.ui.charts.category;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.chart.RecyclerCategoryChartAdapter;
import com.dut.moneytracker.constant.FilterType;
import com.dut.moneytracker.models.FilterManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.ui.base.BaseFragment;
import com.dut.moneytracker.ui.charts.objects.ValueCategoryChart;
import com.dut.moneytracker.ui.dashboard.FragmentDashboard;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 10/04/2017.
 */
@EFragment(R.layout.fragment_chart_category)
public class FragmentChartCategory extends BaseFragment {
    @ViewById(R.id.llPrevious)
    LinearLayout llPrevious;
    @ViewById(R.id.llNext)
    LinearLayout llNext;
    @ViewById(R.id.tvInformationExchange)
    TextView tvInformationExchange;
    @FragmentArg
    Filter mFilter;
    @ViewById(R.id.recyclerChartCategory)
    RecyclerView mRecyclerView;
    private RecyclerCategoryChartAdapter mAdapter;
    private List<ValueCategoryChart> mValueCategoryCharts;
    private Handler mHandler;

    @AfterViews
    void init() {
        mHandler = new Handler();
        changeDateLabel();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerCategoryChartAdapter(getContext(), mValueCategoryCharts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mValueCategoryCharts = ExchangeManger.getInstance().getValueCategoryCharts(mFilter);
                ExchangeManger.getInstance().sortByAmountExchange(mValueCategoryCharts);
                mAdapter.addAll(mValueCategoryCharts);
                mAdapter.notifyDataSetChanged();
            }
        }, FragmentDashboard.DELAY);
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
        if (mFilter.getTypeFilter() == FilterType.CUSTOM || mFilter.getTypeFilter() == FilterType.ALL) {
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
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
