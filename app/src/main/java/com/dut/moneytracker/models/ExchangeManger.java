package com.dut.moneytracker.models;

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
        realmResults.sort("created", Sort.DESCENDING);
        List<Exchange> exchanges = realmResults.subList(0, realmResults.size());
        realm.commitTransaction();
        return exchanges;
    }

    public List<Exchange> getListExchange(int limit) {
        realm.beginTransaction();
        List<Exchange> exchanges;
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAll();
        realmResults.sort("created", Sort.DESCENDING);
        if (limit <= realmResults.size()) {
            exchanges = realmResults.subList(0, realmResults.size());
        } else {
            exchanges = realmResults.subList(0, limit);
        }
        realm.commitTransaction();
        return exchanges;
    }
}
