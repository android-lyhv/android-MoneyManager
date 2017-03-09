package com.dut.moneytracker.models.realms;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 28/02/2017.
 */

abstract class RealmHelper {
    Realm realm;

    RealmHelper() {
        realm = Realm.getDefaultInstance();
    }

    public void insertOrUpdate(RealmObject object) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
    }

    public void delete(RealmObject object) {
        realm.beginTransaction();
        object.deleteFromRealm();
        realm.insertOrUpdate(object);
    }
}
