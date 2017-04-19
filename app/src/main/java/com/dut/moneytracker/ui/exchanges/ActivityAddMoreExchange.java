package com.dut.moneytracker.ui.exchanges;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.dut.moneytracker.utils.DialogUtils;
import com.dut.moneytracker.view.DayPicker;
import com.dut.moneytracker.view.DayPicker_;
import com.dut.moneytracker.view.TimePicker;
import com.dut.moneytracker.view.TimePicker_;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.Locale;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 17/03/2017.
 */
@EActivity(R.layout.activity_add_more_exchange)
@OptionsMenu(R.menu.menu_add_exchange)
public class ActivityAddMoreExchange extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = ActivityAddMoreExchange.class.getSimpleName();
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.edtDescription)
    EditText editDescription;
    @ViewById(R.id.tvDate)
    TextView tvDate;
    @ViewById(R.id.tvTime)
    TextView tvTime;
    @ViewById(R.id.tvAmount)
    TextView tvAmount;
    @ViewById(R.id.tvAddress)
    TextView tvAddress;
    @Extra
    Exchange mExchange;
    private DayPicker mDayPicker;
    private TimePicker mTimePicker;
    private Date mDate;
    private GoogleMap mGoogleMap;

    @AfterViews
    void init() {
        initData();
        initView();
        initMap();
    }

    private void initMap() {
        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    private void initData() {
        if (null != mExchange.getCreated()) {
            mDate = mExchange.getCreated();
        } else {
            mDate = new Date();
        }
    }

    @OptionsItem(android.R.id.home)
    void onClose() {
        finish();
    }

    @OptionsItem(R.id.actionAdd)
    void onSave() {
        mExchange.setDescription(editDescription.getText().toString());
        mExchange.setCreated(mDate);
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.extra_more_add), mExchange);
        setResult(IntentCode.MORE_ADD, intent);
        finish();
    }

    void initView() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mExchange.getAmount(), "VND"));
        tvDate.setText(DateTimeUtils.getInstance().getStringFullDateVn(mDate));
        tvTime.setText(DateTimeUtils.getInstance().getStringTime(mDate));
        editDescription.setText(null == mExchange.getDescription() ? "" : mExchange.getDescription());
        mDayPicker = DayPicker_.builder().build();
        mDayPicker.registerPicker(new DayPicker.DatePickerListener() {

            @Override
            public void onResultDate(Date date) {
                mDate = date;
                tvDate.setText(DateTimeUtils.getInstance().getStringFullDateVn(mDate));
            }
        });
        mTimePicker = TimePicker_.builder().build();
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
                tvTime.setText(time);
            }
        });
    }

    @Click(R.id.tvDate)
    void onClickPickDay() {
        mDayPicker.show(getSupportFragmentManager(), TAG);
    }

    @Click(R.id.tvTime)
    void onClickPickTime() {
        mTimePicker.show(getSupportFragmentManager(), TAG);
    }

    @Click(R.id.rlLocation)
    void onCLickPickPlace() {
        onRequestPermissionMap();
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
                                        IntentCode.PERMISSION_LOCATION);
                            }
                        }).show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        IntentCode.PERMISSION_LOCATION);
            }
        } else {
            showDialogPickPlace();
        }
    }

    private void showDialogPickPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), IntentCode.PICK_PLACE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        DialogUtils.getInstance().showProgressDialog(this, "Loading");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCode.PICK_PLACE) {
            DialogUtils.getInstance().dismissProgressDialog();
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);
                onSetExchangePlace(place);
                onLoadMaker();
            }
        }
    }

    private void onSetExchangePlace(com.google.android.gms.location.places.Place place) {
        if (place == null) {
            return;
        }
        String address = String.format(Locale.US, "%s\n%s", place.getName() != null ? place.getName() : "", place.getAddress() != null ? place.getAddress() : "");
        mExchange.setAddress(address);
        mExchange.setLatitude(place.getLatLng().latitude);
        mExchange.setLongitude(place.getLatLng().longitude);
    }

    private void onLoadMaker() {
        mGoogleMap.clear();
        onTargetLocation();
    }

    private void onTargetLocation() {
        LatLng sydney = new LatLng(mExchange.getLatitude(), mExchange.getLongitude());
        mGoogleMap.addMarker(new MarkerOptions().position(sydney));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, getResources().getInteger(R.integer.zoom_map)));
        tvAddress.setText(mExchange.getAddress());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        onTargetLocation();
    }
}
