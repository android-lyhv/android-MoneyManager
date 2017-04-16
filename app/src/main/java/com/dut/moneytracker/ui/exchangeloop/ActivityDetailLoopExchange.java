package com.dut.moneytracker.ui.exchangeloop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.dialogs.DialogCalculator;
import com.dut.moneytracker.dialogs.DialogCalculator_;
import com.dut.moneytracker.dialogs.DialogConfirm;
import com.dut.moneytracker.dialogs.DialogConfirm_;
import com.dut.moneytracker.dialogs.DialogInput;
import com.dut.moneytracker.dialogs.DialogInput_;
import com.dut.moneytracker.dialogs.DialogPickAccount;
import com.dut.moneytracker.dialogs.DialogPickAccount_;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.CategoryManager;
import com.dut.moneytracker.models.realms.ExchangeLoopManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.ExchangeLooper;
import com.dut.moneytracker.objects.Place;
import com.dut.moneytracker.service.GenerateManager;
import com.dut.moneytracker.ui.base.SpinnerTypeLoopManger;
import com.dut.moneytracker.ui.category.ActivityPickCategory;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.dut.moneytracker.utils.DialogUtils;
import com.dut.moneytracker.view.DayPicker;
import com.dut.moneytracker.view.DayPicker_;
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
 * Created by ly.ho on 19/03/2017.
 */
@EActivity(R.layout.activity_add_loop_exchange)
@OptionsMenu(R.menu.menu_detail_exchange)
public class ActivityDetailLoopExchange extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = ActivityDetailLoopExchange.class.getSimpleName();
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.tvTabIncome)
    TextView tvTabIncome;
    @ViewById(R.id.tvTabExpense)
    TextView tvTabExpense;
    @ViewById(R.id.tvTabTransfer)
    TextView tvTabTransfer;
    @ViewById(R.id.tvCategoryName)
    TextView tvCategoryName;
    @ViewById(R.id.tvAmount)
    TextView mTvAmount;
    @ViewById(R.id.tvAccountName)
    TextView tvDescription;
    @ViewById(R.id.tvDate)
    TextView tvDate;
    @ViewById(R.id.tvAccount)
    TextView tvAccount;
    @ViewById(R.id.spinnerTypeLoop)
    AppCompatSpinner mAppCompatSpinner;
    @ViewById(R.id.switchLoop)
    SwitchCompat switchCompat;
    @Extra
    ExchangeLooper mExchangeLoop;
    private GenerateManager mGenerateManager;
    private GoogleMap mGoogleMap;
    private Place mPlace;
    private int mTypeExchange;
    private int lastTypeLoop = -1;
    private Date lastDate;
    private boolean lastStatus;
    private SpinnerTypeLoopManger mSpinnerTypeLoopManger;
    private DialogCalculator mDialogCalculator;

    @AfterViews
    void init() {
        mDialogCalculator = DialogCalculator_.builder().build();
        initToolbar();
        initSpinner();
        onShowData();
        initMap();
    }

    private void onShowData() {
        mGenerateManager = new GenerateManager(this);
        lastTypeLoop = mExchangeLoop.getTypeLoop();
        lastDate = mExchangeLoop.getCreated();
        lastStatus = mExchangeLoop.isLoop();
        mPlace = mExchangeLoop.getPlace();
        mTypeExchange = mExchangeLoop.getTypeExchange();
        switchCompat.setChecked(mExchangeLoop.isLoop());
        mSpinnerTypeLoopManger.setSelectItem(mExchangeLoop.getTypeLoop());
        if (mExchangeLoop.getTypeExchange() == ExchangeType.INCOME || mExchangeLoop.getTypeExchange() == ExchangeType.EXPENSES) {
            Category category = CategoryManager.getInstance().getCategoryById(mExchangeLoop.getIdCategory());
            tvCategoryName.setText(category.getName());
        }
        mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mExchangeLoop.getAmount(), mExchangeLoop.getCurrencyCode()));
        switch (mExchangeLoop.getTypeExchange()) {
            case ExchangeType.INCOME:
                onClickTabIncome();
                break;
            case ExchangeType.EXPENSES:
                onClickTabExpense();
                break;
            case ExchangeType.TRANSFER:
                onClickTransfer();
                break;
        }
        tvDescription.setText(mExchangeLoop.getDescription());
        tvAccount.setText(AccountManager.getInstance().getAccountNameById(mExchangeLoop.getIdAccount()));
        tvDate.setText(DateTimeUtils.getInstance().getStringDateUs(mExchangeLoop.getCreated()));
    }

    private void initSpinner() {
        mSpinnerTypeLoopManger = new SpinnerTypeLoopManger(this, mAppCompatSpinner);
        mSpinnerTypeLoopManger.registerSelectedItem(new SpinnerTypeLoopManger.ItemSelectedListener() {
            @Override
            public void onResultTypeLoop(int type) {
                mExchangeLoop.setTypeLoop(type);
            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mExchangeLoop.setLoop(isChecked);
            }
        });
    }

    private void initMap() {
        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white);
    }

    @OptionsItem(android.R.id.home)
    void onClickHomeBack() {
        finish();
    }

    @OptionsItem(R.id.actionSave)
    void onClickSave() {
        if (TextUtils.isEmpty(mExchangeLoop.getAmount())) {
            Toast.makeText(this, R.string.input_money, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mExchangeLoop.getIdAccount())) {
            Toast.makeText(this, R.string.input_account, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mExchangeLoop.getIdCategory())) {
            Toast.makeText(this, getString(R.string.input_category), Toast.LENGTH_SHORT).show();
            return;
        }
        mExchangeLoop.setPlace(mPlace);
        mExchangeLoop.setTypeExchange(mTypeExchange);
        onSaveDataBase();
        finish();
    }

    @OptionsItem(R.id.actionDelete)
    void onClickDelete() {
        DialogConfirm dialogConfirm = DialogConfirm_.builder().build();
        dialogConfirm.setMessage(getString(R.string.delete_exchange));
        dialogConfirm.registerClickListener(new DialogConfirm.ClickListener() {
            @Override
            public void onClickResult(boolean value) {
                if (value) {
                    ExchangeLoopManager.getInstance(getApplicationContext()).deleteExchangeLoopById(mExchangeLoop.getId());
                    finish();
                }
            }
        });
        dialogConfirm.show(getSupportFragmentManager(), null);
    }

    /**
     * Handler pending loop exchange and save database
     */
    private void onSaveDataBase() {
        if ((lastTypeLoop != mExchangeLoop.getTypeLoop() || !DateTimeUtils.getInstance().isSameDate(lastDate, mExchangeLoop.getCreated()))) {
            ExchangeLoopManager.getInstance(getApplicationContext()).upDatePendingExchange(mExchangeLoop);
            return;
        }
        if (!lastStatus && mExchangeLoop.isLoop()) {
            Log.d(TAG, "onSaveDataBase: 1");
            ExchangeLoopManager.getInstance(getApplicationContext()).upDatePendingExchange(mExchangeLoop);
            return;
        }
        if (lastStatus && !mExchangeLoop.isLoop()) {
            Log.d(TAG, "onSaveDataBase: 2");
            mGenerateManager.removePendingLoopExchange(mExchangeLoop.getId());
        }
        ExchangeLoopManager.getInstance(getApplicationContext()).insertOrUpdate(mExchangeLoop);
    }

    @Click(R.id.tvTabIncome)
    void onClickTabIncome() {
        mTypeExchange = ExchangeType.INCOME;
        mTvAmount.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tvTabIncome.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        tvTabIncome.setBackgroundResource(R.color.colorPrimary);
        tvTabExpense.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tvTabExpense.setBackgroundColor(ContextCompat.getColor(this, R.color.color_background_tab_unselect));
        tvTabTransfer.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tvTabTransfer.setBackgroundColor(ContextCompat.getColor(this, R.color.color_background_tab_unselect));
        if (mExchangeLoop.getAmount().startsWith("-")) {
            mExchangeLoop.setAmount(mExchangeLoop.getAmount().substring(1));
        }
        mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mExchangeLoop.getAmount(), "VND"));
    }

    @Click(R.id.tvTabExpense)
    void onClickTabExpense() {
        mTypeExchange = ExchangeType.EXPENSES;
        mTvAmount.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
        tvTabExpense.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        tvTabExpense.setBackgroundResource(R.color.colorPrimary);
        tvTabIncome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tvTabIncome.setBackgroundColor(ContextCompat.getColor(this, R.color.color_background_tab_unselect));
        tvTabTransfer.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tvTabTransfer.setBackgroundColor(ContextCompat.getColor(this, R.color.color_background_tab_unselect));
        if (!mExchangeLoop.getAmount().startsWith("-")) {
            mExchangeLoop.setAmount(String.format(Locale.US, "-%s", mExchangeLoop.getAmount()));
        }
        Log.d(TAG, "onClickTabExpense: " + mExchangeLoop.getAmount());
        mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mExchangeLoop.getAmount(), "VND"));
    }

    @Click(R.id.tvTabTransfer)
    void onClickTransfer() {
        mTypeExchange = ExchangeType.TRANSFER;
        mTvAmount.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
        tvTabTransfer.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        tvTabTransfer.setBackgroundResource(R.color.colorPrimary);
        tvTabExpense.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tvTabExpense.setBackgroundColor(ContextCompat.getColor(this, R.color.color_background_tab_unselect));
        tvTabIncome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tvTabIncome.setBackgroundColor(ContextCompat.getColor(this, R.color.color_background_tab_unselect));
        if (!mExchangeLoop.getAmount().startsWith("-")) {
            mExchangeLoop.setAmount(String.format(Locale.US, "-%s", mExchangeLoop.getAmount()));
        }
        Log.d(TAG, "onClickTabExpense: " + mExchangeLoop.getAmount());
        mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mExchangeLoop.getAmount(), "VND"));
    }

    @Click(R.id.rlCategory)
    void onClickCategory() {
        startActivityForResult(new Intent(this, ActivityPickCategory.class), IntentCode.PICK_CATEGORY);
    }

    @Click(R.id.rlAmount)
    void onCLickAmount() {
        String amount = mExchangeLoop.getAmount() == null ? "" : mExchangeLoop.getAmount();
        if (amount.startsWith("-")) {
            amount = amount.substring(1);
        }
        mDialogCalculator.show(getFragmentManager(), null);
        mDialogCalculator.setAmount(amount);
        mDialogCalculator.registerResultListener(new DialogCalculator.ResultListener() {
            @Override
            public void onResult(String amount) {
                if (mTypeExchange == ExchangeType.INCOME) {
                    mExchangeLoop.setAmount(amount);
                } else {
                    mExchangeLoop.setAmount(String.format(Locale.US, "-%s", amount));
                }
                mTvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mExchangeLoop.getAmount(), "VND"));
            }
        });
    }

    @Click(R.id.rlDescription)
    void onCLickDescription() {
        DialogInput dialogInput = DialogInput_.builder().build();
        dialogInput.register(new DialogInput.DescriptionListener() {
            @Override
            public void onResult(String content) {
                mExchangeLoop.setDescription(content);
                tvDescription.setText(content);
            }
        });
        dialogInput.show(getSupportFragmentManager(), null);
    }

    @Click(R.id.rlAccount)
    void onCLickAccount() {
        DialogPickAccount dialogPickAccount = DialogPickAccount_.builder().build();
        dialogPickAccount.registerPickAccount(new DialogPickAccount.AccountListener() {
            @Override
            public void onResultAccount(Account account) {
                mExchangeLoop.setIdAccount(account.getId());
                tvAccount.setText(account.getName());
            }
        }, false);
        dialogPickAccount.show(getFragmentManager(), null);
    }

    @Click(R.id.rlDate)
    void onClickDate() {
        DayPicker dayPicker = DayPicker_.builder().build();
        dayPicker.registerPicker(new DayPicker.DatePickerListener() {
            @Override
            public void onResultDate(Date date) {
                mExchangeLoop.setCreated(date);
                tvDate.setText(DateTimeUtils.getInstance().getStringDateUs(date));
            }
        });
        dayPicker.show(getSupportFragmentManager(), null);
    }

    @Click(R.id.rlLocation)
    void onClickLocation() {
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
                                ActivityCompat.requestPermissions(ActivityDetailLoopExchange.this,
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
        if (requestCode == IntentCode.PICK_CATEGORY) {
            if (resultCode == IntentCode.PICK_CATEGORY) {
                Category category = data.getParcelableExtra(getString(R.string.extra_category));
                String idCategory = category.getId();
                String nameCategory = category.getName();
                tvCategoryName.setText(nameCategory);
                mExchangeLoop.setIdCategory(idCategory);
            }
        }
        if (requestCode == IntentCode.PICK_PLACE) {
            DialogUtils.getInstance().dismissProgressDialog();
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);
                onSetExchangePlace(place);
                updateMap();
            }
        }
    }

    private void updateMap() {
        mGoogleMap.clear();
        onTargetLocationExchange();
    }

    private void onTargetLocationExchange() {
        if (mPlace == null) {
            return;
        }
        LatLng sydney = new LatLng(mPlace.getLatitude(), mPlace.getLongitude());
        String title = String.format(Locale.US, "%s,%s", mPlace.getName(), mPlace.getName());
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title(title));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, getResources().getInteger(R.integer.zoom_map)));
    }

    private void onSetExchangePlace(com.google.android.gms.location.places.Place place) {
        mPlace = new Place();
        if (place.getName() != null) {
            mPlace.setName(place.getName().toString());
        }
        if (place.getAddress() != null) {
            mPlace.setAddress(place.getAddress().toString());
        }
        mPlace.setLatitude(place.getLatLng().latitude);
        mPlace.setLongitude(place.getLatLng().longitude);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        onTargetLocationExchange();
    }
}
