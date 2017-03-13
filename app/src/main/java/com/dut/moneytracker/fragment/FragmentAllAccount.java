package com.dut.moneytracker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.ActivityDetailExchange;
import com.dut.moneytracker.adapter.CardAccountAdapter;
import com.dut.moneytracker.adapter.ClickItemRecyclerListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.ExchangeAccountAdapter;
import com.dut.moneytracker.charts.LineChartAmount;
import com.dut.moneytracker.charts.ValueChartAmount;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.AppPreferences;
import com.dut.moneytracker.models.presenter.AccountPresenter;
import com.dut.moneytracker.models.realms.CurrencyManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

import static com.dut.moneytracker.R.id.lineChart;


/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
public class FragmentAllAccount extends BaseFragment implements TabAccountListener, View.OnClickListener {
    private static final String TAG = FragmentAllAccount.class.getSimpleName();

    public interface CardAccountListener {
        void onClickCardAccount(int position);
    }

    CardAccountListener cardAccountListener;

    public void registerCardAccountListener(CardAccountListener cardAccountListener) {
        this.cardAccountListener = cardAccountListener;
    }

    //View
    private CardView mCardViewCardAccount;
    private RecyclerView mRecyclerViewCardAccount;
    private RecyclerView mRecyclerExchange;
    private TextView mTvAmount;
    private TextView tvMoreExchange;
    private CardAccountAdapter mCardAccountAdapter;
    private ExchangeAccountAdapter mExchangeAccountAdapter;
    private LineChart mLineChart;
    //Model
    private List<Account> accounts;
    private AccountPresenter mPresenter = AccountPresenter.getInstance();
    private Handler mHandle = new Handler();
    //Call Back main Activity

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // accounts = getArguments().getParcelableArrayList(getString(R.string.extra_account_list));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onLoadAmount();
        onLoadViewCardAccount();
        onLoadExchanges();
        onLoadChart();
    }

    private void initView(View view) {
        mCardViewCardAccount = (CardView) view.findViewById(R.id.cardViewCardAccount);
        mRecyclerViewCardAccount = (RecyclerView) view.findViewById(R.id.recyclerViewCardAccount);
        mRecyclerExchange = (RecyclerView) view.findViewById(R.id.recyclerExchange);
        mTvAmount = (TextView) view.findViewById(R.id.tvAmount);
        tvMoreExchange = (TextView) view.findViewById(R.id.tvMoreExchange);
        tvMoreExchange.setOnClickListener(this);
        mLineChart = (LineChart) view.findViewById(lineChart);
    }


    private void onLoadViewCardAccount() {
        mCardAccountAdapter = new CardAccountAdapter(getContext(), accounts);
        mRecyclerViewCardAccount.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerViewCardAccount.setNestedScrollingEnabled(false);
        mRecyclerViewCardAccount.setAdapter(mCardAccountAdapter);
        mRecyclerViewCardAccount.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), new ClickItemRecyclerListener() {
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
        final List<Exchange> exchanges = ExchangeManger.getInstance().getExchanges(limit);
        mExchangeAccountAdapter = new ExchangeAccountAdapter(getContext(), exchanges);
        mRecyclerExchange.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerExchange.setNestedScrollingEnabled(false);
        mRecyclerExchange.setAdapter(mExchangeAccountAdapter);
        mRecyclerExchange.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), new ClickItemRecyclerListener() {
            @Override
            public void onClick(View view, int position) {
                onShowDetailExchange(exchanges.get(position));
                Log.d(TAG, "onClick: " + exchanges.get(position).getAmount());
            }
        }));
    }

    @Override
    public void onLoadAmount() {
        mTvAmount.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        String money = CurrencyUtils.getInstance().getStringMoneyType(mPresenter.getTotalAmountAvailable(),
                CurrencyManager.getInstance().getCurrentCodeCurrencyDefault());
        mTvAmount.setText(money);
    }

    @Override
    public void onShowDetailExchange(Exchange exchange) {
        Intent intent = new Intent(getActivity(), ActivityDetailExchange.class);
        intent.putExtra(getString(R.string.extra_account), exchange);
        startActivityForResult(intent, RequestCode.DETAIL_EXCHANGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ResultCode.DETAIL_EXCHANGE:
                mCardAccountAdapter.notifyDataSetChanged();
                mExchangeAccountAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandle.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {

    }
}
