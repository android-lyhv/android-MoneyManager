package com.dut.moneytracker.models.realms;

import android.content.Context;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.interfaces.AccountListener;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.utils.DateTimeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/03/2017.
 */

public class AccountManager extends RealmHelper implements AccountListener {
    private static AccountManager accountManager;

    public static AccountManager getInstance() {
        if (accountManager == null) {
            accountManager = new AccountManager();
        }
        return accountManager;
    }

    private AccountManager() {

    }

    @Override
    public RealmResults<Account> getAccounts() {
        realm.beginTransaction();
        RealmResults<Account> realmResults = realm.where(Account.class).findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    public RealmResults<Account> loadAccountsAsync() {
        return realm.where(Account.class).findAllSortedAsync("created", Sort.DESCENDING);
    }

    @Override
    public String getAmountAvailableByAccount(String idAccount) {
        BigDecimal bigDecimal = new BigDecimal(getInitAmountByAccount(idAccount));
        realm.beginTransaction();
        RealmResults<Exchange> exchanges = realm.where(Exchange.class).equalTo("idAccount", idAccount).findAll();
        for (Exchange exchange : exchanges) {
            bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
        }
        realm.commitTransaction();
        return bigDecimal.toString();
    }

    public String getAmountAvailableByDate(Date date) {
        BigDecimal bigDecimal = new BigDecimal(getTotalInitAmount());
        realm.beginTransaction();
        RealmResults<Exchange> resultsExchange = realm.where(Exchange.class).findAllSorted("created", Sort.ASCENDING);
        List<Exchange> newExchanges = new ArrayList<>();
        for (Exchange exchange : resultsExchange) {
            if (DateTimeUtils.getInstance().isSameDate(date, exchange.getCreated()) || exchange.getCreated().before(date)) {
                newExchanges.add(exchange);
            }
        }
        for (Exchange exchange : newExchanges) {
            bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
        }
        realm.commitTransaction();
        return bigDecimal.toString();
    }

    public String getAmountAvailableByDate(String idAccount, Date date) {
        BigDecimal bigDecimal = new BigDecimal(getInitAmountByAccount(idAccount));
        realm.beginTransaction();
        RealmResults<Exchange> exchanges = realm.where(Exchange.class).equalTo("idAccount", idAccount).findAll();
        exchanges.sort("created", Sort.ASCENDING);
        List<Exchange> newExchanges = new ArrayList<>();
        for (Exchange exchange : exchanges) {
            if (DateTimeUtils.getInstance().isSameDate(date, exchange.getCreated()) || exchange.getCreated().before(date)) {
                newExchanges.add(exchange);
            }
        }
        for (Exchange exchange : newExchanges) {
            bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
        }
        realm.commitTransaction();
        return bigDecimal.toString();
    }


    @Override
    public String getTotalAmountAvailable() {
        BigDecimal bigDecimal = new BigDecimal(getTotalInitAmount());
        realm.beginTransaction();
        RealmResults<Exchange> resultsExchange = realm.where(Exchange.class).findAll();
        for (Exchange exchange : resultsExchange) {
            bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
        }
        realm.commitTransaction();
        return bigDecimal.toString();
    }

    public String getTotalInitAmount() {
        realm.beginTransaction();
        BigDecimal bigDecimal = new BigDecimal("0");
        RealmResults<Account> resultsAccount = realm.where(Account.class).findAll();
        for (Account account : resultsAccount) {
            bigDecimal = bigDecimal.add(new BigDecimal(account.getInitAmount()));
        }
        realm.commitTransaction();
        return bigDecimal.toString();
    }

    public String getInitAmountByAccount(String idAccount) {
        realm.beginTransaction();
        Account resultsAccount = realm.where(Account.class).equalTo("id", idAccount).findFirst();
        BigDecimal bigDecimal = new BigDecimal(resultsAccount.getInitAmount());
        realm.commitTransaction();
        return bigDecimal.toString();
    }

    public String getTotalAmountByListExchange(List<Exchange> exchanges) {
        BigDecimal bigDecimal = new BigDecimal("0");
        for (Exchange exchange : exchanges) {
            bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
        }
        return bigDecimal.toString();
    }

    public String getAccountNameById(String id) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).equalTo("id", id).findFirst();
        String name = account.getName();
        realm.commitTransaction();
        return name;
    }

    public void createDefaultAccount(Context context) {
        Account account = new Account();
        account.setId(context.getString(R.string.id_default_account));
        account.setName(context.getString(R.string.name_default_account));
        account.setDefault(true);
        account.setColorHex(context.getString(R.string.color_account_default));
        account.setCreated(new Date());
        account.setInitAmount("0");
        insertOrUpdate(account);
    }
}
