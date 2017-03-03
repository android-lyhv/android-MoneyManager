package com.dut.moneytracker.objects;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 28/02/2017.
 */

public class Exchange extends RealmObject {
    @PrimaryKey
    private String id;
    private String idCategory;
    private String idCurrency;
    private String idPaymentType;
    private int type;
    private float amount;
    private String note;
    private Place place;
    private Date created;
    private RealmList<Attechment> attechments;
}
