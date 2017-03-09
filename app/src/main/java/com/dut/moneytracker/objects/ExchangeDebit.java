package com.dut.moneytracker.objects;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 02/03/2017.
 */

public class ExchangeDebit extends RealmObject{
    @PrimaryKey
    private String id;
    private float ammout;
    private Date created;

}
