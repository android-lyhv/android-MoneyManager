package com.dut.moneytracker.models.realms;

import io.realm.Realm;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 28/02/2017.
 */

abstract class RealmHelper {
    Realm realm;

    RealmHelper() {
        realm = Realm.getDefaultInstance();
    }
}
