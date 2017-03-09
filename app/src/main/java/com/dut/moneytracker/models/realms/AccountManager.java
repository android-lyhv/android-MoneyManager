package com.dut.moneytracker.models.realms;

import android.content.Context;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.interfaces.AccountListener;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/03/2017.
 */

public class AccountManager extends RealmHelper implements AccountListener {
    private static final String TAG =AccountManager.class.getSimpleName();
    private static AccountManager accountManager = new AccountManager();

    public static AccountManager getInstance() {
        return accountManager;
    }

    private AccountManager() {

    }

    @Override
    public List<Account> getListAccount() {
        realm.beginTransaction();
        RealmResults<Account> realmResults = realm.where(Account.class).findAll();
        realmResults.sort("created", Sort.ASCENDING);
        List<Account> accounts = realmResults.subList(0, realmResults.size());
        realm.commitTransaction();
        return accounts;
    }

    @Override
    public String getAmountAvailable(String id) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).equalTo("id", id).findFirst();
        BigDecimal bigDecimal = new BigDecimal(account.getInitAmount());
        RealmList<Exchange> exchanges = account.getExchanges();
        for (Exchange exchange : exchanges) {
            bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
        }
        realm.commitTransaction();
        return bigDecimal.toString();
    }

    @Override
    public String getAllAmountAvailable() {
        BigDecimal bigDecimal = new BigDecimal("0");
        realm.beginTransaction();
        RealmResults<Account> resultsAccount = realm.where(Account.class).findAll();
        for (Account account : resultsAccount) {
            bigDecimal = bigDecimal.add(new BigDecimal(account.getInitAmount()));
        }
        RealmResults<Exchange> resultsExchange = realm.where(Exchange.class).findAll();
        for (Exchange exchange : resultsExchange) {
            bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
        }
        realm.commitTransaction();
        return bigDecimal.toString();
    }

    @Override
    public void addExchange(Account account, Exchange exchange) {
        realm.beginTransaction();
        account.getExchanges().add(exchange);
        realm.commitTransaction();
    }
    public String getAccountNameById(String id) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).like("id",id).findFirst();
        String name = account.getName();
        realm.commitTransaction();
        return name;
    }

    public void addExchange(String idAccount, Exchange exchange) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).like("id", idAccount).findFirst();
        if (account != null) {
            account.getExchanges().add(exchange);
        }
        realm.commitTransaction();
    }

    public void createDefaultAccount(Context context) {
        Account account = new Account();
        account.setId(context.getString(R.string.id_default_account));
        account.setName(context.getString(R.string.name_default_account));
        account.setDefault(true);
        account.setColorCode("#FF028761");
        account.setCreated(new Date());
        account.setCurrencyCode(CurrencyManager.getInstance().getCurrentCodeCurrencyDefault());
        account.setInitAmount("100000");
        insertOrUpdate(account);

        Account account1 = new Account();
        account1.setId(UUID.randomUUID().toString());
        account1.setName("ATM");
        account1.setDefault(true);
        account1.setColorCode("#B71C1C");
        account1.setCreated(new Date());
        account1.setCurrencyCode(CurrencyManager.getInstance().getCurrentCodeCurrencyDefault());
        account1.setInitAmount("100000");
        insertOrUpdate(account1);
    }
}
