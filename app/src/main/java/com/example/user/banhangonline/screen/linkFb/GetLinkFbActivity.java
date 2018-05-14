package com.example.user.banhangonline.screen.linkFb;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.user.banhangonline.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetLinkFbActivity extends AppCompatActivity {
    @BindView(R.id.btn_login_fb)
    LoginButton loginButton;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_link_fb);
        ButterKnife.bind(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callbackManager = CallbackManager.Factory.create();

                LoginManager.getInstance().registerCallback(callbackManager,
                         new FacebookCallback<LoginResult>() {
                             @Override
                             public void onSuccess(LoginResult loginResult) {
                                 // App code
                                 Log.d("TAG RS", String.valueOf(loginResult.getAccessToken()));
                             }

                             @Override
                             public void onCancel() {
                                 // App code
                             }

                             @Override
                             public void onError(FacebookException exception) {
                                 // App code
                             }
                         });
            }
        });
    }
}
