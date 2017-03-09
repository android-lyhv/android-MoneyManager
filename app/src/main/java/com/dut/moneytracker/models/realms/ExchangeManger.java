package com.dut.moneytracker.models.realms;

import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;

import java.util.List;

import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
public class ExchangeManger extends RealmHelper {
    private static ExchangeManger ourInstance = new ExchangeManger();

    public static ExchangeManger getInstance() {
        return ourInstance;
    }

    private ExchangeManger() {
    }

    public List<Exchange> getListExchange() {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAll();
        realmResults = realmResults.sort("created", Sort.DESCENDING);
        List<Exchange> exchanges = realmResults.subList(0, realmResults.size());
        realm.commitTransaction();
        return exchanges;
    }

    public List<Exchange> getListExchange(int limit) {
        realm.beginTransaction();
        List<Exchange> exchanges;
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAll();
        realmResults = realmResults.sort("created", Sort.DESCENDING);
        if (limit >= realmResults.size()) {
            exchanges = realmResults.subList(0, realmResults.size());
        } else {
            exchanges = realmResults.subList(0, limit);
        }
        realm.commitTransaction();
        return exchanges;
    }

    public List<Exchange> getListExchangeByAccount(String accountID, int limit) {
        realm.beginTransaction();
        List<Exchange> exchanges;
        Account account = realm.where(Account.class).like("id", accountID).findFirst();
        List<Exchange> tempList = account.getExchanges();
        if (limit >= tempList.size()) {
            exchanges = tempList.subList(0, tempList.size());
        } else {
            exchanges = tempList.subList(0, limit);
        }
        realm.commitTransaction();
        return exchanges;
    }

    public List<Exchange> getListExchangeByAccount(String accountID) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).like("id", accountID).findFirst();
        List<Exchange> exchanges = account.getExchanges();
        realm.commitTransaction();
        return exchanges;
    }
}
