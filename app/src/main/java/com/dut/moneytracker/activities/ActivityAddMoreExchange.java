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
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
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
import com.dut.moneytracker.objects.Place;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.dut.moneytracker.utils.DialogUtils;
import com.dut.moneytracker.view.DayPicker;
import com.dut.moneytracker.view.TimePicker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

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
    private Place mPlace;
    private Date mDate;
    private MapView mapView;
    private AppCompatSpinner mAppCompatSpinner;
    private SpinnerTypeLoopManger spinnerTypeLoopManger;
    //Google Map
    GoogleMap mGoogleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.fragment_detail_add);
        setInData();
        initView();
        //Init Map
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void setInData() {
        mExchange = getIntent().getParcelableExtra(getString(R.string.extra_more_add));
        mDate = new Date();
        if (null != mExchange.getCreated()) {
            mDate = mExchange.getCreated();
        }
        if (null != mExchange.getPlace()) {
            mPlace = mExchange.getPlace();
        } else {
            mPlace = new Place();
        }
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
                onSaveAddMoreExchange();
                break;
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSaveAddMoreExchange() {
        String description = mEditDescription.getText().toString();
        mExchange.setDescription(String.valueOf(description));
        mExchange.setPlace(mPlace);
        mExchange.setCreated(mDate);
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.extra_more_add), mExchange);
        setResult(RequestCode.MORE_ADD, intent);
        finish();
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
        tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyType(mExchange.getAmount(), "VND"));
        mTvDate.setText(DateTimeUtils.getInstance().getStringFullDate(mDate));
        mTvTime.setText(DateTimeUtils.getInstance().getStringTime(mDate));
        mAppCompatSpinner = (AppCompatSpinner) findViewById(R.id.spinnerTypeLoop);
        mDayPicker = new DayPicker();
        mEditDescription.setText(null == mExchange.getDescription() ? "" : mExchange.getDescription());
        mDayPicker.registerPicker(new DayPicker.DatePickerListener() {

            @Override
            public void onResultDate(Date date) {
                mDate = date;
                mTvDate.setText(DateTimeUtils.getInstance().getStringFullDate(mDate));
            }
        });
        mTimePicker = new TimePicker();
        mTimePicker.registerPicker(new TimePicker.TimePickerListener() {
            @Override
            public void onResultHour(int hour) {
                mDate = DateTimeUtils.getInstance().setHours(mDate, hour);
            }

            @Override
            public void onResultMinute(int minute) {
                mDate = DateTimeUtils.getInstance().setMinute(mDate, minute);
            }

            @Override
            public void onResultStringTime(String time) {
                mTvTime.setText(time);
            }
        });
        spinnerTypeLoopManger = new SpinnerTypeLoopManger(this, mAppCompatSpinner);
        spinnerTypeLoopManger.registerSelectedItem(new SpinnerTypeLoopManger.ItemSelectedListener() {
            @Override
            public void onResultTypeLoop(int type) {
                mExchange.setTypeLoop(type);
            }
        });
        spinnerTypeLoopManger.setSelectItem(mExchange.getTypeLoop());
        mSwitchCompat.setChecked(mExchange.isLoop());
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
        mExchange.setLoop(isChecked);
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
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);
                onSetExchangePlace(place);
                updateMap();
            }
        }
    }

    private void onSetExchangePlace(com.google.android.gms.location.places.Place place) {
        if (place.getName() != null) {
            mPlace.setName(place.getName().toString());
        }
        if (place.getAddress() != null) {
            mPlace.setAddress(place.getAddress().toString());
        }
        mPlace.setLatitude(place.getLatLng().latitude);
        mPlace.setLongitude(place.getLatLng().longitude);
    }

    private void updateMap() {
        mGoogleMap.clear();
        onTargetLocationExchange();
        mapView.refreshDrawableState();
    }

    private void onTargetLocationExchange() {
        LatLng sydney = new LatLng(mPlace.getLatitude(), mPlace.getLongitude());
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title(mPlace.getAddress()));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        onTargetLocationExchange();
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
