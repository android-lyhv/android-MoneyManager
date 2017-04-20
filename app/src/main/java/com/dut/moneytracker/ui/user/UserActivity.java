package com.dut.moneytracker.ui.user;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.IntentCode;
import com.dut.moneytracker.models.AppConfig;
import com.dut.moneytracker.view.CircleImageView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 03/03/2017.
 */
@EActivity(R.layout.activity_user)
public class UserActivity extends AppCompatActivity {
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.imgUser)
    CircleImageView imgUser;
    @ViewById(R.id.tvUserName)
    TextView tvUser;
    @ViewById(R.id.tvEmail)
    TextView tvEmail;
    private FirebaseAuth mFireBaseAuth;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadDataUser();
    }

    @Click(R.id.btnLogout)
    void onClickLogout() {
        AppConfig.getInstance().clearAllData(this);
        if (mFireBaseAuth.getCurrentUser() != null) {
            mFireBaseAuth.signOut();
            requestLogoutFacebook();
            setResult(IntentCode.PROFILE);
            finish();
        }
    }

    @OptionsItem(android.R.id.home)
    void onClose() {
        finish();
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

    private void requestLogoutFacebook() {
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }
}
