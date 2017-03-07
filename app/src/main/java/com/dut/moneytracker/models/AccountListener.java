package com.dut.moneytracker.models;

import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/03/2017.
 */

public interface AccountListener {
    List<Account> getListAccount();

    float getAmountAvailable(String id);

    float getAllAmountAvailable();

    void addExchange(Account account, Exchange exchange);
}
