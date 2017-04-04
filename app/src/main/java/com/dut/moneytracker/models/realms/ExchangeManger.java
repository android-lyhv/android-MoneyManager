package com.dut.moneytracker.models.realms;

import com.dut.moneytracker.charts.ValueChartAmount;
import com.dut.moneytracker.constant.TypeFilter;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.Filter;
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
    private static ExchangeManger ourInstance;

    public static ExchangeManger getInstance() {
        if (ourInstance==null){
            ourInstance = new ExchangeManger();
        }
        return ourInstance;
    }

    private ExchangeManger() {
    }


    public void deleteExchangeById(String id) {
        realm.beginTransaction();
        Exchange exchange = realm.where(Exchange.class).equalTo("id", id).findFirst();
        exchange.deleteFromRealm();
        realm.commitTransaction();
    }

    public List<Exchange> getExchanges() {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAll();
        realmResults = realmResults.sort("created", Sort.DESCENDING);
        List<Exchange> exchanges = realmResults.subList(0, realmResults.size());
        realm.commitTransaction();
        return exchanges;
    }

    public List<Exchange> getExchangesByAccount(String accountID) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).equalTo("id", accountID).findFirst();
        List<Exchange> exchanges = account.getExchanges();
        realm.commitTransaction();
        return exchanges;
    }

    public List<Exchange> getExchangesLimit(int limit) {
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

    public List<Exchange> getExchangesLimitByAccount(String accountID, int limit) {
        realm.beginTransaction();
        List<Exchange> exchanges;
        RealmList<Exchange> realmList = realm.where(Account.class).equalTo("id", accountID).findFirst().getExchanges();
        if (limit >= realmList.size()) {
            exchanges = realmList.sort("created", Sort.DESCENDING).subList(0, realmList.size());
        } else {
            exchanges = realmList.sort("created", Sort.DESCENDING).subList(0, limit);
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
            valueChartAmounts.add(new ValueChartAmount(dates.get(i), amount, DateTimeUtils.getInstance().getStringDayMonthUs(dates.get(i))));
        }
        return valueChartAmounts;
    }

    public List<ValueChartAmount> getValueChartByDailyDay(String idAccount, int limitDay) {
        List<Date> dates = DateTimeUtils.getInstance().getListLastDay(limitDay);
        List<ValueChartAmount> valueChartAmounts = new ArrayList<>();
        int size = dates.size();
        for (int i = 0; i < size; i++) {
            String amount = AccountManager.getInstance().getAmountAvailableByDate(idAccount, dates.get(i));
            valueChartAmounts.add(new ValueChartAmount(dates.get(i), amount, DateTimeUtils.getInstance().getStringDayMonthUs(dates.get(i))));
        }
        return valueChartAmounts;
    }


    public List<Exchange> getExchanges(Filter filter) {
        boolean isRequestAccount = filter.isRequestByAccount();
        if (!isRequestAccount) {
            return getExchangesFilter(filter.getDateFilter(), filter.getViewType());
        } else {
            return getExchangesFilterByAccount(filter.getAccountId(), filter.getDateFilter(), filter.getViewType());

        }
    }

    /**
     * @param accountID account id
     * @param date      time for filter
     * @param viewType  month, year, day..
     * @return
     */

    public List<Exchange> getExchangesFilterByAccount(String accountID, Date date, int viewType) {
        switch (viewType) {
            case TypeFilter.ALL:
                return getExchangesByAccount(accountID);
            case TypeFilter.DAY:
                return getExchangesSameDay(accountID, date);
            case TypeFilter.MONTH:
                return getExchangesSameMonth(accountID, date);
            case TypeFilter.YEAR:
                return getExchangesSameYear(accountID, date);
            case TypeFilter.CUSTOM:
                //TODO
            case TypeFilter.WEAK:
                //TODO
        }
        return new ArrayList<>();
    }

    public List<Exchange> getExchangesFilter(Date date, int viewType) {
        switch (viewType) {
            case TypeFilter.ALL:
                return getExchanges();
            case TypeFilter.DAY:
                return getExchangesSameDay(date);
            case TypeFilter.MONTH:
                return getExchangesSameMonth(date);
            case TypeFilter.YEAR:
                return getExchangesSameYear(date);
            case TypeFilter.CUSTOM:
                //TODO
            case TypeFilter.WEAK:
                //TODO
        }
        return new ArrayList<>();
    }

    private List<Exchange> getExchangesSameDay(Date date) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> exchangesNew = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameDate(exchange.getCreated(), date)) {
                exchangesNew.add(exchange);
            }
        }
        realm.commitTransaction();
        return exchangesNew;
    }

    private List<Exchange> getExchangesSameMonth(Date date) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> exchangesNew = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameMonth(exchange.getCreated(), date)) {
                exchangesNew.add(exchange);
            }
        }
        realm.commitTransaction();
        return exchangesNew;
    }

    private List<Exchange> getExchangesSameYear(Date date) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> exchangesNew = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameYear(exchange.getCreated(), date)) {
                exchangesNew.add(exchange);
            }
        }
        realm.commitTransaction();
        return exchangesNew;
    }

    private List<Exchange> getExchangesSameDay(String accountID, Date date) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).equalTo("id", accountID).findFirst();
        RealmList<Exchange> exchanges = account.getExchanges();
        RealmResults<Exchange> realmResults = exchanges.sort("created", Sort.DESCENDING);
        List<Exchange> result = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameDate(exchange.getCreated(), date)) {
                result.add(exchange);
            }
        }
        realm.commitTransaction();
        return result;
    }

    private List<Exchange> getExchangesSameMonth(String accountID, Date date) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).equalTo("id", accountID).findFirst();
        RealmList<Exchange> exchanges = account.getExchanges();
        RealmResults<Exchange> realmResults = exchanges.sort("created", Sort.DESCENDING);
        List<Exchange> result = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameMonth(exchange.getCreated(), date)) {
                result.add(exchange);
            }
        }
        realm.commitTransaction();
        return result;
    }

    private List<Exchange> getExchangesSameYear(String accountID, Date date) {
        realm.beginTransaction();
        Account account = realm.where(Account.class).equalTo("id", accountID).findFirst();
        RealmList<Exchange> exchanges = account.getExchanges();
        RealmResults<Exchange> realmResults = exchanges.sort("created", Sort.DESCENDING);
        List<Exchange> result = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameYear(exchange.getCreated(), date)) {
                result.add(exchange);
            }
        }
        realm.commitTransaction();
        return result;
    }
}
