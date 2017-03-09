package com.dut.moneytracker.models.realms;

import com.dut.moneytracker.objects.PaymentType;

import java.util.List;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */

public class PaymentManager extends RealmHelper {
    private static PaymentManager paymentManager = new PaymentManager();

    public static PaymentManager getInstance() {
        return paymentManager;
    }

    private void PaymentType() {

    }

    public void createPaymentType(String[] listType) {
        int size = listType.length;
        for (int i = 0; i < size; i++) {
            PaymentType paymentType = new PaymentType();
            paymentType.setId(String.valueOf(i));
            paymentType.setName(listType[0]);
            insertOrUpdate(paymentType);
        }
    }

    public List<PaymentType> getPaymentTypes() {
        realm.beginTransaction();
        RealmResults<PaymentType> realmResults = realm.where(PaymentType.class).findAll();
        List<PaymentType> paymentTypes = realmResults.subList(0, realmResults.size());
        realm.commitTransaction();
        return paymentTypes;
    }
}
