package com.dut.moneytracker.objects;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 28/02/2017.
 */

public class Debit extends RealmObject {
    @PrimaryKey
    private String id;
    private float amount;
    private boolean isClose;
    private Date create;
    private Date expires;
    private String name;
    private String note;
    private RealmList<ExchangeDebit> exchangeDebits;
}
