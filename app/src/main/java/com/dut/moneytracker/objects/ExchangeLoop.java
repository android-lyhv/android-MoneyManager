package com.dut.moneytracker.objects;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 19/03/2017.
 */

public class ExchangeLoop extends RealmObject {
    @PrimaryKey
    private String id;
    private int typeExchange;
    private String idAccount;
    private String idCategory;
    private String idAccountTransfer;
    private String currencyCode;
    private String amount;
    private String description;
    private Date created;
    private boolean isLoop;
    private int typeLoop;
    private Place place;
}
