package com.dut.moneytracker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.CardAccountAdapter;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.models.CurrencyManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.utils.StringUtils;

import java.util.List;


/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
public class FragmentAllAccount extends BaseFragment {
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
    //Model
    private List<Account> accounts;
    private AccountPresenter mPresenter = AccountPresenter.getInstance();
    //Call Back main Activity

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
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
        setViewAmount();
        onLoadViewCardAccount();
    }

    private void initView(View view) {
        mCardViewCardAccount = (CardView) view.findViewById(R.id.cardViewCardAccount);
        mRecyclerViewCardAccount = (RecyclerView) view.findViewById(R.id.recyclerViewCardAccount);
        mRecyclerExchange = (RecyclerView) view.findViewById(R.id.recyclerExchange);
        mTvAmount = (TextView) view.findViewById(R.id.tvAmount);
        tvMoreExchange = (TextView) view.findViewById(R.id.tvMoreExchange);
    }

    private void setViewAmount() {
        mTvAmount.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        String money = StringUtils.getInstance().getStringMoneyType(mPresenter.getTotalAmountAvailable(),
                CurrencyManager.getInstance().getCurrentCodeCurrencyDefault());
        mTvAmount.setText(money);
    }

    private void onLoadViewCardAccount() {
        mCardAccountAdapter = new CardAccountAdapter(getContext(), accounts);
        mRecyclerViewCardAccount.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerViewCardAccount.setNestedScrollingEnabled(false);
        mRecyclerViewCardAccount.setAdapter(mCardAccountAdapter);
        mRecyclerViewCardAccount.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), mRecyclerViewCardAccount, new ClickItemListener() {
            @Override
            public void onClick(View view, int position) {
                cardAccountListener.onClickCardAccount(position + 1);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void onLoadChart() {

    }

    private void onLoadListExchange() {

    }
}
