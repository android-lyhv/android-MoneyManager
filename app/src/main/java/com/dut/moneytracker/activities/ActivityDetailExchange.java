package com.dut.moneytracker.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.activities.interfaces.DetailExchangeListener;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.constant.ResultCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.CategoryManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.ExchangePlace;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.dut.moneytracker.utils.DialogUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 12/03/2017.
 */

public class ActivityDetailExchange extends AppCompatActivity implements View.OnClickListener, DetailExchangeListener, OnMapReadyCallback {
    private static final String TAG = ActivityDetailExchange.class.getSimpleName();
    private Toolbar toolbar;
    private RelativeLayout rlCategory;
    private RelativeLayout rlAmount;
    private RelativeLayout rlAccount;
    private RelativeLayout rlCurrency;
    private RelativeLayout rlDate;
    private RelativeLayout rlTime;
    private RelativeLayout rlLocation;

    private TextView tvCategory;
    private TextView tvAmount;
    private TextView tvAccount;
    private TextView tvCurrency;
    private TextView mTvDescription;
    private TextView tvDate;
    private TextView tvTime;
    private ImageView imgLocation;
    private MapView mapView;
    //
    private Exchange mExchange;
    private ExchangePlace mExchangePlace;
    //Google Map
    GoogleMap mGoogleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_exchange);
        onLoadExtra();
        initView(savedInstanceState);
        onLoadDetailExchange();
    }

    private void onLoadExtra() {
        mExchange = getIntent().getParcelableExtra(getString(R.string.extra_account));
        mExchangePlace = mExchange.getExchangePlace() != null ? mExchange.getExchangePlace() : new ExchangePlace();
    }

    private void initView(Bundle savedInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.toolbar_detail_exchange);
        toolbar.setNavigationIcon(R.drawable.ic_close_white);
        rlCategory = (RelativeLayout) findViewById(R.id.rlCategory);
        rlCategory.setOnClickListener(this);
        rlAmount = (RelativeLayout) findViewById(R.id.rlAmount);
        rlAmount.setOnClickListener(this);
        rlAccount = (RelativeLayout) findViewById(R.id.rlAccount);
        rlAccount.setOnClickListener(this);
        rlCurrency = (RelativeLayout) findViewById(R.id.rlCurrency);
        rlCurrency.setOnClickListener(this);
        rlDate = (RelativeLayout) findViewById(R.id.rlDate);
        rlDate.setOnClickListener(this);
        rlTime = (RelativeLayout) findViewById(R.id.rlTime);
        rlTime.setOnClickListener(this);
        rlLocation = (RelativeLayout) findViewById(R.id.rlLocation);
        rlLocation.setOnClickListener(this);
        tvCategory = (TextView) findViewById(R.id.tvCategoryName);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        tvAccount = (TextView) findViewById(R.id.tvAccount);
        mTvDescription = (TextView) findViewById(R.id.tvDescription);
        tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        imgLocation = (ImageView) findViewById(R.id.imgLocaiton);
        imgLocation.setOnClickListener(this);
        //Init Map
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_exchange, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.actionSave:
                onChangeExchange();
                finish();
                break;
            case R.id.actionDelete:
                //TODO
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlLocation:
            case R.id.imgLocaiton:
                onRequestPermissionMap();
                break;
        }
    }

    private void showDialogPickPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), RequestCode.PICK_PLACE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        DialogUtils.getInstance().showProgressDialog(this, "Loading");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.PICK_PLACE) {
            DialogUtils.getInstance().dismissProgressDialog();
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                onSetExchangePlace(place);
                updateMap();
            }
        }
    }

    private void onSetExchangePlace(Place place) {
        if (place == null) {
            return;
        }
        if (place.getName() != null) {
            mExchangePlace.setName(place.getName().toString());
        }
        if (place.getAddress() != null) {
            mExchangePlace.setAddress(place.getAddress().toString());
        }
        mExchangePlace.setLatitude(place.getLatLng().latitude);
        mExchangePlace.setLongitude(place.getLatLng().longitude);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestCode.PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showDialogPickPlace();
                }
            }

        }
    }

    public void onRequestPermissionMap() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(ActivityDetailExchange.this,
                                        new String[]{Manifest.permission
                                                .ACCESS_FINE_LOCATION},
                                        RequestCode.PERMISSION_LOCATION);
                            }
                        }).show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        RequestCode.PERMISSION_LOCATION);
            }
        } else {
            showDialogPickPlace();
        }
    }

    @Override
    public void onLoadDetailExchange() {
        if (mExchange == null) {
            return;
        }
        switch (mExchange.getTypeExchange()) {
            case ExchangeType.INCOME:
            case ExchangeType.EXPENSES:
                showDetailTypeIncomeAndExpenses();
            case ExchangeType.TRANSFER:
                //TODO
                break;
        }
        onLoadMap();
    }

    @Override
    public void onChangeExchange() {
        mExchange.setExchangePlace(mExchangePlace);
        ExchangeManger.getInstance().insertOrUpdate(mExchange);
        setResult(ResultCode.DETAIL_EXCHANGE);
    }

    private void showDetailTypeIncomeAndExpenses() {
        Category category = CategoryManager.getInstance().getCategoryById(mExchange.getIdCategory());
        tvCategory.setText(category.getName());
        tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyType(mExchange.getAmount(), mExchange.getCurrencyCode()));
        mTvDescription.setText(String.valueOf(mExchange.getDescription()));
        tvCurrency.setText(String.valueOf(mExchange.getCurrencyCode()));
        String nameAccount = AccountManager.getInstance().getAccountNameById(mExchange.getIdAccount());
        tvAccount.setText(String.valueOf(nameAccount));
        tvDate.setText(DateTimeUtils.getInstance().getStringDate(mExchange.getCreated()));
        tvTime.setText(DateTimeUtils.getInstance().getSortStringTime(mExchange.getCreated()));
    }

    private void onLoadMap() {
        mapView.getMapAsync(this);
    }

    private void onTargetLocationExchange() {
        LatLng sydney = new LatLng(mExchangePlace.getLatitude(), mExchangePlace.getLongitude());
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title(mExchangePlace.getAddress()));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f));
    }

    private void updateMap() {
        Log.d(TAG, "updateMap: " + mExchangePlace.getLatitude() + " - " + mExchangePlace.getLongitude());
        if (mGoogleMap == null) {
            return;
        }
        mGoogleMap.clear();
        onTargetLocationExchange();
        mapView.refreshDrawableState();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        onTargetLocationExchange();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
