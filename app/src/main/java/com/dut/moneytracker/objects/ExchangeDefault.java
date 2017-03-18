package com.dut.moneytracker.objects;

import java.util.Date;

import io.realm.annotations.PrimaryKey;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 17/03/2017.
 */

public class ExchangeDefault {
    @PrimaryKey
    private String id;
    private int typeExchange;// thu nhap, chuyen tien, chi tieu, di vay, cho vay
    private String idAccount;
    private String idCategory;
    private String idAccountTransfer;// truong hop chuyen tien
    private String currencyCode;
    private String amount;
    private String description;
    private Date created;
    private boolean isLoop;
    private int typeLoop;
}
