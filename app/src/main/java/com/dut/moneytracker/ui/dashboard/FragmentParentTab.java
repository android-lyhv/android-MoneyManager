package com.dut.moneytracker.ui.dashboard;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.account.CardAccountAdapter;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.ExchangeRecyclerViewTabAdapter;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.AppPreferences;
import com.dut.moneytracker.models.charts.LineChartMoney;
import com.dut.moneytracker.models.charts.ValueLineChart;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.CurrencyManager;
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
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.List;


/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
@EFragment(R.layout.fragment_tab_account)
public class FragmentParentTab extends BaseFragment implements TabAccountListener {
    private static final String TAG = FragmentParentTab.class.getSimpleName();

    public interface CardAccountListener {
        void onClickCardAccount(int position);
    }

    CardAccountListener cardAccountListener;
    NotificationListener notificationListener;

    public void registerNotification(NotificationListener notificationListner) {
        this.notificationListener = notificationListner;
    }

    public void registerCardAccountListener(CardAccountListener cardAccountListener) {
        this.cardAccountListener = cardAccountListener;
    }

    //View
    @ViewById(R.id.recyclerExchange)
    RecyclerView mRecyclerExchange;
    @ViewById(R.id.tvAmount)
    TextView mTvAmount;
    @ViewById(R.id.pieChart)
    LineChart mLineChart;
    @ViewById(R.id.recyclerViewCardAccount)
    RecyclerView mRecyclerViewCardAccount;
    private List<Account> mAccounts;
    private CardAccountAdapter mCardAccountAdapter;
    private ExchangeRecyclerViewTabAdapter mExchangeAdapter;
    private List<ValueLineChart> mValueLineCharts;
    private LineChartMoney mLineChartMoney;

    @AfterViews
    public void init() {
        mLineChartMoney = new LineChartMoney(getContext(), mLineChart);
        onShowAmount();
        onLoadCardAccount();
        onLoadExchanges();
        onLoadChart();
    }

    private void onLoadCardAccount() {
        mAccounts = AccountManager.getInstance().getAccounts();
        mCardAccountAdapter = new CardAccountAdapter(getContext(), mAccounts);
        mRecyclerViewCardAccount.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerViewCardAccount.setNestedScrollingEnabled(false);
        mRecyclerViewCardAccount.setAdapter(mCardAccountAdapter);
        mRecyclerViewCardAccount.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), new ClickItemListener() {
            @Override
            public void onClick(View view, int position) {
                cardAccountListener.onClickCardAccount(position + 1);
            }
        }));
    }

    @Override
    public void onLoadChart() {
        mValueLineCharts = ExchangeManger.getInstance().getValueChartByDailyDay(30);
        mLineChartMoney.setColorChart(getString(R.string.color_account_default));
        mLineChartMoney.updateNewValueLineChart(mValueLineCharts);
        mLineChartMoney.notifyDataSetChanged();
    }

    @Override
    public void onLoadExchanges() {
        int limit = AppPreferences.getInstance().getLimitViewExchange(getContext());
        final List<Exchange> exchanges = ExchangeManger.getInstance().getExchangesLimit(limit);
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
    public void onShowAmount() {
        mTvAmount.setTextColor(Color.parseColor(getString(R.string.color_account_default)));
        String money = CurrencyUtils.getInstance().getStringMoneyFormat(AccountManager.getInstance().getTotalAmountAvailable(),
                CurrencyManager.getInstance().getCurrentCodeCurrencyDefault());
        mTvAmount.setText(money);
    }

    @Override
    public void onShowDetailExchange(Exchange exchange) {
        ActivityDetailExchange_.intent(FragmentParentTab.this).mExchange(exchange).startForResult(ResultCode.DETAIL_EXCHANGE);
    }

    @OnActivityResult(ResultCode.DETAIL_EXCHANGE)
    void onResult(int resultCode) {
       /* if (resultCode == ResultCode.DELETE_EXCHANGE) {
            int limit = AppPreferences.getInstance().getLimitViewExchange(getContext());
            final List<Exchange> exchanges = ExchangeManger.getInstance().getExchangesLimit(limit);
            mExchangeAdapter.setObjects(exchanges);
            mExchangeAdapter.notifyDataSetChanged();
        } else {
            mExchangeAdapter.notifyDataSetChanged();
        }*/
        notificationListener.onNotification();
    }


    @Click(R.id.tvMoreExchange)
    void onClickMoreExchange() {
        ((MainActivity) getActivity()).onLoadFragmentExchanges();
    }
}
