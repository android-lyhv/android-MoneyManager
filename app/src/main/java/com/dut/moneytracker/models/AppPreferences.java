package com.dut.moneytracker.models;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 01/03/2017.
 */
public class AppPreferences {
    private static AppPreferences ourInstance;
    private static final String FIRST_INIT = "first_init";
    private static final String SHARED_PREF_NAME = "MoneyTracker";
    private static final String KEY_REFERENCE_DATABASE = "KEY_REFERENCE_DATABASE";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_DEFAULT_CODE_CURRENCY = "KEY_DEFAULT_CODE_CURRENCY";

    private static final String KEY_LIMIT_VIEW = "KEY_LIMIT_VIEW";

    public static AppPreferences getInstance() {
        if (ourInstance == null) {
            ourInstance = new AppPreferences();
        }
        return ourInstance;
    }

    private AppPreferences() {
    }
    public boolean isInitCategory(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(FIRST_INIT, false);
    }

    public void setInitCategory(Context context, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FIRST_INIT, value);
        editor.apply();
    }


    public String getReferenceDatabase(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(KEY_REFERENCE_DATABASE, "");
    }

    public void setReferenceDatabase(Context context, String userId) {
        String reference = String.format(Locale.US, "/users/%s/", userId);
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_REFERENCE_DATABASE, reference);
        editor.apply();
    }

    public void setCurrentUserId(Context context, String userId) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getCurrentUserId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(KEY_USER_ID, "");
    }
}
