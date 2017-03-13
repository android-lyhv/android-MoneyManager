package com.dut.moneytracker.models.presenter;

import com.dut.moneytracker.models.realms.AccountManager;

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

    public String getTotalAmountAvailable(String idAccount) {
        return accountManager.getAmountAvailableByAccount(idAccount);
    }

    public String getTotalAmountAvailable() {
        return accountManager.getAllAmountAvailable();
    }
}
