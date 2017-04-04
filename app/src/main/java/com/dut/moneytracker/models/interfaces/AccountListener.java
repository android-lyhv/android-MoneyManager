package com.dut.moneytracker.models.interfaces;

import com.dut.moneytracker.objects.Account;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/03/2017.
 */

public interface AccountListener {
    List<Account> getAccounts();

    String getAmountAvailableByAccount(String id);

    String getTotalAmountAvailable();
}
