package com.dut.moneytracker.models.realms;

import com.dut.moneytracker.objects.Debit;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 11/04/2017.
 */
public class DebitManager extends RealmHelper {
    private static DebitManager ourInstance;

    public static DebitManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DebitManager();
        }
        return ourInstance;
    }

    private DebitManager() {
    }

    public RealmResults<Debit> onLoadDebitAsync() {
        return realm.where(Debit.class).findAllAsync();
    }

    public void deleteDebitByAccount(String idAccount) {
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("idAccount", idAccount).findFirst();
        if (debit != null) {
            debit.deleteFromRealm();
        }
        realm.commitTransaction();
    }
}
