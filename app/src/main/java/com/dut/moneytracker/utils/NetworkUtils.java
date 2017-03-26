package com.dut.moneytracker.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 27/02/2017.
 */
public class NetworkUtils {
    private static NetworkUtils ourInstance;

    public static NetworkUtils getInstance() {
        if (ourInstance == null){
            ourInstance = new NetworkUtils();
        }
        return ourInstance;
    }

    private NetworkUtils() {
    }

    public boolean isConnectNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
