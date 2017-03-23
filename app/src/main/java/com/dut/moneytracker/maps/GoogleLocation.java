
package com.dut.moneytracker.maps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.dut.moneytracker.objects.Place;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Copyright AsianTech@ Inc
 * Created by Sumiu on 11/14/2016.
 */


public class GoogleLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = GoogleLocation.class.getName();
    private static final long UPDATE_INTERVAL = 10000;
    private static final long FASTEST_INTERVAL = 5000;
    private static final float DISPLACEMENT = 10;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private CurrentPlaceListener currentPlaceListener;
    private Context mContext;
    private Geocoder geocoder;

    public interface CurrentPlaceListener {
        void onResultPlace(Place place);
    }

    public void registerCurrentPlaceListener(GoogleLocation.CurrentPlaceListener currentPlaceListener) {
        this.currentPlaceListener = currentPlaceListener;
    }

    public GoogleLocation(Context context) {
        mContext = context;
        initApiClient();
        configLocationUpdate();
        geocoder = new Geocoder(mContext, Locale.getDefault());
    }

    private void initApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void connectApiGoogle() {
        mGoogleApiClient.connect();
    }

    public void disConnectApiGoogle() {
        Log.d(TAG, "disconnection google Api Location");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void configLocationUpdate() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setMaxWaitTime(2000L);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    public void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void stopLocationUpDate() {
        Log.d(TAG, "stop location update");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public Location getLastLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: aaaa");
        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (currentPlaceListener != null) {
            currentPlaceListener.onResultPlace(new Place());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: aaaaa");
        if (currentPlaceListener != null) {
            currentPlaceListener.onResultPlace(new Place());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: aaaa");
        if (currentPlaceListener != null) {
            responseExchangePlace(location);
        }
    }

    private void responseExchangePlace(Location location) {
        try {
            List<Address> addresses;
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            // set up
            Place place = new Place();
            place.setAddress(address);
            place.setLatitude(location.getLatitude());
            place.setLongitude(location.getLongitude());
            currentPlaceListener.onResultPlace(place);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

