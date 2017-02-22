package com.dut.moneytracker;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 20/02/2017.
 */

public class MoneyTrackerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        configFacebookSdk();
    }

    private void configFacebookSdk() {
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
