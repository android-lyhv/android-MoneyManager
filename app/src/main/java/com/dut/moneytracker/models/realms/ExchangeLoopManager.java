package com.dut.moneytracker.models.realms;

import android.content.Context;

import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.ExchangeLooper;
import com.dut.moneytracker.recevier.GenerateManager;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 03/04/2017.
 */

public class ExchangeLoopManager extends RealmHelper {
    private static ExchangeLoopManager exchangeLoopManager;
    private GenerateManager mGenerateManager;

    public static ExchangeLoopManager getInstance(Context context) {
        if (exchangeLoopManager == null) {
            exchangeLoopManager = new ExchangeLoopManager(context);
        }
        return exchangeLoopManager;
    }

    private ExchangeLoopManager(Context context) {
        mGenerateManager = new GenerateManager(context);
    }

    public RealmResults<ExchangeLooper> getListLoopExchange() {
        realm.beginTransaction();
        RealmResults<ExchangeLooper> realmResults = realm.where(ExchangeLooper.class).findAllSorted("created", Sort.ASCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    public void deleteExchangeLoopById(int id) {
        realm.beginTransaction();
        ExchangeLooper exchangeLooper = realm.where(ExchangeLooper.class).equalTo("id", id).findFirst();
        exchangeLooper.deleteFromRealm();
        realm.commitTransaction();
        mGenerateManager.removePendingLoopExchange(id);
    }

    public ExchangeLooper getExchangeLooperById(int id) {
        realm.beginTransaction();
        ExchangeLooper exchangeLooper = realm.where(ExchangeLooper.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return exchangeLooper;
    }

    public void generateNewExchange(int idExchangeLooper) {
        ExchangeLooper exchangeLooper = getExchangeLooperById(idExchangeLooper);
        if (exchangeLooper == null || !exchangeLooper.isLoop()) {
            return;
        }
        ExchangeManger.getInstance().insertOrUpdate(copyExchange(exchangeLooper));
    }

    public Exchange copyExchange(ExchangeLooper exchangeLooper) {
        Exchange exchange = new Exchange();
        exchange.setId(UUID.randomUUID().toString());
        exchange.setAmount(exchangeLooper.getAmount());
        exchange.setDescription(exchangeLooper.getDescription());
        exchange.setPlace(exchangeLooper.getPlace());
        exchange.setCurrencyCode(exchangeLooper.getCurrencyCode());
        exchange.setCreated(new Date());
        if (exchangeLooper.getTypeExchange() != ExchangeType.TRANSFER) {
            exchange.setTypeExchange(exchangeLooper.getTypeExchange());
            exchange.setIdAccount(exchangeLooper.getIdAccount());
            exchange.setIdCategory(exchangeLooper.getIdCategory());
        } else {
            //TODO
        }
        return exchange;
    }

    public void upDatePendingExchange(ExchangeLooper exchangeLooper) {
        insertOrUpdate(exchangeLooper);
        mGenerateManager.pendingGenerateExchange(exchangeLooper);
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
        mGenerateManager.pendingGenerateExchange(exchangeLooper);
    }
}
