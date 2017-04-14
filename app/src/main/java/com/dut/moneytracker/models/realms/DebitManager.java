package com.dut.moneytracker.models.realms;

import com.dut.moneytracker.objects.Debit;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 11/04/2017.
 */
public class DebitManager extends RealmHelper {
    private static DebitManager ourInstance;

    public static DebitManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DebitManager();
        }
        return ourInstance;
    }

    private DebitManager() {
    }

    public RealmResults<Debit> onLoadDebitAsync() {
        return realm.where(Debit.class).findAllAsync();
    }

    public void deleteDebitByAccount(String idAccount) {
        String idDebit = null;
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("idAccount", idAccount).findFirst();
        if (debit != null) {
            idDebit = debit.getId();
            debit.deleteFromRealm();
        }
        realm.commitTransaction();
        ExchangeManger.getInstance().deleteExchangeByDebitId(idDebit);
    }

    public void insertOrUpdateDebit(Debit debit) {
        realm.beginTransaction();
        realm.insertOrUpdate(debit);
        realm.commitTransaction();
        genExchangeFromDebit(debit, null);
    }

    public void deleteDebitById(String id) {
        ExchangeManger.getInstance().deleteExchangeByDebitId(id);
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("id", id).findFirst();
        debit.deleteFromRealm();
        realm.commitTransaction();
    }

    public void genExchangeFromDebit(Debit debit, String amount) {
        //TODO here
       /* Exchange exchange = new Exchange();
        if (null == amount) {
            // This for first debit exchange
            exchange.setId(debit.getId());
            exchange.setAmount(debit.getAmount());
        } else {
            exchange.setId(UUID.randomUUID().toString());
            exchange.setAmount(amount);
        }
        exchange.setCreated(new Date());
        exchange.setIdDebit(debit.getId());
        if (debit.getTypeDebit() == DebitType.LEND) {
            exchange.setDescription(String.format(Locale.US, "%s -> Tôi", debit.getName()));
        } else {
            exchange.setDescription(String.format(Locale.US, "Tôi -> %s", debit.getName()));
        }
        ExchangeManger.getInstance().insertOrUpdate(exchange);*/
    }
}
