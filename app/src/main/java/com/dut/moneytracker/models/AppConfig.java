package com.dut.moneytracker.models;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

import io.realm.Realm;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 01/03/2017.
 */
public class AppConfig {
    private static AppConfig ourInstance;
    private static final String FIRST_INIT = "first_init";
    private static final String SHARED_PREF_NAME = "MoneyTracker";
    private static final String KEY_REFERENCE_DATABASE = "KEY_REFERENCE_DATABASE";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";
    private static Realm mRealm;

    public static AppConfig getInstance() {
        if (ourInstance == null) {
            ourInstance = new AppConfig();
        }
        return ourInstance;
    }

    private AppConfig() {
        mRealm = Realm.getDefaultInstance();
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

    public boolean isLogin(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }

    public void setLogin(Context context, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_IS_LOGIN, value);
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

    public void clearAllData(Context context) {
        clearDataRealm();
        clearSharedPreferences(context);
    }

    private void clearDataRealm() {
        mRealm.beginTransaction();
        mRealm.removeAllChangeListeners();
        mRealm.deleteAll();
        mRealm.close();
        mRealm.commitTransaction();
    }

    private void clearSharedPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
