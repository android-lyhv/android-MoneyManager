package com.dut.moneytracker.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 28/02/2017.
 */

public class User extends RealmObject {
    @PrimaryKey
    private String id;
    private String userName;
    private String email;
}
