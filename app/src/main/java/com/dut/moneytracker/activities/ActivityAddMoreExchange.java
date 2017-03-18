package com.dut.moneytracker.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.RequestCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.ExchangePlace;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.dut.moneytracker.utils.DialogUtils;
import com.dut.moneytracker.view.DayPicker;
import com.dut.moneytracker.view.TimePicker;
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

import java.util.Calendar;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 17/03/2017.
 */

public class ActivityAddMoreExchange extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, OnMapReadyCallback {
    private static final String TAG = ActivityAddMoreExchange.class.getSimpleName();
    private EditText mEditDescription;
    private TextView mTvDate;
    private TextView mTvTime;
    private RelativeLayout rlLocation;
    private TextView tvAmount;
    private DayPicker mDayPicker;
    private TimePicker mTimePicker;
    private SwitchCompat mSwitchCompat;
    private Exchange mExchange;
    private ExchangePlace mExchangePlace = new ExchangePlace();
    private Calendar mCalendar;
    private MapView mapView;
    //Google Map
    GoogleMap mGoogleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.fragment_detail_add);
        initView();
        setInView();
        //Init Map
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(this);
        mapView.getMapAsync(this);
    }

    private void setInView() {
        mExchange = getIntent().getParcelableExtra(getString(R.string.extra_more_add));
        mCalendar = Calendar.getInstance();
        if (null != mExchange.getCreated()) {
            mCalendar.setTime(mExchange.getCreated());
        }
        mExchangePlace = mExchange.getExchangePlace();
        tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyType(mExchange.getAmount(), "VND"));
        mTvDate.setText(DateTimeUtils.getInstance().getStringFullDate(mCalendar.getTime()));
        mTvTime.setText(DateTimeUtils.getInstance().getStringTime(mCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_exchange, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAdd:
                onAddMoreExchange();
                break;
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onAddMoreExchange() {
        String description = mEditDescription.getText().toString();
        if (!TextUtils.isEmpty(description)) {
            mExchange.setDescription(description);
        }
        mExchange.setExchangePlace(mExchangePlace);
        mExchange.setCreated(mCalendar.getTime());
    }

    void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mEditDescription = (EditText) findViewById(R.id.tvDescription);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        mTvDate = (TextView) findViewById(R.id.tvDate);
        mTvDate.setOnClickListener(this);
        mTvTime = (TextView) findViewById(R.id.tvTime);
        mTvTime.setOnClickListener(this);
        rlLocation = (RelativeLayout) findViewById(R.id.rlLocation);
        rlLocation.setOnClickListener(this);
        mSwitchCompat = (SwitchCompat) findViewById(R.id.switchLoop);
        mSwitchCompat.setOnCheckedChangeListener(this);
        mDayPicker = new DayPicker();
        mDayPicker.registerPicker(new DayPicker.DatePickerListener() {
            @Override
            public void onResultYear(int year) {
                mCalendar.set(Calendar.YEAR, year);
            }

            @Override
            public void onResultMonthOfYear(int month) {
                mCalendar.set(Calendar.MONTH, month);
            }

            @Override
            public void onResultDayOfMonth(int day) {
                mCalendar.set(Calendar.DAY_OF_MONTH, day);
            }

            @Override
            public void onResultStringDate(String date) {
                mTvDate.setText(date);
            }
        });
        mTimePicker = new TimePicker();
        mTimePicker.registerPicker(new TimePicker.TimePickerListener() {
            @Override
            public void onResultHour(int hour) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            }

            @Override
            public void onResultMinute(int minute) {
                mCalendar.set(Calendar.MINUTE, minute);
            }

            @Override
            public void onResultStringTime(String time) {
                mTvTime.setText(time);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDescription:
                break;
            case R.id.tvDate:
                mDayPicker.show(getSupportFragmentManager(), TAG);
                break;
            case R.id.tvTime:
                mTimePicker.show(getSupportFragmentManager(), TAG);
                break;
            case R.id.rlLocation:
                onRequestPermissionMap();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

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
                                ActivityCompat.requestPermissions(ActivityAddMoreExchange.this,
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

    private void updateMap() {
        Log.d(TAG, "updateMap: " + mExchangePlace.getLatitude() + " - " + mExchangePlace.getLongitude());
        if (mGoogleMap == null) {
            return;
        }
        mGoogleMap.clear();
        onTargetLocationExchange();
        mapView.refreshDrawableState();
    }

    private void onTargetLocationExchange() {
        LatLng sydney = new LatLng(mExchangePlace.getLatitude(), mExchangePlace.getLongitude());
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title(mExchangePlace.getAddress()));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        onTargetLocationExchange();
    }
}
