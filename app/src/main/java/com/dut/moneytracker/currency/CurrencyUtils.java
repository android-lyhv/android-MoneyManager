package com.dut.moneytracker.currency;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 05/03/2017.
 */
public class CurrencyUtils {
    public static final String DEFAULT_CURRENCY_CODE = "VND";

    private static CurrencyUtils ourInstance;

    public static CurrencyUtils getInstance() {
        if (ourInstance == null) {
            ourInstance = new CurrencyUtils();
        }
        return ourInstance;
    }

    private CurrencyUtils() {
    }

    public String getStringMoneyFormat(String amount, String currencyCode) {
        BigDecimal bigDecimal = new BigDecimal(amount);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);
        Currency currency = Currency.getInstance(currencyCode);
        numberFormat.setCurrency(currency);
        return numberFormat.format(bigDecimal.doubleValue());
    }

    public int compareAmount(String amount1, String amount2) {
        BigDecimal bigDecimal1 = new BigDecimal(amount1);
        BigDecimal bigDecimal2 = new BigDecimal(amount2);
        if (Math.abs(bigDecimal1.floatValue()) == Math.abs(bigDecimal2.floatValue())) {
            return 0;
        }
        if (Math.abs(bigDecimal1.floatValue()) > Math.abs(bigDecimal2.floatValue())) {
            return 1;
        }
        return -1;
    }

    public float getFloatMoney(String amount) {
        BigDecimal bigDecimal = new BigDecimal(amount);
        return bigDecimal.floatValue();
    }

}
