package com.dut.moneytracker.models.realms;

import com.dut.moneytracker.charts.ValueChartAmount;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
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

    public List<Exchange> getExchanges() {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAll();
        realmResults = realmResults.sort("created", Sort.DESCENDING);
        List<Exchange> exchanges = realmResults.subList(0, realmResults.size());
        realm.commitTransaction();
        return exchanges;
    }

    public List<Exchange> getExchanges(int limit) {
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

    public List<Exchange> getExchangesByAccount(String accountID, int limit) {
        realm.beginTransaction();
        List<Exchange> exchanges;
        RealmList<Exchange> realmList = realm.where(Account.class).equalTo("id", accountID).findFirst().getExchanges();
        if (limit >= realmList.size()) {
            exchanges =  realmList.sort("created", Sort.DESCENDING).subList(0, realmList.size());
        } else {
            exchanges =  realmList.sort("created", Sort.DESCENDING).subList(0, limit);
        }
        realm.commitTransaction();
        return exchanges;
    }

    public List<ValueChartAmount> getValueChartByDailyDay(int limitDay) {
        List<Date> dates = DateTimeUtils.getInstance().getListLastDay(limitDay);
        List<ValueChartAmount> valueChartAmounts = new ArrayList<>();
        int size = dates.size();
        for (int i = 0; i < size; i++) {
            String amount = AccountManager.getInstance().getAmountAvailableByDate(dates.get(i));
            valueChartAmounts.add(new ValueChartAmount(dates.get(i), amount, DateTimeUtils.getInstance().getSortStringDate(dates.get(i))));
        }
        return valueChartAmounts;
    }

    public List<ValueChartAmount> getValueChartByDailyDay(String idAccount, int limitDay) {
        List<Date> dates = DateTimeUtils.getInstance().getListLastDay(limitDay);
        List<ValueChartAmount> valueChartAmounts = new ArrayList<>();
        int size = dates.size();
        for (int i = 0; i < size; i++) {
            String amount = AccountManager.getInstance().getAmountAvailableByDate(idAccount, dates.get(i));
            valueChartAmounts.add(new ValueChartAmount(dates.get(i), amount, DateTimeUtils.getInstance().getSortStringDate(dates.get(i))));
        }
        return valueChartAmounts;
    }

    public List<Exchange> getExchangesByAccount(String accountID) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).equalTo("id", accountID).findFirst();
        List<Exchange> exchanges = account.getExchanges();
        realm.commitTransaction();
        return exchanges;
    }
}
