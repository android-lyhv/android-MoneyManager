package com.dut.moneytracker.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.DebitType;
import com.dut.moneytracker.models.realms.DebitManager;
import com.dut.moneytracker.objects.Debit;
import com.dut.moneytracker.ui.MainActivity;

import java.util.Locale;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 16/04/2017.
 */

/**
 * This alarm turn on when end date of debit come in
 */
public class ReceiveAlarmDebit extends BroadcastReceiver {
    private static final String TAG = ReceiveAlarmDebit.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !TextUtils.equals(intent.getAction(), context.getString(R.string.alarm_debit_action))) {
            return;
        }
        int idDebit = intent.getIntExtra(context.getString(R.string.alarm_debit_id), -1);
        Debit debit = DebitManager.getInstance().getDebitById(idDebit);
        onNotification(context, debit);
    }

    private void onNotification(Context context, Debit debit) {
        if (debit == null) {
            return;
        }
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
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, debit.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(debit.getId(), builder.build());
    }
}
