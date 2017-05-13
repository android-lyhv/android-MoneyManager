package com.dut.moneytracker.models.firebase;

import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.DebitManager;
import com.dut.moneytracker.models.realms.ExchangeLoopManager;
import com.dut.moneytracker.models.realms.ExchangeManger;
import com.dut.moneytracker.objects.Account;
import com.dut.moneytracker.objects.Debit;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.ExchangeLooper;
import com.dut.moneytracker.utils.DateTimeUtils;
import com.google.firebase.auth.FirebaseAuth;
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
    private static FireBaseSync fireBaseSync;
    private static final String CHILD_ACCOUNT = "account";
    private static final String CHILD_EXCHANGE = "exchange";
    private static final String CHILD_EXCHANGE_LOOP = "exchange_loop";
    private static final String CHILD_DEBIT = "debit";
    private static DatabaseReference mDatabaseReference;

    private FireBaseSync() {
        // No-op
    }

    public static FireBaseSync getInstance() {
        if (fireBaseSync == null) {
            fireBaseSync = new FireBaseSync();
        }
        initDataReference();
        return fireBaseSync;
    }

    private static void initDataReference() {
        String reference = String.format(Locale.US, "/users/%s/", FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(reference);
        mDatabaseReference.keepSynced(true);
    }


    public void upDateAccount(Account account) {
        String path = String.format(Locale.US, "/%s/%s", mDatabaseReference.child(CHILD_ACCOUNT).getKey(), account.getId());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(path, toMapAccount(account));
        mDatabaseReference.updateChildren(childUpdates);
    }

    public void upDateExchange(Exchange exchange) {
        String path = String.format(Locale.US, "/%s/%s", mDatabaseReference.child(CHILD_EXCHANGE).getKey(), exchange.getId());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(path, toMapExchange(exchange));
        mDatabaseReference.updateChildren(childUpdates);
    }

    public void upDateExchangeLoop(ExchangeLooper exchangeLooper) {
        String path = String.format(Locale.US, "/%s/%s", mDatabaseReference.child(CHILD_EXCHANGE_LOOP).getKey(), exchangeLooper.getId());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(path, toMapExchangeLoop(exchangeLooper));
        mDatabaseReference.updateChildren(childUpdates);
    }

    public void upDateDebit(Debit debit) {
        String path = String.format(Locale.US, "/%s/%s", mDatabaseReference.child(CHILD_DEBIT).getKey(), debit.getId());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(path, toMapDebit(debit));
        mDatabaseReference.updateChildren(childUpdates);
    }

    public void deleteAccount(String id) {
        mDatabaseReference.child(CHILD_ACCOUNT).child(id).removeValue();
    }

    public void deleteExchange(String id) {
        mDatabaseReference.child(CHILD_EXCHANGE).child(id).removeValue();
    }

    public void deleteExchangeLoop(int id) {
        mDatabaseReference.child(CHILD_EXCHANGE_LOOP).child(String.valueOf(id)).removeValue();
    }

    public void deleteDebit(int id) {
        mDatabaseReference.child(CHILD_DEBIT).child(String.valueOf(id)).removeValue();
    }

    public void onLoadDataServer(final LoadDataListener loadDataListener) {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onLoadAccount(dataSnapshot.child(CHILD_ACCOUNT));
                onLoadExchange(dataSnapshot.child(CHILD_EXCHANGE));
                onLoadDebit(dataSnapshot.child(CHILD_DEBIT));
                onLoadExchangeLoop(dataSnapshot.child(CHILD_EXCHANGE_LOOP));
                loadDataListener.onFinishLoadDataServer();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onLoadAccount(DataSnapshot dataSnapshot) {
        for (DataSnapshot result : dataSnapshot.getChildren()) {
            String id = result.getKey();
            HashMap<String, Object> data = (HashMap<String, Object>) result.getValue();
            onSaveAccount(id, data);
        }
    }

    private void onLoadExchange(DataSnapshot dataSnapshot) {
        for (DataSnapshot result : dataSnapshot.getChildren()) {
            String id = result.getKey();
            HashMap<String, Object> data = (HashMap<String, Object>) result.getValue();
            onSaveExchange(id, data);
        }
    }

    private void onLoadDebit(DataSnapshot dataSnapshot) {
        for (DataSnapshot result : dataSnapshot.getChildren()) {
            String id = result.getKey();
            HashMap<String, Object> data = (HashMap<String, Object>) result.getValue();
            onSaveDebit(id, data);
        }
    }

    private void onLoadExchangeLoop(DataSnapshot dataSnapshot) {
        for (DataSnapshot result : dataSnapshot.getChildren()) {
            String id = result.getKey();
            HashMap<String, Object> data = (HashMap<String, Object>) result.getValue();
            onSaveExchangeLoop(id, data);
        }
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

    private void onSaveAccount(String id, HashMap<String, Object> data) {
        Account account = new Account();
        account.setId(id);
        account.setName((String) data.get("name"));
        account.setCreated(DateTimeUtils.getInstance().getDateFormat((String) data.get("created")));
        account.setInitAmount((String) data.get("initAmount"));
        account.setColorHex((String) data.get("colorHex"));
        account.setSaveLocation((Boolean) data.get("saveLocation"));
        account.setDefault((Boolean) data.get("isDefault"));
        AccountManager.getInstance().insertOrUpdate(account);
    }

    private void onSaveExchange(String id, HashMap<String, Object> data) {
        Exchange exchange = new Exchange();
        exchange.setId(id);
        exchange.setTypeExchange(Integer.parseInt(String.valueOf(data.get("typeExchange"))));
        exchange.setIdAccount((String) data.get("idAccount"));
        exchange.setIdCategory((String) data.get("idCategory"));
        exchange.setIdDebit(Integer.parseInt(String.valueOf(data.get("idDebit"))));
        exchange.setIdAccountTransfer((String) data.get("idAccountTransfer"));
        exchange.setCodeTransfer((String) data.get("codeTransfer"));
        exchange.setAmount((String) data.get("amount"));
        exchange.setDescription((String) data.get("description"));
        exchange.setCreated(DateTimeUtils.getInstance().getDateFormat((String) data.get("created")));
        exchange.setAddress((String) data.get("address"));
        exchange.setLatitude(Double.parseDouble(String.valueOf(data.get("latitude"))));
        exchange.setLongitude(Double.parseDouble(String.valueOf(data.get("longitude"))));
        ExchangeManger.getInstance().insertOrUpdate(exchange);
    }

    private void onSaveExchangeLoop(String id, HashMap<String, Object> data) {
        ExchangeLooper exchangeLooper = new ExchangeLooper();
        exchangeLooper.setId(Integer.parseInt(id));
        exchangeLooper.setTypeExchange(Integer.parseInt(String.valueOf(data.get("typeExchange"))));
        exchangeLooper.setIdAccount((String) data.get("idAccount"));
        exchangeLooper.setIdCategory((String) data.get("idCategory"));
        exchangeLooper.setIdAccountTransfer((String) data.get("idAccountTransfer"));
        exchangeLooper.setCodeTransfer((String) data.get("codeTransfer"));
        exchangeLooper.setAmount((String) data.get("amount"));
        exchangeLooper.setDescription((String) data.get("description"));
        exchangeLooper.setCreated(DateTimeUtils.getInstance().getDateFormat((String) data.get("created")));
        exchangeLooper.setAddress((String) data.get("address"));
        exchangeLooper.setLatitude(Double.parseDouble(String.valueOf(data.get("latitude"))));
        exchangeLooper.setLongitude(Double.parseDouble(String.valueOf(data.get("longitude"))));
        exchangeLooper.setLoop((Boolean) data.get("isLoop"));
        exchangeLooper.setTypeLoop(Integer.parseInt(String.valueOf(data.get("typeLoop"))));
        ExchangeLoopManager.getInstance().insertOrUpdate(exchangeLooper);
    }

    private void onSaveDebit(String id, HashMap<String, Object> data) {
        Debit debit = new Debit();
        debit.setId(Integer.parseInt(id));
        debit.setName((String) data.get("name"));
        debit.setAmount((String) data.get("amount"));
        debit.setIdAccount((String) data.get("idAccount"));
        debit.setTypeDebit(Integer.parseInt(String.valueOf(data.get("typeDebit"))));
        debit.setClose((Boolean) data.get("isClose"));
        debit.setStartDate(DateTimeUtils.getInstance().getDateFormat((String) data.get("startDate")));
        debit.setEndDate(DateTimeUtils.getInstance().getDateFormat((String) data.get("endDate")));
        debit.setDescription((String) data.get("description"));
        DebitManager.getInstance().insertOrUpdateDebit(debit);
    }
}
