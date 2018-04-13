package com.example.user.banhangonline.screen.login;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.screen.forgot.ForgotActivity;
import com.example.user.banhangonline.screen.home.HomeActivity;
import com.example.user.banhangonline.screen.register.RegisterActivity;
import com.example.user.banhangonline.untils.KeyUntils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends BaseActivity implements LoginContact.View {

    private LoginPresenter mPresenter;
    private Unbinder unbinder;

    @BindView(R.id.ln_login)
    LinearLayout lnLogin;

    @BindView(R.id.edt_emal)
    EditText edtEmail;

    @BindView(R.id.edt_Password)
    EditText edtPassword;

    @BindView(R.id.tv_notifi_password)
    TextView tvNotifiPassword;

    @BindView(R.id.btn_sign_in)
    Button btnSignIn;

    @BindView(R.id.tv_warning)
    TextView tvWarning;

    private boolean isAvaialbeUser = false;
    private boolean isAvaialbePassword = false;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        mPresenter.onCreate();

        isAvailableUserPassword();
        email = getIntent().getStringExtra(KeyUntils.keyEmailRegister);
        password = getIntent().getStringExtra(KeyUntils.keyPasswordRegister);
        if (email != null && password != null) {
            edtEmail.setText(email);
            edtPassword.setText(password);
        }

    }


    private void isAvailableUserPassword() {

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    isAvaialbeUser = true;
                } else {
                    isAvaialbeUser = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    isAvaialbeUser = true;
                } else {
                    isAvaialbeUser = false;
                }
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    isAvaialbePassword = true;
                    isClickAble();
                    if (charSequence.toString().length() >= 6) {
                        tvNotifiPassword.setVisibility(View.GONE);
                    } else {
                        tvNotifiPassword.setVisibility(View.VISIBLE);
                    }
                } else {
                    isAvaialbePassword = false;
                    isClickAble();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    isAvaialbePassword = true;
                    isClickAble();
                } else {
                    isAvaialbePassword = false;
                    isClickAble();
                }
            }
        });
    }

    private void isClickAble() {
        if (isAvaialbeUser && isAvaialbePassword) {
            btnSignIn.setClickable(true);
            btnSignIn.setBackground(getResources().getDrawable(R.drawable.bg_button_signin_after));
            btnSignIn.setTextColor(getResources().getColor(R.color.white));
        } else {
            btnSignIn.setClickable(false);
            btnSignIn.setBackground(getResources().getDrawable(R.drawable.bg_button_signin_before));
            btnSignIn.setTextColor(getResources().getColor(R.color.blue));
        }
    }

    @OnClick(R.id.btn_sign_in)
    public void onClickSignIn() {
        if (mPresenter != null) {
            mPresenter.signInWithEmailPassword("0123456789",
                     edtEmail.getText().toString().trim(),
                     edtPassword.getText().toString().trim());
        }
    }

    @OnClick(R.id.btn_refister)
    public void onOnclickRegister() {
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissDialog();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        }, 1000);
    }

    @Override
    public void signInSuccess() {
        showDialog();
        mAuth.signInWithEmailAndPassword(edtEmail.getText().toString().trim(),
                 edtPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dismissDialog();
                    runAnimationStartActivity(lnLogin);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    tvWarning.setVisibility(View.VISIBLE);
                    runAnimationStartActivity(tvWarning);
                    dismissDialog();
                }
            }
        });
    }

    @Override
    public void signInError() {
        tvWarning.setVisibility(View.VISIBLE);
        runAnimationStartActivity(tvWarning);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter.detach();
        unbinder.unbind();
        unbinder = null;
        super.onDestroy();
    }

    @OnClick(R.id.btn_forgot)
    public void forgotPassword() {
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runAnimationStartActivity(findViewById(android.R.id.content));
                dismissDialog();
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));

            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismissDialog();
    }
}
