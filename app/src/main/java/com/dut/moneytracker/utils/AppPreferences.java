package com.dut.moneytracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 01/03/2017.
 */
public class AppPreferences {
    private static final String FIRST_INIT = "first_init";
    private static AppPreferences ourInstance = new AppPreferences();

    public static AppPreferences getInstance() {
        return ourInstance;
    }

    private static final String SHARED_PREF_NAME = "MoneyTracker";

    private AppPreferences() {
    }

    public boolean isFirstInstall(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(FIRST_INIT, false);
    }

    public void setFirstInstall(Context context, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FIRST_INIT, value);
        editor.apply();
    }
}
