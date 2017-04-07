package com.dut.moneytracker.ui.dashboard;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.ExchangeRecyclerViewTabAdapter;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.AppPreferences;
import com.dut.moneytracker.models.charts.LineChartMoney;
import com.dut.moneytracker.models.charts.ValueLineChart;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.ui.MainActivity;
import com.dut.moneytracker.ui.base.BaseFragment;
import com.dut.moneytracker.ui.exchanges.ActivityDetailExchange_;
import com.github.mikephil.charting.charts.LineChart;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.List;


/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
@EFragment(R.layout.fragment_tab_account)
public class FragmentChildTab extends BaseFragment implements TabAccountListener {
    private static final String TAG = FragmentChildTab.class.getSimpleName();
    //View
    @ViewById(R.id.recyclerExchange)
    RecyclerView mRecyclerExchange;
    @ViewById(R.id.tvAmount)
    TextView mTvAmount;
    @ViewById(R.id.cardViewCardAccount)
    CardView mCardView;
    @ViewById(R.id.pieChart)
    LineChart mLineChart;
    @FragmentArg
    Account mAccount;
    private ExchangeRecyclerViewTabAdapter mExchangeAdapter;
    private List<ValueLineChart> mValueLineCharts;
    private LineChartMoney mLineChartMoney;
    NotificationListener notificationListener;

    public void registerNotification(NotificationListener notificationListner) {
        this.notificationListener = notificationListner;
    }

    @AfterViews
    void init() {
        mCardView.setVisibility(View.GONE);
        mLineChartMoney = new LineChartMoney(getContext(), mLineChart, mAccount.getColorCode());
        onShowAmount();
        onLoadExchanges();
        onLoadChart();
    }

    @Override
    public void onLoadChart() {
        mValueLineCharts = ExchangeManger.getInstance().getValueChartByDailyDay(mAccount.getId(), 30);
        mLineChartMoney.updateNewValueLineChart(mValueLineCharts);
        mLineChartMoney.notifyDataSetChanged();
    }

    @Override
    public void onLoadExchanges() {
        final int limit = AppPreferences.getInstance().getLimitViewExchange(getContext());
        final List<Exchange> exchanges = ExchangeManger.getInstance().getExchangesLimitByAccount(mAccount.getId(), limit);
        mExchangeAdapter = new ExchangeRecyclerViewTabAdapter(getContext(), exchanges);
        mRecyclerExchange.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerExchange.setNestedScrollingEnabled(false);
        mRecyclerExchange.setAdapter(mExchangeAdapter);
        mRecyclerExchange.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), new ClickItemListener() {
            @Override
            public void onClick(View view, int position) {
                onShowDetailExchange(exchanges.get(position));
            }
        }));
    }

    @Override
    public void onShowDetailExchange(Exchange exchange) {
        ActivityDetailExchange_.intent(FragmentChildTab.this).mExchange(exchange).startForResult(ResultCode.DETAIL_EXCHANGE);
    }

    @OnActivityResult(ResultCode.DETAIL_EXCHANGE)
    void onResult(int resultCode) {
     /*   if (resultCode == ResultCode.DELETE_EXCHANGE) {
            final int limit = AppPreferences.getInstance().getLimitViewExchange(getContext());
            final List<Exchange> exchanges = ExchangeManger.getInstance().getExchangesLimitByAccount(mAccount.getId(), limit);
            mExchangeAdapter.setObjects(exchanges);
            mExchangeAdapter.notifyDataSetChanged();
        } else {
            mExchangeAdapter.notifyDataSetChanged();
        }*/
        notificationListener.onNotification();
    }

    @Override
    public void onShowAmount() {
        mTvAmount.setTextColor(ContextCompat.getColor(getContext(), mAccount.getColorCode()));
        String money = CurrencyUtils.getInstance().getStringMoneyType(AccountManager.getInstance().getAmountAvailableByAccount(mAccount.getId()), mAccount.getCurrencyCode());
        mTvAmount.setText(money);
    }


    @Click(R.id.tvMoreExchange)
    void onClickMoreExchange() {
        ((MainActivity) getActivity()).onLoadFragmentExchangesByAccount(mAccount.getId());
    }
}
