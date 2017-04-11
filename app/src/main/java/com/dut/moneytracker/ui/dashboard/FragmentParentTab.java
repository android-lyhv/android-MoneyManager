package com.dut.moneytracker.ui.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.ExchangeRecyclerViewTabAdapter;
import com.dut.moneytracker.adapter.account.CardAccountAdapter;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.ui.MainActivity;
import com.dut.moneytracker.ui.base.BaseFragment;
import com.dut.moneytracker.ui.charts.objects.LineChartMoney;
import com.dut.moneytracker.ui.charts.objects.ValueLineChart;
import com.dut.moneytracker.ui.exchanges.ActivityDetailExchange_;
import com.github.mikephil.charting.charts.LineChart;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
@EFragment(R.layout.fragment_tab_account)
public class FragmentParentTab extends BaseFragment implements TabAccountListener, RealmChangeListener<RealmResults<Account>> {
    public FragmentParentTab() {
    }

    public interface CardAccountListener {

        void onClickCardAccount(int position);
    }

    CardAccountListener cardAccountListener;

    public void registerCardAccountListener(CardAccountListener cardAccountListener) {
        this.cardAccountListener = cardAccountListener;
    }

    private static String BASE_COLOR = "#03a879";

    private int positionItem;

    //View
    @ViewById(R.id.recyclerExchange)
    RecyclerView mRecyclerExchange;

    @ViewById(R.id.tvAmount)
    TextView mTvAmount;
    @ViewById(R.id.pieChart)
    LineChart mLineChart;
    @ViewById(R.id.recyclerViewCardAccount)
    RecyclerView mRecyclerViewCardAccount;
    private CardAccountAdapter mCardAccountAdapter;
    private ExchangeRecyclerViewTabAdapter mExchangeAdapter;
    private LineChartMoney mLineChartMoney;
    private Handler mHandler;
    private RealmResults<Exchange> mExchanges;
    private RealmResults<Account> mAccounts;

    @AfterViews
    public void init() {
        mHandler = new Handler();
        mLineChartMoney = new LineChartMoney(getContext(), mLineChart);
        onLoadCardAccount();
        onLoadChart();
        onShowAmount();
        onLoadExchanges();
    }


    private void onLoadCardAccount() {
        mAccounts = AccountManager.getInstance().loadAccountsAsync();
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
        mAccounts.addChangeListener(this);
    }

    @Override
    public void onChange(RealmResults<Account> element) {
        if (mCardAccountAdapter == null) {
            return;
        }
        mCardAccountAdapter.notifyDataSetChanged();
    }

    @Override
    public void onShowAmount() {
        mTvAmount.setTextColor(Color.parseColor(BASE_COLOR));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String money = CurrencyUtils.getInstance().getStringMoneyFormat(AccountManager.getInstance().getTotalAmountAvailable(),
                        CurrencyUtils.DEFAULT_CURRENCY_CODE);
                mTvAmount.setText(money);
            }
        }, FragmentDashboard.DELAY);
    }

    @Override
    public void onLoadChart() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reloadChartExchange();
            }
        }, FragmentDashboard.DELAY);
    }

    @Override
    public void onLoadExchanges() {
        mExchanges = ExchangeManger.getInstance().onLoadExchangeAsync(FragmentDashboard.LIMIT_ITEM);
        mExchangeAdapter = new ExchangeRecyclerViewTabAdapter(getContext(), mExchanges);
        mRecyclerExchange.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerExchange.setNestedScrollingEnabled(false);
        mRecyclerExchange.setAdapter(mExchangeAdapter);
        mRecyclerExchange.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), new ClickItemListener() {
            @Override
            public void onClick(View view, int position) {
                positionItem = position;
                onShowDetailExchange(mExchanges.get(position));
            }
        }));
        mExchanges.addChangeListener(new RealmChangeListener<RealmResults<Exchange>>() {
            @Override
            public void onChange(RealmResults<Exchange> element) {
                mExchangeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onShowDetailExchange(Exchange exchange) {
        ActivityDetailExchange_.intent(FragmentParentTab.this).mExchange(exchange).startForResult(RequestCode.DETAIL_EXCHANGE);
    }

    @OnActivityResult(RequestCode.DETAIL_EXCHANGE)
    void onResult(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Exchange exchange = data.getParcelableExtra(getString(R.string.extra_edit_exchange));
        switch (resultCode) {
            case ResultCode.EDIT_EXCHANGE:
                ExchangeManger.getInstance().insertOrUpdate(exchange);
                break;
            case ResultCode.DELETE_EXCHANGE:
                ExchangeManger.getInstance().deleteExchangeById(((Exchange) mExchangeAdapter.getItem(positionItem)).getId());
        }
        onLoadCardAccount();
        onLoadChart();
        onShowAmount();
    }

    @Click(R.id.tvMoreExchange)
    void onClickMoreExchange() {
        ((MainActivity) getActivity()).onLoadFragmentExchanges();
    }

    private void reloadChartExchange() {
        List<ValueLineChart> mValueLineCharts = ExchangeManger.getInstance().getValueChartByDailyDay(30);
        mLineChartMoney.setColorChart(BASE_COLOR);
        mLineChartMoney.updateNewValueLineChart(mValueLineCharts);
        mLineChartMoney.notifyDataSetChanged();
    }
}
