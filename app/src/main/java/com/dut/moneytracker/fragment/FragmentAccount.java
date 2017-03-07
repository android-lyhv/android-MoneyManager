package com.dut.moneytracker.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.CardAccountAdapter;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.utils.StringUtils;


/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
public class FragmentAccount extends BaseFragment {
    private static final String TAG = FragmentAccount.class.getSimpleName();
    //View
    private CardView mCardViewCardAccount;
    private RecyclerView mRecyclerExchange;
    private TextView mTvAmount;
    private TextView tvMoreExchange;
    private CardAccountAdapter mCardAccountAdapter;
    //Model
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
        setViewAmount();
    }

    private void initView(View view) {
        mCardViewCardAccount = (CardView) view.findViewById(R.id.cardViewCardAccount);
        mRecyclerExchange = (RecyclerView) view.findViewById(R.id.recyclerExchange);
        mTvAmount = (TextView) view.findViewById(R.id.tvAmount);
        tvMoreExchange = (TextView) view.findViewById(R.id.tvMoreExchange);
    }

    private void onLoadChart() {

    }

    private void onLoaEchanges() {

    }

    private void setViewAmount() {
        mTvAmount.setTextColor(Color.parseColor(account.getColorCode()));
        String money = StringUtils.getInstance().getStringMoneyType(mPresenter.getTotalAmountAvailable(account.getId()), "VND");
        mTvAmount.setText(money);
    }
}
