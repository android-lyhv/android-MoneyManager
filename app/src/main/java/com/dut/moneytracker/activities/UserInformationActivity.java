package com.dut.moneytracker.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.ResultCode;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 03/03/2017.
 */

public class UserInformationActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks {
    private ImageView imgUser;
    private TextView tvUser;
    private TextView tvEmail;
    private Button btnLogout;
    private FirebaseAuth mFireBaseAuth;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        loadDataUser();
    }

    private void initView() {
        imgUser = (ImageView) findViewById(R.id.imgUser);
        tvUser = (TextView) findViewById(R.id.tvUserName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                if (mFireBaseAuth.getCurrentUser() != null) {
                    mFireBaseAuth.signOut();
                    logoutGoogle();
                    setResult(ResultCode.PROFILE);
                    finish();
                }
                break;
        }

    }

    private void loadDataUser() {
        mFireBaseAuth = FirebaseAuth.getInstance();
        tvUser.setText(String.valueOf(mFireBaseAuth.getCurrentUser().getDisplayName()));
        tvEmail.setText(String.valueOf(mFireBaseAuth.getCurrentUser().getEmail()));
        Glide.with(this).load(mFireBaseAuth.getCurrentUser().getPhotoUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .into(imgUser);
    }

    private void logoutGoogle() {
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
