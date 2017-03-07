package com.dut.moneytracker.models;

import android.content.Context;

import com.dut.moneytracker.R;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/03/2017.
 */

public class AccountManager extends RealmHelper implements AccountListener {
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
    public float getAmountAvailable(String id) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).equalTo("id", id).findFirst();
        float total = account.getInitAmount();
        RealmList<Exchange> exchanges = account.getExchanges();
        for (Exchange exchange : exchanges) {
            total += exchange.getAmount();
        }
        realm.commitTransaction();
        return total;
    }

    @Override
    public float getAllAmountAvailable() {
        realm.beginTransaction();
        float totalInit = realm.where(Account.class).sum("initAmount").floatValue();
        float totalExchange = realm.where(Exchange.class).sum("amount").floatValue();
        realm.commitTransaction();
        return totalInit - totalExchange;
    }

    @Override
    public void addExchange(Account account, Exchange exchange) {
        realm.beginTransaction();
        account.getExchanges().add(exchange);
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
        account.setInitAmount(0);
        insertOrUpdate(account);

        Account account1 = new Account();
        account1.setId("abc");
        account1.setName(context.getString(R.string.name_default_account));
        account1.setDefault(true);
        account1.setColorCode("#FF028761");
        account1.setCreated(new Date());
        account1.setCurrencyCode(CurrencyManager.getInstance().getCurrentCodeCurrencyDefault());
        account1.setInitAmount(0);
        insertOrUpdate(account1);
    }
}
