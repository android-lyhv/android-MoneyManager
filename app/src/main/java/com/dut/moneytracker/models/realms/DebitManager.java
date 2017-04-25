package com.dut.moneytracker.models.realms;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.DebitType;
import com.dut.moneytracker.models.firebase.FireBaseSync;
import com.dut.moneytracker.objects.Debit;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.ui.MainActivity_;

import java.math.BigDecimal;
import java.util.Calendar;
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

    public void insertOrUpdateDebit(Debit debit) {
        realm.beginTransaction();
        realm.insertOrUpdate(debit);
        realm.commitTransaction();
        genExchangeFromDebit(debit, null);
        FireBaseSync.getInstance().upDateDebit(debit);
    }

    public void updateDebitIfAccountChange(Debit debit) {
        realm.beginTransaction();
        realm.insertOrUpdate(debit);
        realm.commitTransaction();
        FireBaseSync.getInstance().upDateDebit(debit);
        ExchangeManger.getInstance().updateExchangeByDebit(debit.getId(), debit.getIdAccount());
    }

    public void deleteDebitById(int id) {
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("id", id).findFirst();
        debit.deleteFromRealm();
        realm.commitTransaction();
        ExchangeManger.getInstance().deleteExchangeByDebit(id);
        FireBaseSync.getInstance().deleteDebit(id);
    }

    public void setStatusDebit(int id, boolean isClose) {
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("id", id).findFirst();
        debit.setClose(isClose);
        realm.insertOrUpdate(debit);
        realm.commitTransaction();
        FireBaseSync.getInstance().upDateDebit(debit);
    }

    /***************************************************************/
    public RealmResults<Debit> onLoadDebitAsync() {
        return realm.where(Debit.class).findAllAsync();
    }

    public Debit getDebitById(int id) {
        realm.beginTransaction();
        Debit debit = realm.where(Debit.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return debit;
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

    public void onCheckEndDateDebit(Context context) {
        RealmResults<Debit> realmResults = getDebitsNotClose();
        for (Debit debit : realmResults) {
            if (Calendar.getInstance().getTimeInMillis() >= debit.getEndDate().getTime()) {
                pushNotification(context, debit);
            }
        }
    }

    private RealmResults<Debit> getDebitsNotClose() {
        realm.beginTransaction();
        RealmResults<Debit> realmResults = realm.where(Debit.class).equalTo("isClose", false).findAll();
        realm.commitTransaction();
        return realmResults;
    }

    private void pushNotification(Context context, Debit debit) {
        String content;
        if (debit.getTypeDebit() == DebitType.LEND) {
            content = String.format(Locale.US, "%s -> Tôi", debit.getName());
        } else {
            content = String.format(Locale.US, "Tôi -> %s", debit.getName());
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_alarm_debit)
                .setContentText(content)
                .setContentTitle(context.getString(R.string.title_debit_notitication))
                .setAutoCancel(true);
        Intent intent = new Intent(context, MainActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, debit.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(debit.getId(), builder.build());
    }
}
