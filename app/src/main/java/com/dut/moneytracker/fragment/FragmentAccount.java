package com.dut.moneytracker.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.ActivityDetailExchange;
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
public class FragmentAccount extends BaseFragment implements TabAccountListener {
    private static final String TAG = FragmentAccount.class.getSimpleName();
    //View
    private RecyclerView mRecyclerExchange;
    private TextView mTvAmount;
    private TextView tvMoreExchange;
    private LineChart mLineChart;
    //Model
    private ExchangeAccountAdapter mExchangeAccountAdapter;
    private Account mAccount;
    private AccountPresenter mPresenter = AccountPresenter.getInstance();
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccount = getArguments().getParcelable(getString(R.string.extra_account));
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
        onLoadExchanges();
        onLoadChart();
    }

    private void initView(View view) {
        CardView mCardViewCardAccount = (CardView) view.findViewById(R.id.cardViewCardAccount);
        mCardViewCardAccount.setVisibility(View.GONE);
        mRecyclerExchange = (RecyclerView) view.findViewById(R.id.recyclerExchange);
        mTvAmount = (TextView) view.findViewById(R.id.tvAmount);
        tvMoreExchange = (TextView) view.findViewById(R.id.tvMoreExchange);
        mLineChart = (LineChart) view.findViewById(lineChart);
    }

    @Override
    public void onLoadChart() {
        final List<ValueChartAmount> valueChartAmounts = ExchangeManger.getInstance().getValueChartByDailyDay(mAccount.getId(), 30);
        for (ValueChartAmount valueChartAmount : valueChartAmounts) {
            Log.d(TAG, "onLoadChartSingle: " + valueChartAmount.toString());
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                LineChartAmount lineChartAmount = new LineChartAmount.Builder(mLineChart)
                        .setValueChartAmounts(valueChartAmounts)
                        .setLabel(getString(R.string.chart_title))
                        .setColorLine(mAccount.getColorCode())
                        .build();
                lineChartAmount.onDraw();
            }
        });
    }

    @Override
    public void onLoadExchanges() {
        int limit = AppPreferences.getInstance().getLimitViewExchange(getContext());
        final List<Exchange> exchanges = ExchangeManger.getInstance().getExchangesByAccount(mAccount.getId(), limit);
        mExchangeAccountAdapter = new ExchangeAccountAdapter(getContext(), exchanges);
        mRecyclerExchange.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerExchange.setNestedScrollingEnabled(false);
        mRecyclerExchange.setAdapter(mExchangeAccountAdapter);
        mRecyclerExchange.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), new ClickItemRecyclerListener() {
            @Override
            public void onClick(View view, int position) {
                onShowDetailExchange(exchanges.get(position));
            }
        }));
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
                mExchangeAccountAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onLoadAmount() {
        mTvAmount.setTextColor(Color.parseColor(mAccount.getColorCode()));
        String money = CurrencyUtils.getInstance().getStringMoneyType(mPresenter.getTotalAmountAvailable(mAccount.getId()), mAccount.getCurrencyCode());
        mTvAmount.setText(money);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
