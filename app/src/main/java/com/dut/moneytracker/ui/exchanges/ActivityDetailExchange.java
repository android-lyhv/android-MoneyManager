package com.dut.moneytracker.ui.exchanges;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.dialogs.DialogCalculator;
import com.dut.moneytracker.dialogs.DialogConfirm;
import com.dut.moneytracker.dialogs.DialogInput;
import com.dut.moneytracker.dialogs.DialogPickAccount;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.CategoryManager;
import com.dut.moneytracker.models.realms.DebitManager;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.ui.category.ActivityPickCategory_;
import com.dut.moneytracker.ui.interfaces.DetailExchangeListener;
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
 * Created by ly.ho on 12/03/2017.
 */
@EActivity(R.layout.activity_detail_exchange)
@OptionsMenu(R.menu.menu_detail_exchange)
public class ActivityDetailExchange extends AppCompatActivity implements DetailExchangeListener, OnMapReadyCallback {
    static final String TAG = ActivityDetailExchange.class.getSimpleName();
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.tvExchangeName)
    TextView tvExchangeName;
    @ViewById(R.id.tvCategoryName)
    TextView tvCategoryName;
    @ViewById(R.id.tvAmount)
    TextView tvAmount;
    @ViewById(R.id.tvAccount)
    TextView tvAccount;
    @ViewById(R.id.tvAccountName)
    TextView mTvDescription;
    @ViewById(R.id.tvDate)
    TextView tvDate;
    @ViewById(R.id.tvTime)
    TextView tvTime;
    @ViewById(R.id.tvTitleCategory)
    TextView mTvTitleCategory;
    @ViewById(R.id.rlCategory)
    RelativeLayout rlCategory;
    @ViewById(R.id.rlAccount)
    RelativeLayout rlAccount;
    @ViewById(R.id.tvTitleAccount)
    TextView tvTitleAccount;
    @ViewById(R.id.tvAddress)
    TextView tvAddress;
    @ViewById(R.id.imgEditAccount)
    ImageView imgEditAccount;
    @Extra
    Exchange mExchange;
    private TimePicker mTimePicker;
    private DayPicker mDayPicker;
    private DialogCalculator mDialogCalculator;
    private DialogPickAccount mDialogPickAccount;
    //GoogleMap
    private GoogleMap mGoogleMap;


    @AfterViews
    void init() {
        setTitle(R.string.toolbar_detail_exchange);
        toolbar.setNavigationIcon(R.drawable.ic_close_white);
        setSupportActionBar(toolbar);
        initDialog();
        initMap();
        onShowDetailExchange();
    }

    private void initDialog() {
        mTimePicker = TimePicker_.builder().build();
        mDayPicker = DayPicker_.builder().build();
        mDialogCalculator = DialogCalculator.getInstance();
        mDialogPickAccount = DialogPickAccount.getInstance();
    }

    private void initMap() {
        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    @OptionsItem(android.R.id.home)
    void onClose() {
        finish();
    }

    @OptionsItem(R.id.actionSave)
    void onClickSave() {
        onSaveChangeExchange();
    }

    @OptionsItem(R.id.actionDelete)
    void onClickDelete() {
        if (mExchange.getTypeExchange() == ExchangeType.DEBIT && TextUtils.equals(mExchange.getId(), String.valueOf(mExchange.getIdDebit()))) {
            Toast.makeText(this, R.string.messger_init_debit_exchange, Toast.LENGTH_SHORT).show();
            return;
        }
        DialogConfirm.getInstance().setMessage(getString(R.string.dialog_confirm_delete_title));
        DialogConfirm.getInstance().show(getSupportFragmentManager(), TAG);
        DialogConfirm.getInstance().registerClickListener(new DialogConfirm.ClickListener() {
            @Override
            public void onClickResult(boolean value) {
                if (value) {
                    setResult(IntentCode.DELETE_EXCHANGE);
                    finish();
                }
            }
        });
    }

    @Click(R.id.rlLocation)
    void onClickLocation() {
        onRequestPermissionMap();
    }

    @Click(R.id.rlCategory)
    void onClickPickCategory() {
        if (mExchange.getTypeExchange() == ExchangeType.TRANSFER) {
            mDialogPickAccount.registerPickAccount(new DialogPickAccount.AccountListener() {
                @Override
                public void onResultAccount(Account account) {
                    mExchange.setIdAccount(account.getId());
                    tvCategoryName.setText(account.getName());
                }
            }, false, null);
            mDialogPickAccount.show(getFragmentManager(), null);
        } else {
            ActivityPickCategory_.intent(this).mType(mExchange.getTypeExchange()).startForResult(IntentCode.PICK_CATEGORY);        }
    }

    @Click(R.id.rlAccount)
    void onCLickAccount() {
        if (mExchange.getTypeExchange() == ExchangeType.DEBIT || TextUtils.equals(mExchange.getIdAccountTransfer(), AccountManager.ID_OUTSIDE)) {
            return;
        }
        if (mExchange.getTypeExchange() == ExchangeType.TRANSFER) {
            mDialogPickAccount.registerPickAccount(new DialogPickAccount.AccountListener() {
                @Override
                public void onResultAccount(Account account) {
                    mExchange.setIdAccountTransfer(account.getId());
                    tvAccount.setText(account.getName());
                }
            }, false, null);
            mDialogPickAccount.show(getFragmentManager(), null);
        } else {
            mDialogPickAccount.registerPickAccount(new DialogPickAccount.AccountListener() {
                @Override
                public void onResultAccount(Account account) {
                    mExchange.setIdAccount(account.getId());
                    tvAccount.setText(account.getName());
                }
            }, false, mExchange.getIdAccount());
            mDialogPickAccount.show(getFragmentManager(), null);
        }
    }

    @Click(R.id.rlAmount)
    void onClickAmount() {
        if (mExchange.getTypeExchange() == ExchangeType.DEBIT && TextUtils.equals(mExchange.getId(), String.valueOf(mExchange.getIdDebit()))) {
            return;
        }
        String amount = mExchange.getAmount();
        if (amount.startsWith("-")) {
            amount = amount.substring(1);
        }
        mDialogCalculator.show(getFragmentManager(), null);
        mDialogCalculator.setAmount(amount);
        mDialogCalculator.registerResultListener(new DialogCalculator.ResultListener() {
            @Override
            public void onResult(String amount) {
                if (mExchange.getAmount().startsWith("-")) {
                    mExchange.setAmount("-" + amount);
                } else {
                    mExchange.setAmount(amount);
                }
                tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mExchange.getAmount(), CurrencyUtils.DEFAULT_CURRENCY_CODE));
            }
        });
    }

    @Click(R.id.rlDescription)
    void onClickDescription() {
        DialogInput.getInstance().register(new DialogInput.DescriptionListener() {
            @Override
            public void onResult(String content) {
                mTvDescription.setText(content);
                mExchange.setDescription(content);
            }
        });
        DialogInput.getInstance().show(getSupportFragmentManager(), null);
    }

    @Click(R.id.rlDate)
    void onClickDate() {
        mDayPicker.registerPicker(new DayPicker.DatePickerListener() {
            @Override
            public void onResultDate(Date date) {
                tvDate.setText(DateTimeUtils.getInstance().getStringFullDateVn(mExchange.getCreated()));
                mExchange.setCreated(date);
            }
        });
        mDayPicker.show(getSupportFragmentManager(), null);
    }

    @Click(R.id.rlTime)
    void onClickTime() {
        mTimePicker.registerPicker(new TimePicker.TimePickerListener() {
            @Override
            public void onResultHour(int hour) {
                Date date = mExchange.getCreated();
                Date newDate = DateTimeUtils.getInstance().setHours(date, hour);
                mExchange.setCreated(newDate);
            }

            @Override
            public void onResultMinute(int minute) {
                Date date = mExchange.getCreated();
                Date newDate = DateTimeUtils.getInstance().setMinute(date, minute);
                mExchange.setCreated(newDate);
            }

            @Override
            public void onResultStringTime(String time) {
                tvTime.setText(time);
            }
        });
        mTimePicker.show(getSupportFragmentManager(), null);
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
                updateMap();
            }
        }

        if (requestCode == IntentCode.PICK_CATEGORY) {
            if (resultCode == IntentCode.PICK_CATEGORY) {
                Category category = data.getParcelableExtra(getString(R.string.extra_category));
                String idCategory = category.getId();
                String nameCategory = category.getName();
                tvCategoryName.setText(nameCategory);
                mExchange.setIdCategory(idCategory);
            }
        }
    }

    void onSetExchangePlace(com.google.android.gms.location.places.Place place) {
        if (place == null) {
            return;
        }
        String address = String.format(Locale.US, "%s\n%s", place.getName() != null ? place.getName() : "", place.getAddress() != null ? place.getAddress() : "");
        mExchange.setAddress(address);
        mExchange.setLatitude(place.getLatLng().latitude);
        mExchange.setLongitude(place.getLatLng().longitude);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case IntentCode.PERMISSION_LOCATION: {
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

    @Override
    public void onShowDetailExchange() {
        switch (mExchange.getTypeExchange()) {
            case ExchangeType.INCOME:
            case ExchangeType.EXPENSES:
                showDetailTypeIncomeAndExpenses();
                break;
            case ExchangeType.DEBIT:
                showDetailExchangeDebit();
                break;
            case ExchangeType.TRANSFER:
                showDetailTypeTransfer();
                break;
        }
    }

    @Override
    public void onSaveChangeExchange() {
        if (mExchange.getTypeExchange() == ExchangeType.TRANSFER && TextUtils.equals(mExchange.getIdAccountTransfer(), mExchange.getIdAccount())) {
            Toast.makeText(this, R.string.different_account, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.extra_detail_exchange), mExchange);
        setResult(IntentCode.EDIT_EXCHANGE, intent);
        finish();
    }

    private void showDetailExchangeDebit() {
        rlCategory.setVisibility(View.GONE);
        tvCategoryName.setText(getString(R.string.debit_name));
        tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mExchange.getAmount(), CurrencyUtils.DEFAULT_CURRENCY_CODE));
        mTvDescription.setText(mExchange.getDescription());
        tvAccount.setText(DebitManager.getInstance().getAccountNameByDebitId(mExchange.getIdDebit()));
        tvDate.setText(DateTimeUtils.getInstance().getStringFullDateVn(mExchange.getCreated()));
        tvTime.setText(DateTimeUtils.getInstance().getStringTime(mExchange.getCreated()));
        if (!mExchange.getAmount().startsWith("-")) {
            tvAmount.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        tvExchangeName.setText(getString(R.string.debit_name));
    }

    private void showDetailTypeIncomeAndExpenses() {
        Category category = CategoryManager.getInstance().getCategoryById(mExchange.getIdCategory());
        tvCategoryName.setText(category.getName());
        tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mExchange.getAmount(), CurrencyUtils.DEFAULT_CURRENCY_CODE));
        mTvDescription.setText(mExchange.getDescription());
        String nameAccount = AccountManager.getInstance().getAccountNameById(mExchange.getIdAccount());
        tvAccount.setText(String.valueOf(nameAccount));
        tvDate.setText(DateTimeUtils.getInstance().getStringFullDateVn(mExchange.getCreated()));
        tvTime.setText(DateTimeUtils.getInstance().getStringTime(mExchange.getCreated()));
        if (!mExchange.getAmount().startsWith("-")) {
            tvAmount.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        if (mExchange.getTypeExchange() == ExchangeType.INCOME) {
            tvExchangeName.setText(R.string.income_name);
        } else {
            tvExchangeName.setText(R.string.expense_name);
        }
    }

    private void showDetailTypeTransfer() {
        tvExchangeName.setText(R.string.exchange_name_transfer);
        mTvTitleCategory.setText(R.string.account_send);
        tvTitleAccount.setText(R.string.account_receive);
        mTvDescription.setText(mExchange.getDescription());
        String amount = mExchange.getAmount();
        if (amount.startsWith("-")) {
            String accountSend = AccountManager.getInstance().getAccountNameById(mExchange.getIdAccount());
            tvCategoryName.setText(accountSend);
            String accountReceiver = AccountManager.getInstance().getAccountNameById(mExchange.getIdAccountTransfer());
            tvAccount.setText(accountReceiver);
        } else {
            String accountSend = AccountManager.getInstance().getAccountNameById(mExchange.getIdAccountTransfer());
            tvCategoryName.setText(accountSend);
            String accountReceiver = AccountManager.getInstance().getAccountNameById(mExchange.getIdAccount());
            tvAccount.setText(accountReceiver);
        }
        tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(mExchange.getAmount(), CurrencyUtils.DEFAULT_CURRENCY_CODE));
        tvDate.setText(DateTimeUtils.getInstance().getStringFullDateVn(mExchange.getCreated()));
        tvTime.setText(DateTimeUtils.getInstance().getStringTime(mExchange.getCreated()));
        if (!mExchange.getAmount().startsWith("-")) {
            tvAmount.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        } else {
            tvAmount.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
        }
    }

    private void onTargetLocationExchange() {
        LatLng sydney = new LatLng(mExchange.getLatitude(), mExchange.getLongitude());
        mGoogleMap.addMarker(new MarkerOptions().position(sydney));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, getResources().getInteger(R.integer.zoom_map)));
        tvAddress.setText(mExchange.getAddress());
    }

    private void updateMap() {
        mGoogleMap.clear();
        onTargetLocationExchange();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        onTargetLocationExchange();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleMap.clear();
    }
}
