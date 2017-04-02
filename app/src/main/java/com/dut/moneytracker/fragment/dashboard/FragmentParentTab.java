package com.dut.moneytracker.fragment.dashboard;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.ActivityDetailExchange_;
import com.dut.moneytracker.activities.MainActivity;
import com.dut.moneytracker.adapter.CardAccountAdapter;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.ExchangeRecyclerViewTabAdapter;
import com.dut.moneytracker.charts.LineChartAmount;
import com.dut.moneytracker.charts.ValueChartAmount;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.fragment.base.BaseFragment;
import com.dut.moneytracker.models.AppPreferences;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.CurrencyManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;
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

    public void registerCardAccountListener(CardAccountListener cardAccountListener) {
        this.cardAccountListener = cardAccountListener;
    }

    //View
    @ViewById(R.id.recyclerExchange)
    RecyclerView mRecyclerExchange;
    @ViewById(R.id.tvAmount)
    TextView mTvAmount;
    @ViewById(R.id.linchartAmount)
    LineChart mLineChart;
    @ViewById(R.id.recyclerViewCardAccount)
    RecyclerView mRecyclerViewCardAccount;
    private List<Account> mAccounts;
    private CardAccountAdapter mCardAccountAdapter;
    private ExchangeRecyclerViewTabAdapter mExchangeAdapter;

    @AfterViews
    public void init() {
        onShowAmount();
        onLoadCardAccount();
        onLoadExchanges();
        onLoadChart();
    }

    private void onLoadCardAccount() {
        mAccounts = AccountManager.getInstance().getListAccount();
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
        final List<ValueChartAmount> valueChartAmounts = ExchangeManger.getInstance().getValueChartByDailyDay(30);
        LineChartAmount lineChartAmount = new LineChartAmount.Builder(mLineChart)
                .setValueChartAmounts(valueChartAmounts)
                .setLabel(getString(R.string.chart_title))
                .build();
        lineChartAmount.onDraw();
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
        mTvAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        String money = CurrencyUtils.getInstance().getStringMoneyType(AccountManager.getInstance().getAllAmountAvailable(),
                CurrencyManager.getInstance().getCurrentCodeCurrencyDefault());
        mTvAmount.setText(money);
    }

    @Override
    public void onShowDetailExchange(Exchange exchange) {
        ActivityDetailExchange_.intent(FragmentParentTab.this).mExchange(exchange).startForResult(ResultCode.DETAIL_EXCHANGE);
    }

    @OnActivityResult(ResultCode.DETAIL_EXCHANGE)
    void onResult() {
        mCardAccountAdapter.notifyDataSetChanged();
        mExchangeAdapter.notifyDataSetChanged();
    }

    @Click(R.id.tvMoreExchange)
    void onClickMoreExchange() {
        ((MainActivity) getActivity()).onLoadFragmentAllExchanges();
    }
}
