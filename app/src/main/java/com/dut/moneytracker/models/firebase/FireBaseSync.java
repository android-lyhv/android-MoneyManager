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

    public void loadDataCategory() {
        Log.d(TAG, "loadDataCategory: aaaaa");
        mDatabase.child("aaa").setValue("abfas");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

    }

}
