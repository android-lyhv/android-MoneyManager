package com.dut.moneytracker.models.realms;

import android.content.Context;
import android.util.Log;

import com.dut.moneytracker.constant.ExchangeType;
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
    private static final String TAG = ExchangeLoopManager.class.getSimpleName();
    private static final long PENDING_DAY = 24 * 60 *60 * 1000L;
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
    public RealmResults<ExchangeLooper> getExchangeLoops() {
        realm.beginTransaction();
        RealmResults<ExchangeLooper> realmResults = realm.where(ExchangeLooper.class).findAllSorted("created", Sort.ASCENDING);
        realm.commitTransaction();
        return realmResults;
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
        if (exchangeLooper.getTypeExchange() != ExchangeType.TRANSFER) {
            exchange.setTypeExchange(exchangeLooper.getTypeExchange());
            exchange.setIdAccount(exchangeLooper.getIdAccount());
            exchange.setIdCategory(exchangeLooper.getIdCategory());
        } else {
            //TODO if transfer
        }
        return exchange;
    }

    public void onGenerateExchange(Context context) {
        Log.d(TAG, "onGenerateExchange: aaaaaaaaaaaa");
        FireBaseSync.getInstance().initDataReference(context);
        RealmResults<ExchangeLooper> exchangeLoops = getExchangeLoopAvailable();
        for (ExchangeLooper exchangeLooper : exchangeLoops) {
            switch (exchangeLooper.getTypeLoop()) {
                case LoopType.DAY:
                    if (Calendar.getInstance().getTimeInMillis() - exchangeLooper.getCreated().getTime() >= PENDING_DAY) {
                        createNewExchange(exchangeLooper);
                    }
                    break;
                case LoopType.WEAK:
                    if (Calendar.getInstance().getTimeInMillis() - exchangeLooper.getCreated().getTime() >= PENDING_WEEK) {
                        createNewExchange(exchangeLooper);
                    }
                    break;
                case LoopType.MONTH:
                    if (Calendar.getInstance().getTimeInMillis() - exchangeLooper.getCreated().getTime() >= PENDING_MONTH) {
                        createNewExchange(exchangeLooper);
                    }
                    break;
                case LoopType.YEAR:
                    if (Calendar.getInstance().getTimeInMillis() - exchangeLooper.getCreated().getTime() >= PENDING_YEAH) {
                        createNewExchange(exchangeLooper);
                    }
                    break;
            }
        }
    }

    private void createNewExchange(ExchangeLooper exchangeLooper) {
        realm.beginTransaction();
        exchangeLooper.setCreated(new Date());
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
}
