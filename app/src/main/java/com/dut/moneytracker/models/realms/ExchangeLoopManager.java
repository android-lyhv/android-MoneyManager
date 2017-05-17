package com.dut.moneytracker.models.realms;

import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.FilterLoop;
import com.dut.moneytracker.constant.LoopType;
import com.dut.moneytracker.models.firebase.FireBaseSync;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.ExchangeLooper;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 03/04/2017.
 */

public class ExchangeLoopManager extends RealmHelper {
    private static final long PENDING_DAY = 24 * 60 * 60 * 1000L;
    private static final long PENDING_WEEK = 7 * PENDING_DAY;
    private static final long PENDING_MONTH = 30 * PENDING_DAY;
    private static final long PENDING_YEAH = 365 * PENDING_DAY;
    private static ExchangeLoopManager exchangeLoopManager;

    public static ExchangeLoopManager getInstance() {
        if (exchangeLoopManager == null) {
            exchangeLoopManager = new ExchangeLoopManager();
        }
        return exchangeLoopManager;
    }

    private ExchangeLoopManager() {
    }

    /**
     * Sync frirebase
     */
    public void insertOrUpdate(ExchangeLooper object) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
        FireBaseSync.getInstance().upDateExchangeLoop(object);
    }

    public void deleteExchangeLoopByAccount(String idAccount) {
        realm.beginTransaction();
        ExchangeLooper exchangeLooper = realm.where(ExchangeLooper.class).equalTo("idAccount", idAccount).findFirst();
        if (exchangeLooper != null) {
            FireBaseSync.getInstance().deleteExchangeLoop(exchangeLooper.getId());
            exchangeLooper.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    public void deleteExchangeLoopById(int id) {
        realm.beginTransaction();
        ExchangeLooper exchangeLooper = realm.where(ExchangeLooper.class).equalTo("id", id).findFirst();
        exchangeLooper.deleteFromRealm();
        realm.commitTransaction();
        FireBaseSync.getInstance().deleteExchangeLoop(id);
    }

    public void insertNewExchangeLoop(ExchangeLooper exchangeLooper) {
        Number number = realm.where(ExchangeLooper.class).max("id");
        int nextId;
        if (number == null) {
            nextId = 0;
        } else {
            nextId = number.intValue() + 1;
        }
        exchangeLooper.setId(nextId);
        insertOrUpdate(exchangeLooper);
    }

    /*********************************************/
    public RealmResults<ExchangeLooper> onLoadSyncExchangeLooper() {
        return realm.where(ExchangeLooper.class).findAllSortedAsync("id", Sort.DESCENDING);
    }

    public RealmResults<ExchangeLooper> onLoadSyncExchangeLooper(int idFilter) {
        switch (idFilter) {
            case FilterLoop.ALL:
                return onLoadSyncExchangeLooper();
            case FilterLoop.DAY:
                return realm.where(ExchangeLooper.class).equalTo("typeLoop", LoopType.DAY).findAllSortedAsync("id", Sort.DESCENDING);
            case FilterLoop.WEAK:
                return realm.where(ExchangeLooper.class).equalTo("typeLoop", LoopType.WEAK).findAllSortedAsync("id", Sort.DESCENDING);
            case FilterLoop.MONTH:
                return realm.where(ExchangeLooper.class).equalTo("typeLoop", LoopType.MONTH).findAllSortedAsync("id", Sort.DESCENDING);
            case FilterLoop.YEAR:
                return realm.where(ExchangeLooper.class).equalTo("typeLoop", LoopType.YEAR).findAllSortedAsync("id", Sort.DESCENDING);
            case FilterLoop.INCOME:
                return realm.where(ExchangeLooper.class).equalTo("typeExchange", ExchangeType.INCOME).findAllSortedAsync("id", Sort.DESCENDING);
            case FilterLoop.EXPENSES:
                return realm.where(ExchangeLooper.class).equalTo("typeExchange", ExchangeType.EXPENSES).findAllSortedAsync("id", Sort.DESCENDING);
        }
        return onLoadSyncExchangeLooper();
    }

    private Exchange copyExchange(ExchangeLooper exchangeLooper) {
        Exchange exchange = new Exchange();
        exchange.setId(UUID.randomUUID().toString());
        exchange.setAmount(exchangeLooper.getAmount());
        exchange.setDescription(exchangeLooper.getDescription());
        exchange.setAddress(exchangeLooper.getAddress());
        exchange.setLatitude(exchangeLooper.getLatitude());
        exchange.setLongitude(exchangeLooper.getLongitude());
        exchange.setCreated(exchangeLooper.getCreated());
        exchange.setTypeExchange(exchangeLooper.getTypeExchange());
        exchange.setIdAccount(exchangeLooper.getIdAccount());
        exchange.setIdCategory(exchangeLooper.getIdCategory());
        return exchange;
    }

    public void onGenerateNewExchange() {
        RealmResults<ExchangeLooper> exchangeLoops = getExchangeLoopAvailable();
        for (ExchangeLooper exchangeLooper : exchangeLoops) {
            long lastDateCreated = exchangeLooper.getCreated().getTime();
            switch (exchangeLooper.getTypeLoop()) {
                case LoopType.DAY:
                    if (Calendar.getInstance().getTimeInMillis() - lastDateCreated >= PENDING_DAY) {
                        createNewExchange(exchangeLooper, lastDateCreated, LoopType.DAY);
                    }
                    break;
                case LoopType.WEAK:
                    if (Calendar.getInstance().getTimeInMillis() - lastDateCreated >= PENDING_WEEK) {
                        createNewExchange(exchangeLooper, lastDateCreated, LoopType.WEAK);
                    }
                    break;
                case LoopType.MONTH:
                    if (Calendar.getInstance().getTimeInMillis() - lastDateCreated >= PENDING_MONTH) {
                        createNewExchange(exchangeLooper, lastDateCreated, LoopType.MONTH);
                    }
                    break;
                case LoopType.YEAR:
                    if (Calendar.getInstance().getTimeInMillis() - lastDateCreated >= PENDING_YEAH) {
                        createNewExchange(exchangeLooper, lastDateCreated, LoopType.YEAR);
                    }
                    break;
            }
        }
    }

    private void createNewExchange(ExchangeLooper exchangeLooper, long lastDateCreated, int loopType) {
        realm.beginTransaction();
        exchangeLooper.setCreated(getGenNewDateExchange(lastDateCreated, loopType));
        realm.commitTransaction();
        insertOrUpdate(exchangeLooper);
        ExchangeManger.getInstance().insertOrUpdate(copyExchange(exchangeLooper));
    }

    private RealmResults<ExchangeLooper> getExchangeLoopAvailable() {
        realm.beginTransaction();
        RealmResults<ExchangeLooper> exchangeLoops = realm.where(ExchangeLooper.class).equalTo("isLoop", true).findAll();
        realm.commitTransaction();
        return exchangeLoops;
    }

    private Date getGenNewDateExchange(long lastDateCreated, int typeLoop) {
        switch (typeLoop) {
            case LoopType.DAY:
                lastDateCreated += PENDING_DAY;
                break;
            case LoopType.WEAK:
                lastDateCreated += PENDING_WEEK;
                break;
            case LoopType.MONTH:
                lastDateCreated += PENDING_MONTH;
                break;
            case LoopType.YEAR:
                lastDateCreated += PENDING_YEAH;
                break;
        }
        return new Date(lastDateCreated);
    }
}
