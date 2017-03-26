package com.dut.moneytracker.objects;

import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 23/03/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExchangeType extends RealmObject {
    private String id;
    private int tag;
    private String name;
    private String description;
}
