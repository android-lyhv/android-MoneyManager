package com.dut.moneytracker.currency;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 05/03/2017.
 */
public class CurrencyUtils {
    private static CurrencyUtils ourInstance = new CurrencyUtils();

    public static CurrencyUtils getInstance() {
        return ourInstance;
    }

    private CurrencyUtils() {
    }

    public String getStringMoneyType(String amount, String currencyCode) {
        BigDecimal bigDecimal = new BigDecimal(amount);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);
        Currency currency = Currency.getInstance(currencyCode);
        numberFormat.setCurrency(currency);
        return numberFormat.format(bigDecimal.doubleValue());
    }

    public String getStringAddMoney(String amount1, String amount2) {
        BigDecimal bigDecimal = new BigDecimal(amount1);
        bigDecimal = bigDecimal.add(new BigDecimal(amount2));
        return bigDecimal.toString();
    }

    public String convertMoney(String amount, String fromCode, String toCode) {
        BigDecimal bigDecimal = new BigDecimal(amount);
        return bigDecimal.toString();
    }

    public float getFloatMoney(String amount) {
        BigDecimal bigDecimal = new BigDecimal(amount);
        return bigDecimal.floatValue();
    }
}
