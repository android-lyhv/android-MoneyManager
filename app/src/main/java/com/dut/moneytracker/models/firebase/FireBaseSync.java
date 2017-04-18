package com.dut.moneytracker.models.firebase;

import android.content.Context;
import android.util.Log;

import com.dut.moneytracker.models.AppConfig;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Debit;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.ExchangeLooper;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 03/03/2017.
 */

public class FireBaseSync {
    private static final String TAG = FireBaseSync.class.getSimpleName();
    private static FireBaseSync fireBaseSync;
    private static final String CHILD_ACCOUNT = "account";
    private static final String CHILD_EXCHANGE = "exchange";
    private static final String CHILD_EXCHANGE_LOOP = "exchange_loop";
    private static final String CHILD_DEBIT = "debit";

    public static FireBaseSync getInstance() {
        if (fireBaseSync == null) {
            fireBaseSync = new FireBaseSync();
        }
        return fireBaseSync;
    }

    private FireBaseSync() {

    }


    public void upDateAccount(Context context, Account account) {
        String reference = AppConfig.getInstance().getReferenceDatabase(context);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(reference);
        String path = String.format(Locale.US, "/%s/%s", mDatabase.child(CHILD_ACCOUNT).getKey(), account.getId());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(path, toMapAccount(account));
        mDatabase.updateChildren(childUpdates);
    }

    public void upDateExchange(Context context, Exchange exchange) {
        String reference = AppConfig.getInstance().getReferenceDatabase(context);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(reference);
        String path = String.format(Locale.US, "/%s/%s", mDatabase.child(CHILD_EXCHANGE).getKey(), exchange.getId());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(path, toMapExchange(exchange));
        mDatabase.updateChildren(childUpdates);
    }

    public void upDateExchangeLoop(Context context, ExchangeLooper exchangeLooper) {
        String reference = AppConfig.getInstance().getReferenceDatabase(context);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(reference);
        String path = String.format(Locale.US, "/%s/%s", mDatabase.child(CHILD_EXCHANGE_LOOP).getKey(), exchangeLooper.getId());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(path, toMapExchangeLoop(exchangeLooper));
        mDatabase.updateChildren(childUpdates);
    }

    public void upDateDebit(Context context, Debit debit) {
        String reference = AppConfig.getInstance().getReferenceDatabase(context);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(reference);
        String path = String.format(Locale.US, "/%s/%s", mDatabase.child(CHILD_DEBIT).getKey(), debit.getId());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(path, toMapDebit(debit));
        mDatabase.updateChildren(childUpdates);
    }

    public void deleteAccount(Context context, String id) {
        String reference = AppConfig.getInstance().getReferenceDatabase(context);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(reference);
        mDatabase.child(CHILD_ACCOUNT).child(id).removeValue();
    }

    public void deleteExchange(Context context, String id) {
        String reference = AppConfig.getInstance().getReferenceDatabase(context);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(reference);
        mDatabase.child(CHILD_EXCHANGE).child(id).removeValue();
    }

    public void deleteExchangeLoop(Context context, int id) {
        String reference = AppConfig.getInstance().getReferenceDatabase(context);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(reference);
        mDatabase.child(CHILD_EXCHANGE_LOOP).child(String.valueOf(id)).removeValue();
    }

    public void deleteDebit(Context context, int id) {
        String reference = AppConfig.getInstance().getReferenceDatabase(context);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(reference);
        mDatabase.child(CHILD_DEBIT).child(String.valueOf(id)).removeValue();
    }

    public void onLoadDataServer(Context context) {
        String reference = AppConfig.getInstance().getReferenceDatabase(context);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(reference);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.child(CHILD_ACCOUNT).getChildren()) {
                    Log.d(TAG, "onDataChange: " + messageSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Map<String, Object> toMapExchange(Exchange exchange) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("typeExchange", exchange.getTypeExchange());
        result.put("idAccount", exchange.getIdAccount());
        result.put("idCategory", exchange.getIdCategory());
        result.put("idDebit", exchange.getIdDebit());
        result.put("idAccountTransfer", exchange.getIdAccountTransfer());
        result.put("codeTransfer", exchange.getCodeTransfer());
        result.put("amount", exchange.getAmount());
        result.put("description", exchange.getDescription());
        result.put("created", DateTimeUtils.getInstance().getStringFullDateUs(exchange.getCreated()));
        result.put("address", exchange.getAddress());
        result.put("latitude", exchange.getLatitude());
        result.put("longitude", exchange.getLongitude());
        return result;
    }

    private Map<String, Object> toMapAccount(Account account) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", account.getName());
        result.put("initAmount", account.getInitAmount());
        result.put("created", DateTimeUtils.getInstance().getStringFullDateUs(account.getCreated()));
        result.put("colorHex", account.getColorHex());
        result.put("saveLocation", account.isSaveLocation());
        result.put("isDefault", account.isDefault());
        return result;
    }

    private Map<String, Object> toMapDebit(Debit debit) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", debit.getName());
        result.put("amount", debit.getAmount());
        result.put("idAccount", debit.getIdAccount());
        result.put("typeDebit", debit.getTypeDebit());
        result.put("isClose", debit.isClose());
        result.put("startDate", DateTimeUtils.getInstance().getStringFullDateUs(debit.getStartDate()));
        result.put("endDate", DateTimeUtils.getInstance().getStringFullDateUs(debit.getEndDate()));
        result.put("description", debit.getDescription());
        return result;
    }

    private Map<String, Object> toMapExchangeLoop(ExchangeLooper exchangeLooper) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("typeExchange", exchangeLooper.getTypeExchange());
        result.put("idAccount", exchangeLooper.getIdAccount());
        result.put("idCategory", exchangeLooper.getIdCategory());
        result.put("idAccountTransfer", exchangeLooper.getIdAccountTransfer());
        result.put("codeTransfer", exchangeLooper.getCodeTransfer());
        result.put("amount", exchangeLooper.getAmount());
        result.put("description", exchangeLooper.getDescription());
        result.put("created", DateTimeUtils.getInstance().getStringFullDateUs(exchangeLooper.getCreated()));
        result.put("address", exchangeLooper.getAddress());
        result.put("latitude", exchangeLooper.getLatitude());
        result.put("longitude", exchangeLooper.getLongitude());
        result.put("isLoop", exchangeLooper.isLoop());
        result.put("typeLoop", exchangeLooper.getTypeLoop());
        return result;
    }
}
