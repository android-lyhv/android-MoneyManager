package com.dut.moneytracker.ui.user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.models.AppConfig;
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
    private static final String TAG = LoginActivity.class.getSimpleName();
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFireBaseAuth;

    @AfterViews
    void init() {
        configFireBase();
        configGoogleApi();
    }

    private void configFireBase() {
        mFireBaseAuth = FirebaseAuth.getInstance();
    }

    @Click(R.id.btnLoginWithGoogle)
    void onClickLoginWithGoogle() {
        if (!NetworkUtils.getInstance().isConnectNetwork(this)) {
            Toast.makeText(this, R.string.toast_text_connection_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        requestLoginWithGoogle();
    }

    @Click(R.id.btnLoginWithFacebook)
    void onClickLoginWithFacebook() {
        if (!NetworkUtils.getInstance().isConnectNetwork(this)) {
            Toast.makeText(this, R.string.toast_text_connection_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        requestLoginFacebook();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onLoginFacebook();
        mFireBaseAuth.addAuthStateListener(this);
    }


    private void onLoginFacebook() {
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

    private void requestLoginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList(PERMISSION_FB));
    }

    private void configGoogleApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void requestLoginWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            logOutGoogle();
            syncDataFromServer(user);
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    private void syncDataFromServer(FirebaseUser firebaseUser) {
        String currentUserId = AppConfig.getInstance().getCurrentUserId(this);
        Log.d(TAG, "syncDataFromServer: " + currentUserId + "   " + firebaseUser.getUid());
        if (!TextUtils.equals(currentUserId, firebaseUser.getUid())) {
            ActivityLoadData_.intent(this).start();
        } else {
            MainActivity_.intent(this).start();
        }
        finish();
    }

    private void logOutGoogle() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
    }
}
