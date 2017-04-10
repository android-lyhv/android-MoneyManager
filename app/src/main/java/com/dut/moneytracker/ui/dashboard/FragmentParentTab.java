package com.dut.moneytracker.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.dut.moneytracker.ui.charts.objects.LineChartMoney;
import com.dut.moneytracker.ui.charts.objects.ValueLineChart;
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

import io.realm.RealmResults;


/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
@EFragment(R.layout.fragment_tab_account)
public class FragmentParentTab extends BaseFragment implements TabAccountListener {
    private int positionItem;

    public interface CardAccountListener {
        void onClickCardAccount(int position);
    }

    CardAccountListener cardAccountListener;

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
    CardAccountAdapter mCardAccountAdapter;
    private ExchangeRecyclerViewTabAdapter mExchangeAdapter;
    private List<ValueLineChart> mValueLineCharts;
    private LineChartMoney mLineChartMoney;
    private Handler mHandler;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            if (TextUtils.equals(intent.getAction(), context.getString(R.string.action_reload_tab_account))) {
                onReloadData();
            }
        }
    };

    @AfterViews
    public void init() {
        mHandler = new Handler();
        mLineChartMoney = new LineChartMoney(getContext(), mLineChart);
        onShowAmount();
        onLoadCardAccount();
        onLoadExchanges();
        onLoadChart();
    }

    private void onLoadCardAccount() {
        RealmResults<Account> mAccounts = AccountManager.getInstance().getAccounts();
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
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reloadChartExchange();
            }
        }, FragmentDashboard.DELAY);
    }

    @Override
    public void onLoadExchanges() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final RealmResults<Exchange> exchanges = ExchangeManger.getInstance().getExchangesLimit(FragmentDashboard.LIMIT_ITEM);
                mExchangeAdapter = new ExchangeRecyclerViewTabAdapter(getContext(), exchanges);
                mRecyclerExchange.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerExchange.setNestedScrollingEnabled(false);
                mRecyclerExchange.setAdapter(mExchangeAdapter);
                mRecyclerExchange.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), new ClickItemListener() {
                    @Override
                    public void onClick(View view, int position) {
                        positionItem = position;
                        onShowDetailExchange(exchanges.get(position));
                    }
                }));
            }
        }, FragmentDashboard.DELAY);
    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(mBroadcastReceiver, new IntentFilter(getString(R.string.action_reload_tab_account)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        getContext().unregisterReceiver(mBroadcastReceiver);
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
        getContext().sendBroadcast(new Intent(getString(R.string.action_reload_tab_account)));
    }

    @Click(R.id.tvMoreExchange)
    void onClickMoreExchange() {
        ((MainActivity) getActivity()).onLoadFragmentExchanges();
    }

    public void onReloadData() {
        onLoadChart();
        onShowAmount();
        reloadCardAccount();
    }

    private void reloadCardAccount() {
        RealmResults<Account> mAccounts = AccountManager.getInstance().getAccounts();
        mCardAccountAdapter.setAccounts(mAccounts);
        mCardAccountAdapter.notifyDataSetChanged();
    }

    private void reloadChartExchange() {
        mValueLineCharts = ExchangeManger.getInstance().getValueChartByDailyDay(30);
        mLineChartMoney.setColorChart(getString(R.string.color_account_default));
        mLineChartMoney.updateNewValueLineChart(mValueLineCharts);
        mLineChartMoney.notifyDataSetChanged();
    }
}
