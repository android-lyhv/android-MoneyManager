package com.dut.moneytracker.models.realms;

import com.dut.moneytracker.constant.DebitType;
import com.dut.moneytracker.objects.Debit;
import com.dut.moneytracker.objects.Exchange;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

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
        ExchangeManger.getInstance().deleteExchangeByDebit(idDebit);
    }

    public void insertOrUpdateDebit(Debit debit) {
        realm.beginTransaction();
        realm.insertOrUpdate(debit);
        realm.commitTransaction();
        genExchangeFromDebit(debit, null);
    }

    public void updateDebitIfAccountChange(Debit debit) {
        realm.beginTransaction();
        realm.insertOrUpdate(debit);
        realm.commitTransaction();
        ExchangeManger.getInstance().updateExchangeByDebit(debit.getId(), debit.getIdAccount());
    }

    public void deleteDebitById(String id) {
        ExchangeManger.getInstance().deleteExchangeByDebit(id);
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("id", id).findFirst();
        debit.deleteFromRealm();
        realm.commitTransaction();
    }

    public String getAccountNameByDebitId(String id) {
        realm.beginTransaction();
        String accountId = realm.where(Debit.class).equalTo("id", id).findFirst().getIdAccount();
        realm.commitTransaction();
        return AccountManager.getInstance().getAccountNameById(accountId);
    }

    public void setStatusDebit(String id, boolean isClose) {
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("id", id).findFirst();
        debit.setClose(isClose);
        realm.insertOrUpdate(debit);
        realm.commitTransaction();
    }

    public String getRemindAmountDebit(Debit debit) {
        String amount = ExchangeManger.getInstance().getAmountExchangeByDebit(debit.getId());
        BigDecimal bigDecimal = new BigDecimal(debit.getAmount());
        bigDecimal = bigDecimal.add(new BigDecimal(amount));
        return bigDecimal.toString();
    }

    public void genExchangeFromDebit(Debit debit, String amount) {
        Exchange exchange = new Exchange();
        if (null == amount) {
            exchange.setId(debit.getId());
            exchange.setAmount(debit.getAmount());
            if (debit.getTypeDebit() == DebitType.LEND) {
                exchange.setDescription(String.format(Locale.US, "%s -> Tôi", debit.getName()));
            } else {
                exchange.setDescription(String.format(Locale.US, "Tôi -> %s", debit.getName()));
            }
        } else {
            if (debit.getTypeDebit() == DebitType.BORROWED) {
                amount = String.format(Locale.US, "-%s", amount);
            }
            exchange.setId(UUID.randomUUID().toString());
            exchange.setAmount(amount);
            if (debit.getTypeDebit() == DebitType.LEND) {
                exchange.setDescription(String.format(Locale.US, "%s -> Tôi (một phần)", debit.getName()));
            } else {
                exchange.setDescription(String.format(Locale.US, "Tôi -> %s (một phần)", debit.getName()));
            }
        }
        exchange.setIdAccount(debit.getIdAccount());
        exchange.setCreated(new Date());
        exchange.setIdDebit(debit.getId());
        ExchangeManger.getInstance().insertOrUpdate(exchange);
    }
}
