package com.dut.moneytracker.models.realms;

import android.content.Context;
import android.text.TextUtils;

import com.dut.moneytracker.R;
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

public class AccountManager extends RealmHelper {
    private static AccountManager accountManager;
    public static final String OUT_SIDE = "out_side";

    public static AccountManager getInstance() {
        if (accountManager == null) {
            accountManager = new AccountManager();
        }
        return accountManager;
    }

    private AccountManager() {

    }

    public void insertOrUpdate(Account object) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
    }

    public RealmResults<Account> getAccounts() {
        realm.beginTransaction();
        RealmResults<Account> realmResults = realm.where(Account.class).notEqualTo("id", OUT_SIDE).findAll();
        realm.commitTransaction();
        return realmResults;
    }

    public RealmResults<Account> getAccountsWithOutSide() {
        realm.beginTransaction();
        RealmResults<Account> realmResults = realm.where(Account.class).findAllSorted("created", Sort.ASCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    public RealmResults<Account> loadAccountsAsync() {
        return realm.where(Account.class).notEqualTo("id", OUT_SIDE).findAllAsync();
    }

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
        BigDecimal bigDecimal = new BigDecimal(getTotalInitAmount(date));
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
        BigDecimal bigDecimal = new BigDecimal(getInitAmountByAccount(idAccount, date));
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

    public String getTotalInitAmount(Date currentDate) {
        realm.beginTransaction();
        BigDecimal bigDecimal = new BigDecimal("0");
        String amountOneAccount;
        RealmResults<Account> resultsAccount = realm.where(Account.class).notEqualTo("id", OUT_SIDE).findAll();
        for (Account account : resultsAccount) {
            if (DateTimeUtils.getInstance().isSameDate(currentDate, account.getCreated())
                    || account.getCreated().before(currentDate)) {
                amountOneAccount = account.getInitAmount();
            } else {
                amountOneAccount = "0";
            }
            bigDecimal = bigDecimal.add(new BigDecimal(amountOneAccount));
        }
        realm.commitTransaction();
        return bigDecimal.toString();
    }

    public String getTotalInitAmount() {
        realm.beginTransaction();
        BigDecimal bigDecimal = new BigDecimal("0");
        RealmResults<Account> resultsAccount = realm.where(Account.class).notEqualTo("id", OUT_SIDE).findAll();
        for (Account account : resultsAccount) {
            bigDecimal = bigDecimal.add(new BigDecimal(account.getInitAmount()));
        }
        realm.commitTransaction();
        return bigDecimal.toString();
    }

    public String getInitAmountByAccount(String idAccount, Date currentDate) {
        BigDecimal bigDecimal;
        realm.beginTransaction();
        Account resultsAccount = realm.where(Account.class).equalTo("id", idAccount).findFirst();
        if (DateTimeUtils.getInstance().isSameDate(currentDate, resultsAccount.getCreated())
                || resultsAccount.getCreated().before(currentDate)) {
            bigDecimal = new BigDecimal(resultsAccount.getInitAmount());
        } else {
            bigDecimal = new BigDecimal("0");
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

    public String getTotalAmountExchanges(List<Exchange> exchanges) {
        BigDecimal bigDecimal = new BigDecimal("0");
        for (Exchange exchange : exchanges) {
            bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
        }
        return bigDecimal.toString();
    }

    public String getAccountNameById(String id) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        if (account == null) {
            return "";
        }
        return account.getName();
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

    public void createOutSideAccount(Context context) {
        Account account = new Account();
        account.setId(OUT_SIDE);
        account.setName(context.getString(R.string.out_side_account));
        account.setCreated(new Date(Long.MAX_VALUE));
        account.setInitAmount("0");
        insertOrUpdate(account);
    }

    public void onDeleteAccount(Context context, String idAccount) {
        // Delete FormServer
        ExchangeManger.getInstance().deleteExchangeByAccount(idAccount);
        ExchangeLoopManager.getInstance(context).deleteExchangeLoopByAccount(idAccount);
        DebitManager.getInstance().deleteDebitByAccount(idAccount);
        realm.beginTransaction();
        final Account account = realm.where(Account.class).equalTo("id", idAccount).findFirst();
        if (account != null) {
            account.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    public boolean isNameAccountAvailable(String newName, String nowIdAccount) {
        realm.beginTransaction();
        RealmResults<Account> accounts = realm.where(Account.class).notEqualTo("id", nowIdAccount).findAll();
        realm.commitTransaction();
        for (Account account : accounts) {
            if (TextUtils.equals(account.getName(), newName)) {
                return false;
            }
        }
        return true;
    }
}
