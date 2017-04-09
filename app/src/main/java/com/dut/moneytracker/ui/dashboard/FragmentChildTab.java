package com.dut.moneytracker.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.ExchangeRecyclerViewTabAdapter;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.currency.CurrencyUtils;
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
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import io.realm.RealmResults;


/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
@EFragment(R.layout.fragment_tab_account)
public class FragmentChildTab extends BaseFragment implements TabAccountListener {
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
    private Handler mHandler;
    private int positionItem;
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
    void init() {
        mHandler = new Handler();
        mCardView.setVisibility(View.GONE);
        onShowAmount();
        onLoadExchanges();
        onLoadChart();
    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(mBroadcastReceiver, new IntentFilter(getString(R.string.action_reload_tab_account)));
    }

    @Override
    public void onLoadChart() {
        mLineChartMoney = new LineChartMoney(getContext(), mLineChart);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mValueLineCharts = ExchangeManger.getInstance().getValueChartByDailyDay(mAccount.getId(), 30);
                mLineChartMoney.setColorChart(mAccount.getColorHex());
                mLineChartMoney.updateNewValueLineChart(mValueLineCharts);
                mLineChartMoney.notifyDataSetChanged();
            }
        }, FragmentDashboard.DELAY);
    }

    @Override
    public void onLoadExchanges() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final RealmResults<Exchange> exchanges = ExchangeManger.getInstance().getExchangesLimitByAccount(mAccount.getId(), FragmentDashboard.LIMIT_ITEM);
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
    public void onShowDetailExchange(Exchange exchange) {
        ActivityDetailExchange_.intent(FragmentChildTab.this).mExchange(exchange).startForResult(RequestCode.DETAIL_EXCHANGE);
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

    @Override
    public void onShowAmount() {
        mTvAmount.setTextColor(Color.parseColor(mAccount.getColorHex()));
        String money = CurrencyUtils.getInstance().getStringMoneyFormat(AccountManager.getInstance().getAmountAvailableByAccount(mAccount.getId()), mAccount.getCurrencyCode());
        mTvAmount.setText(money);
    }


    @Click(R.id.tvMoreExchange)
    void onClickMoreExchange() {
        ((MainActivity) getActivity()).onLoadFragmentExchangesByAccount(mAccount.getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        getContext().unregisterReceiver(mBroadcastReceiver);
    }

    public void onReloadData() {
        reloadChartExchange();
        reloadTvAmount();
    }

    private void reloadTvAmount() {
        String money = CurrencyUtils.getInstance().getStringMoneyFormat(AccountManager.getInstance().getTotalAmountAvailable(),
                CurrencyManager.getInstance().getCurrentCodeCurrencyDefault());
        mTvAmount.setText(money);
    }

    private void reloadChartExchange() {
      //  onLoadChart();
    }
}
