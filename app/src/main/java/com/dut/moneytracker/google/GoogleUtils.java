package com.dut.moneytracker.google;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 03/03/2017.
 */
public class GoogleUtils {
    private GoogleApiClient googleApiClient;
    private static GoogleUtils ourInstance = new GoogleUtils();

    public static GoogleUtils getInstance() {
        return ourInstance;
    }

    private GoogleUtils() {
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }
}
