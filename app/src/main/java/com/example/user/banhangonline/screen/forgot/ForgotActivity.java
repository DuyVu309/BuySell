package com.example.user.banhangonline.screen.forgot;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ForgotActivity extends BaseActivity implements ForgotContact.View {

    @BindView(R.id.edt_email_forgot)
    EditText edtEmailForgot;

    @BindView(R.id.btn_lay_mat_khau)
    Button btnContinue;


    Unbinder unbinder;
    ForgotPresenter mPresenter;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        unbinder = ButterKnife.bind(this);
        mPresenter = new ForgotPresenter();
        mPresenter.setFirebaseAuth(mAuth);
        mPresenter.onCreate();
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter.detach();
        unbinder.unbind();
        unbinder = null;
        super.onDestroy();
    }

    @OnClick(R.id.img_back)
    public void onBackLogin(){
        finish();
    }

    @OnClick(R.id.btn_lay_mat_khau)
    public void resetPassword(){
        showDialog();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmailForgot.getText().toString().trim()).matches()) {
            mPresenter.sendPasswordResetEmail(edtEmailForgot.getText().toString().trim());
        } else {
            edtEmailForgot.setError(getResources().getString(R.string.email_khong_hop_le));
            dismissDialog();
        }
    }

    @Override
    public void resetPasswordSuccess() {
        dismissDialog();
        showSnackbar(getString(R.string.da_gui_toi_email_cua_ban));
        finish();
    }

    @Override
    public void resetPasswordFailed() {
        dismissDialog();
        showSnackbar(getResources().getString(R.string.error_and_check));
    }
}
