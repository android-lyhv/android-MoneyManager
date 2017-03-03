package com.dut.moneytracker.objects;

import io.realm.annotations.PrimaryKey;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 01/03/2017.
 */

public class PaymentType {
    @PrimaryKey
    private String id;
    private String name;
    private String description;
}
