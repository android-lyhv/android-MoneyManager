package com.dut.moneytracker.fragment;

import com.dut.moneytracker.models.AccountManager;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */

public class AccountPresenter {
    private static AccountPresenter accountPresenter = new AccountPresenter();
    AccountManager accountManager = AccountManager.getInstance();

    public static AccountPresenter getInstance() {
        return accountPresenter;
    }

    private AccountPresenter() {
    }


    public void loadRecyclerCardAccount() {

    }

    public float getTotalAmountAvailable(String idAccount) {
        return accountManager.getAmountAvailable(idAccount);
    }

    public float getTotalAmountAvailable() {
        return accountManager.getAllAmountAvailable();
    }
}
