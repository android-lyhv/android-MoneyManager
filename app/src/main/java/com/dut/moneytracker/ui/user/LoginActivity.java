package com.dut.moneytracker.ui.user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.AppConfig;
import com.dut.moneytracker.models.firebase.FireBaseSync;
import com.dut.moneytracker.ui.ActivityLoadData_;
import com.dut.moneytracker.ui.MainActivity_;
import com.dut.moneytracker.utils.DialogUtils;
import com.dut.moneytracker.utils.NetworkUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.Arrays;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 21/02/2017.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, FirebaseAuth.AuthStateListener {
    private static final String[] PERMISSION_FB = {"email", "public_profile", "user_posts"};
    private static final int RC_SIGN_IN = 1;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFireBaseAuth;

    @AfterViews
    void init() {
        initMap();
        if (AppConfig.getInstance().isLogin(this)) {
            MainActivity_.intent(this).start();
        } else {
            configFireBase();
            configLoginGoogle();
            configLoginFacebook();
        }
    }

    private void configFireBase() {
        mFireBaseAuth = FirebaseAuth.getInstance();
        mFireBaseAuth.addAuthStateListener(this);
    }

    private void configLoginGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void configLoginFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fireBaseAuthWthFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void initMap() {
        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapInit);
        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //no-op
            }
        });

    }

    @Click(R.id.tvLoginWithGoogle)
    void onClickLoginWithGoogle() {
        if (!NetworkUtils.getInstance().isConnectNetwork(this)) {
            Toast.makeText(this, R.string.toast_text_connection_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Click(R.id.tvLoginWithFacebook)
    void onClickLoginWithFacebook() {
        if (!NetworkUtils.getInstance().isConnectNetwork(this)) {
            Toast.makeText(this, R.string.toast_text_connection_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(PERMISSION_FB));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                fireBaseAuthWithGoogle(account);
            }
        }
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount account) {
        DialogUtils.getInstance().showProgressDialog(this, getString(R.string.dialog_messenger_connect));
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFireBaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DialogUtils.getInstance().dismissProgressDialog();
                    }
                });
    }

    private void fireBaseAuthWthFacebook(AccessToken accessToken) {
        DialogUtils.getInstance().showProgressDialog(this, getString(R.string.dialog_messenger_connect));
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFireBaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DialogUtils.getInstance().dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFireBaseAuth != null) {
            mFireBaseAuth.removeAuthStateListener(this);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            AppConfig.getInstance().setReferenceDatabase(this, firebaseUser.getUid());
            FireBaseSync.getInstance().initDataReference(getApplicationContext());
            requestLogOutGoogle();
            ActivityLoadData_.intent(this).start();
            finish();
        }
    }

    private void requestLogOutGoogle() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFireBaseAuth.removeAuthStateListener(this);
    }
}
