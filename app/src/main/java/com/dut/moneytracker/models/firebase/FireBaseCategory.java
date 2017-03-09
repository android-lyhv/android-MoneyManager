package com.dut.moneytracker.models.firebase;

import android.content.Context;

import com.dut.moneytracker.models.realms.CategoryManager;
import com.google.firebase.database.DatabaseReference;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 03/03/2017.
 */

public class FireBaseCategory {
    private static final String TAG = FireBaseCategory.class.getSimpleName();
    private DatabaseReference mDatabase;
    private CategoryManager categoryManager;

    public FireBaseCategory() {

    }

    public void loadDataCategory(final Context context) {
        /*FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String s="/users/"+firebaseAuth.getCurrentUser().getUid()+"/";
        Log.d(TAG, "loadDataCategory: "+s);
        mDatabase = FirebaseDatabase.getInstance().getReference(s);
        mDatabase.child("aaa").setValue("abfas");
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

}
