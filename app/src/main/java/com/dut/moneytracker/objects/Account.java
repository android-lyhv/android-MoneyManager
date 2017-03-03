package com.dut.moneytracker.objects;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 28/02/2017.
 */

public class Account extends RealmObject {
    @PrimaryKey
    private String id;
    private String idCurrency;
    private String name;
    private float initalValue;
    private Date created;
    private int colorCode;
    private boolean saveLocation;
    private RealmList<Exchange> exchanges;
}
