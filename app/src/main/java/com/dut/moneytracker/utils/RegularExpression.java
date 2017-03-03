package com.dut.moneytracker.utils;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 25/02/2017.
 */

public class RegularExpression {
    private static RegularExpression mRegularExpression = new RegularExpression();
    private final String TYPE_MONEY = "^[1-9]([0-9]+)(.[0-9]{1,2})?$";

    public static RegularExpression getInstance() {
        return mRegularExpression;
    }

    private RegularExpression() {

    }

    public boolean isValidateTypeMoney(String planText) {
        return planText.matches(TYPE_MONEY);
    }
}
