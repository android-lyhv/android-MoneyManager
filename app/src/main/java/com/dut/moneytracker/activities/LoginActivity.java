package com.dut.moneytracker.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dut.moneytracker.R;
import com.google.android.gms.common.SignInButton;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 21/02/2017.
 */

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SignInButton signInButton = (SignInButton) findViewById(R.id.btnLoginWithGoogle);
        signInButton.setBackgroundColor(Color.BLUE);
        signInButton.setColorScheme(SignInButton.COLOR_AUTO);
        signInButton.setSize(SignInButton.SIZE_WIDE);
    }

}
