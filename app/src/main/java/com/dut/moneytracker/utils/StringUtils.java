package com.dut.moneytracker.utils;

import java.text.NumberFormat;
import java.util.Currency;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 05/03/2017.
 */
public class StringUtils {
    private static StringUtils ourInstance = new StringUtils();

    public static StringUtils getInstance() {
        return ourInstance;
    }

    private StringUtils() {
    }

    public String getStringMoneyType(float amount, String currencyCode) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        Currency currency = Currency.getInstance(currencyCode);
        if (currency == null) {
            return "VND";
        }
        numberFormat.setCurrency(currency);
        return numberFormat.format(amount);
    }
}
