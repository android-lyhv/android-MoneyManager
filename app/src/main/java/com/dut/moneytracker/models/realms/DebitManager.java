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

    /**
     * Sync with firebase
     */
    public void deleteDebitByAccount(String idAccount) {
        int idDebit = -1;
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("idAccount", idAccount).findFirst();
        if (debit != null) {
            idDebit = debit.getId();
            debit.deleteFromRealm();
        }
        realm.commitTransaction();
        ExchangeManger.getInstance().deleteExchangeByDebit(idDebit);
    }

    public Debit getDebitById(int id) {
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return debit;
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

    public void deleteDebitById(int id) {
        ExchangeManger.getInstance().deleteExchangeByDebit(id);
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("id", id).findFirst();
        debit.deleteFromRealm();
        realm.commitTransaction();
    }

    public void setStatusDebit(int id, boolean isClose) {
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("id", id).findFirst();
        debit.setClose(isClose);
        realm.insertOrUpdate(debit);
        realm.commitTransaction();
    }

    /***************************************************************/
    public RealmResults<Debit> onLoadDebitAsync() {
        return realm.where(Debit.class).findAllAsync();
    }

    public String getAccountNameByDebitId(int id) {
        realm.beginTransaction();
        String accountId = realm.where(Debit.class).equalTo("id", id).findFirst().getIdAccount();
        realm.commitTransaction();
        return AccountManager.getInstance().getAccountNameById(accountId);
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
            exchange.setId(String.valueOf(debit.getId()));
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

    public int getNewIdDebit() {
        Number number = realm.where(Debit.class).max("id");
        int nextId;
        if (number == null) {
            nextId = 1;
        } else {
            nextId = number.intValue() + 1;
        }
        return nextId;
    }
}
