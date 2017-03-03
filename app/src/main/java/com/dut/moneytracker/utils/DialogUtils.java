package com.dut.moneytracker.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 03/03/2017.
 */
public class DialogUtils {
    private static DialogUtils ourInstance = new DialogUtils();
    private ProgressDialog progressDialog;

    public static DialogUtils getInstance() {
        return ourInstance;
    }

    private DialogUtils() {
    }

    public void showProgressDialog(Context context, String messing) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(messing);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
