package com.dut.moneytracker.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ExchangeAccountAdapter;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.AppPreferences;
import com.dut.moneytracker.models.presenter.AccountPresenter;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;

import java.util.List;


/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
public class FragmentAccount extends BaseFragment implements TabAccountListener {
    private static final String TAG = FragmentAccount.class.getSimpleName();
    //View
    private CardView mCardViewCardAccount;
    private RecyclerView mRecyclerExchange;
    private TextView mTvAmount;
    private TextView tvMoreExchange;
    //Model
    private ExchangeAccountAdapter mExchangeAccountAdapter;
    private Account account;
    private AccountPresenter mPresenter = AccountPresenter.getInstance();

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        mCardViewCardAccount.setVisibility(View.GONE);
        onLoadAmount();
        onLoadExchanges();
    }

    private void initView(View view) {
        mCardViewCardAccount = (CardView) view.findViewById(R.id.cardViewCardAccount);
        mRecyclerExchange = (RecyclerView) view.findViewById(R.id.recyclerExchange);
        mTvAmount = (TextView) view.findViewById(R.id.tvAmount);
        tvMoreExchange = (TextView) view.findViewById(R.id.tvMoreExchange);
    }


    @Override
    public void onLoadChart() {

    }

    @Override
    public void onLoadExchanges() {
        int limit = AppPreferences.getInstance().getLimitViewExchange(getContext());
        List<Exchange> exchanges = ExchangeManger.getInstance().getListExchangeByAccount(account.getId(), limit);
        mExchangeAccountAdapter = new ExchangeAccountAdapter(getContext(), exchanges);
        mRecyclerExchange.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerExchange.setNestedScrollingEnabled(false);
        mRecyclerExchange.setAdapter(mExchangeAccountAdapter);
    }

    @Override
    public void onLoadAmount() {
        if (account == null) {
            return;
        }
        mTvAmount.setTextColor(Color.parseColor(account.getColorCode()));
        String money = CurrencyUtils.getInstance().getStringMoneyType(mPresenter.getTotalAmountAvailable(account.getId()), account.getCurrencyCode());
        mTvAmount.setText(money);
    }
}
