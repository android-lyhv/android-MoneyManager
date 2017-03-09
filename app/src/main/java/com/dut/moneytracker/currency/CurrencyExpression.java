package com.dut.moneytracker.currency;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 25/02/2017.
 */

public class CurrencyExpression {
    private static CurrencyExpression mCurrencyExpression = new CurrencyExpression();
    private final String TYPE_MONEY = "^[1-9]([0-9]*)(.[0-9]{1,2})?$";
    private final String TYPE_MONEY1 = "^[1-9]([0-9]*)(.)$";

    public static CurrencyExpression getInstance() {
        return mCurrencyExpression;
    }

    private CurrencyExpression() {

    }

    public boolean isValidateTypeMoney(String planText) {
        return planText.matches(TYPE_MONEY) || planText.matches(TYPE_MONEY1);
    }
    public boolean isValidateMoney(String planText) {
        return planText.matches(TYPE_MONEY);
    }
}
