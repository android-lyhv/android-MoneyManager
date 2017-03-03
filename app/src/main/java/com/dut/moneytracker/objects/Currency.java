package com.dut.moneytracker.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 01/03/2017.
 */

public class Currency extends RealmObject {
    @PrimaryKey
    private String id;
    private String title;
    private String nameContry;
}
