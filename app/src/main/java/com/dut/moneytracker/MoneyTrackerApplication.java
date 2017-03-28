package com.dut.moneytracker;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.google.android.gms.maps.MapsInitializer;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 20/02/2017.
 */

public class MoneyTrackerApplication extends Application {
    private static final String TAG = MoneyTrackerApplication.class.getSimpleName();

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
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }

    private void configFireBase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private void configMap() {
        MapsInitializer.initialize(this);
    }
}
