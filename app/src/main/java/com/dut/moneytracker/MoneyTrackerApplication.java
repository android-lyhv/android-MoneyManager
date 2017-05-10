package com.dut.moneytracker;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.google.android.gms.maps.MapsInitializer;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 20/02/2017.
 */

public class MoneyTrackerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        configRealm(getApplicationContext());
        configFacebookSdk();
        configFireBase();
        configMap();
    }

    private void configFacebookSdk() {
        FacebookSdk.sdkInitialize(this);
    }

    private void configRealm(Context context) {
        Realm.init(context);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().build());
    }

    private void configFireBase() {
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().goOnline();
    }

    private void configMap() {
        MapsInitializer.initialize(this);
    }
}
