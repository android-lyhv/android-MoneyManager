package com.dut.moneytracker.models.firebase;

import android.content.Context;
import android.util.Log;

import com.dut.moneytracker.models.AppConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 03/03/2017.
 */

public class FireBaseSync {
    private static final String TAG = FireBaseSync.class.getSimpleName();
    private DatabaseReference mDatabase;
    private static FireBaseSync fireBaseSync;
    private static final String CHILD_ACCOUNT = "account";
    private static final String CHILD_EXCHANGE = "exchange";
    private static final String CHILD_EXCHANGE_LOOP = "exchange_loop";
    private static final String CHILD_DEBIT = "debit";

    public static FireBaseSync getInstance(Context context) {
        if (fireBaseSync == null) {
            fireBaseSync = new FireBaseSync(context);
        }
        return fireBaseSync;
    }

    private FireBaseSync(Context context) {
        String reference = AppConfig.getInstance().getReferenceDatabase(context);
        mDatabase = FirebaseDatabase.getInstance().getReference(reference);
    }

    public void upDateAccount() {
  /*   mDatabase.child("account").child("2").setValue("aaaaaaaaaa");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });*/
    }

    public void upDateExchange() {

    }

    public void upDateExchangeLoop() {

    }

    public void upDateDebit() {

    }

    public void onLoadDataServer() {
        Log.d(TAG, "onLoadDataServer: aaaaa");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.child(CHILD_DEBIT).getChildren()) {
                    Log.d(TAG, "onDataChange: "+messageSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
