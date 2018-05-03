package com.example.user.banhangonline.screen.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.screen.home.HomeActivity;
import com.example.user.banhangonline.screen.login.LoginActivity;
import com.example.user.banhangonline.untils.KeyUntils;
import com.example.user.banhangonline.untils.NetworkUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyUntils.keyAccount;
import static com.example.user.banhangonline.untils.KeyUntils.keyAccountBuy;
import static com.example.user.banhangonline.untils.KeyUntils.keyAccountSell;

public class RegisterActivity extends BaseActivity implements RegisterContact.View {

    @BindView(R.id.ln_register)
    LinearLayout lnRegister;

    @BindView(R.id.rbtn_nguoi_mua)
    RadioButton rbNguoiMua;

    @BindView(R.id.rbtn_nguoi_ban_hang)
    RadioButton rbNguoiBan;

    @BindView(R.id.layout_register_buy)
    ScrollView layoutRegisterBuy;

    @BindView(R.id.edt_full_name)
    EditText edtFullName;

    @BindView(R.id.tv_waring_fullname)
    TextView tvWarningFullName;

    @BindView(R.id.edt_email)
    EditText edtmailBuy;

    @BindView(R.id.tv_waring_email)
    TextView tvWarningEmailBuy;

    @BindView(R.id.edt_password_buy)
    EditText edtPasswordBuy;

    @BindView(R.id.tv_warning_password)
    TextView tvWarningPasswordBuy;

    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPasswordBuy;

    @BindView(R.id.tv_warning_confirm_password)
    TextView tvWarningConfirmPasswordBuy;

    @BindView(R.id.btn_register)
    Button btnRegisterBuy;

    @BindView(R.id.layout_register_sell)
    ScrollView layoutRegisterSell;

    @BindView(R.id.edt_ten_doanhnghiep)
    EditText edtTenDoanhNghiep;

    @BindView(R.id.tv_warning_doanhnghiep)
    TextView tvWarningTenDoanhNghiep;

    @BindView(R.id.edt_email_register)
    EditText edtEmailRegisterSell;

    @BindView(R.id.tv_warning_email)
    TextView tvWarningEmailSell;

    @BindView(R.id.edt_password_business)
    EditText edtPasswordSell;

    @BindView(R.id.tv_notifi_password)
    TextView tvWarningPasswordSell;

    @BindView(R.id.edt_confirm_password_business)
    EditText edtConfirmPasswordSell;

    @BindView(R.id.tv_notifi_confirm_password)
    TextView tvWarningConfirmPasswordSell;

    @BindView(R.id.edit_phone_standard_vietnam)
    EditText edtPhoneStandard;

    @BindView(R.id.edt_number_phone)
    EditText edtPhoneNumberSell;

    @BindView(R.id.btn_register_business)
    Button btnRegisterSell;

    @BindView(R.id.edt_code)
    EditText edtCode;

    @BindView(R.id.btn_send_code)
    Button btnSendCode;

    @BindView(R.id.btn_resend_code)
    Button btnResendCode;

    @BindView(R.id.btn_vevify_code)
    Button btnVerifyCode;

    Unbinder unbinder;
    RegisPresenter mPresenter;

    private boolean isConfirmCode = false;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);
        mPresenter = new RegisPresenter();
        mPresenter.attachView(this);
        mPresenter.onCreate();
        mPresenter.setContext(this);
        mPresenter.setFireBaseAuth(mAuth);
        mPresenter.setCallBacksPhone();

        initCofirmRefister();
        isAvaiableData();
    }

    private void initCofirmRefister() {
        rbNguoiMua.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    layoutRegisterSell.setVisibility(View.GONE);
                    layoutRegisterBuy.setVisibility(View.VISIBLE);
                }
            }
        });

        rbNguoiBan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    layoutRegisterBuy.setVisibility(View.GONE);
                    layoutRegisterSell.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void isAvaiableData() {

        //start register with buy
        edtFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().equals("") && charSequence.toString().trim().length() >= 8) {
                    tvWarningFullName.setVisibility(View.GONE);
                } else {
                    tvWarningFullName.setVisibility(View.VISIBLE);
                }
                isVisibilityBtnRegisterBuy();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtmailBuy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(charSequence.toString().trim()).matches()) {
                    tvWarningEmailBuy.setVisibility(View.GONE);
                } else {
                    tvWarningEmailBuy.setVisibility(View.VISIBLE);
                }
                isVisibilityBtnRegisterBuy();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtPasswordBuy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() >= 6) {
                    tvWarningPasswordBuy.setVisibility(View.GONE);
                } else {
                    tvWarningPasswordBuy.setVisibility(View.VISIBLE);
                }
                isVisibilityBtnRegisterBuy();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtConfirmPasswordBuy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().equals(edtPasswordBuy.getText().toString().trim())) {
                    tvWarningConfirmPasswordBuy.setVisibility(View.GONE);
                } else {
                    tvWarningConfirmPasswordBuy.setVisibility(View.VISIBLE);
                }
                isVisibilityBtnRegisterBuy();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnRegisterBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.isClickable()) {
                    if (NetworkUtils.isConnected(RegisterActivity.this)) {
                        if (mPresenter != null) {
                            if (edtPasswordBuy.getText().toString().trim().equals(edtConfirmPasswordBuy.getText().toString().trim())) {
                                mPresenter.registerBuy(edtFullName.getText().toString().trim(),
                                         edtmailBuy.getText().toString().trim(),
                                         edtPasswordBuy.getText().toString().trim(),
                                         edtConfirmPasswordBuy.getText().toString().trim());
                            } else {
                                showSnackbar("Mật khẩu không trùng khớp");
                            }

                        }
                    } else {
                        showNoInternet();
                    }

                }
            }
        });
        //end register with buy

        //start register with sell
        edtTenDoanhNghiep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().equals("")) {
                    tvWarningTenDoanhNghiep.setVisibility(View.GONE);
                } else {
                    tvWarningTenDoanhNghiep.setVisibility(View.VISIBLE);
                }
                isVisibilityBtnRegisterSell();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtEmailRegisterSell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(charSequence.toString().trim()).matches()) {
                    tvWarningEmailSell.setVisibility(View.GONE);
                } else {
                    tvWarningEmailSell.setVisibility(View.VISIBLE);
                }
                isVisibilityBtnRegisterSell();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtPasswordSell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() >= 6) {
                    tvWarningPasswordSell.setVisibility(View.GONE);
                } else {
                    tvWarningPasswordSell.setVisibility(View.VISIBLE);
                }
                isVisibilityBtnRegisterSell();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtConfirmPasswordSell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().equals(edtPasswordSell.getText().toString().trim())) {
                    tvWarningConfirmPasswordSell.setVisibility(View.GONE);
                } else {
                    tvWarningConfirmPasswordSell.setVisibility(View.VISIBLE);
                }
                isVisibilityBtnRegisterSell();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtPhoneNumberSell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("0")) {
                    edtPhoneNumberSell.setText("");
                }
                isVisibilityBtnRegisterSell();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnRegisterSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.isClickable()) {
                    if (NetworkUtils.isConnected(RegisterActivity.this)) {
                        showDialog();
                        if (mPresenter != null) {
                            mPresenter.setphoneNumber(edtPhoneStandard.getText().toString().trim(), edtPhoneNumberSell.getText().toString().trim());
                            if (edtConfirmPasswordSell.getText().toString().trim().equals(edtPasswordSell.getText().toString().trim())) {
                                mPresenter.registerSell(edtTenDoanhNghiep.getText().toString().trim(),
                                         edtEmailRegisterSell.getText().toString().trim(),
                                         edtPasswordSell.getText().toString().trim(),
                                         edtConfirmPasswordSell.getText().toString().trim());
                            } else {
                                showSnackbar("Mật khẩu không trùng khớp");
                            }

                        }
                    } else {
                        showNoInternet();
                    }
                }
            }
        });
        //end register with sell
    }

    private void isVisibilityBtnRegisterBuy() {
        if (tvWarningFullName.getVisibility() == View.GONE
                 && tvWarningEmailBuy.getVisibility() == View.GONE
                 && tvWarningPasswordBuy.getVisibility() == View.GONE
                 && tvWarningConfirmPasswordBuy.getVisibility() == View.GONE) {
            btnRegisterBuy.setEnabled(true);
            btnRegisterBuy.setTextColor(getResources().getColor(R.color.white));
        } else {
            btnRegisterBuy.setEnabled(false);
            btnRegisterBuy.setTextColor(getResources().getColor(R.color.grey_divider));
        }
    }

    private void isVisibilityBtnRegisterSell() {
        if (tvWarningTenDoanhNghiep.getVisibility() == View.GONE
                 && tvWarningEmailSell.getVisibility() == View.GONE
                 && tvWarningPasswordSell.getVisibility() == View.GONE
                 && tvWarningConfirmPasswordSell.getVisibility() == View.GONE
                 && isConfirmCode) {
            btnRegisterSell.setEnabled(true);
            btnRegisterSell.setTextColor(getResources().getColor(R.color.white));
        } else {
            btnRegisterSell.setEnabled(false);
            btnRegisterSell.setTextColor(getResources().getColor(R.color.grey_divider));
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter.detach();
        unbinder.unbind();
        unbinder = null;
        super.onDestroy();
    }

    @Override
    public void registerBuySuccess() {
        final String email = edtmailBuy.getText().toString().trim();
        final String[] key = email.split("\\.");
        showDialog();
        if (NetworkUtils.isConnected(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDataBase.child(keyAccount).child(key[0]).setValue(new Account(keyAccountBuy,
                             edtFullName.getText().toString().trim(),
                             getString(R.string.dont_phone_number),
                             "",
                             "",
                             "",
                             "",
                             getString(R.string.dont_address)))
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful()) {
                                         mAuth.createUserWithEmailAndPassword(edtmailBuy.getText().toString().trim(), edtPasswordBuy.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                             @Override
                                             public void onComplete(@NonNull Task<AuthResult> task) {
                                                 if (task.isSuccessful()) {
                                                     Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                     intent.putExtra(KeyUntils.keyEmailRegister, edtmailBuy.getText().toString().trim());
                                                     intent.putExtra(KeyUntils.keyPasswordRegister, edtPasswordBuy.getText().toString().trim());
                                                     startActivity(intent);
                                                     finish();
                                                     dismissDialog();
                                                     showSnackbar(getResources().getString(R.string.error_and_check));

                                                 } else {
                                                     showSnackbar(getResources().getString(R.string.error_and_check));
                                                     dismissDialog();
                                                 }
                                             }
                                         });
                                     } else {
                                         showSnackbar(getResources().getString(R.string.error_and_check));
                                     }
                                 }
                             });

                }
            }, 1000);
        } else {
            showNoInternet();
        }
    }

    @Override
    public void registerBuyError() {
        showSnackbar(getResources().getString(R.string.email_avaliable_or_er));
    }

    @Override
    public void registerSellSuccess() {
        String email = edtEmailRegisterSell.getText().toString().trim();
        final String[] key = email.split("\\.");
        if (NetworkUtils.isConnected(this)) {

            mDataBase.child(keyAccount).child(key[0]).setValue(new Account(keyAccountSell,
                     edtTenDoanhNghiep.getText().toString().trim(),
                     mPresenter.getPhoneNumber(),
                     "",
                     "",
                     "",
                     "",
                     getString(R.string.dont_address))).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mAuth.createUserWithEmailAndPassword(edtEmailRegisterSell.getText().toString().trim(),
                                 edtPasswordSell.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.putExtra(KeyUntils.keyEmailRegister, edtEmailRegisterSell.getText().toString().trim());
                                    intent.putExtra(KeyUntils.keyPasswordRegister, edtPasswordSell.getText().toString().trim());
                                    dismissDialog();
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showSnackbar(getResources().getString(R.string.email_avaliable_or_er));
                                    dismissDialog();
                                }
                            }
                        });
                    } else {
                        showSnackbar(getResources().getString(R.string.error_and_check));
                    }
                }
            });


        } else {
            dismissDialog();
        }

    }

    @Override
    public void registerSellError() {
        showSnackbar(getResources().getString(R.string.error_and_check));
    }

    @Override
    public void verificationSuccess(PhoneAuthCredential credential) {
        if (credential != null) {
            if (credential.getSmsCode() != null) {
                edtCode.setText(credential.getSmsCode());
            } else {
                edtCode.setText("validation");
            }
        }
    }

    @Override
    public void sendingCode() {
        edtPhoneNumberSell.setEnabled(false);
        edtPhoneStandard.setEnabled(false);
        btnSendCode.setVisibility(View.GONE);
        btnResendCode.setVisibility(View.VISIBLE);
        btnVerifyCode.setEnabled(true);
        edtCode.setEnabled(true);
        edtCode.setClickable(true);
        dismissDialog();
        showSnackbar("Đang gửi mã, xin vui lòng chờ! ");
    }

    @Override
    public void notifyErrorPhoneNumber() {
        dismissDialog();
        isConfirmCode = false;
        edtPhoneNumberSell.setEnabled(true);
        edtPhoneStandard.setEnabled(true);
    }

    @Override
    public void notifyQuotaexceeded() {
        dismissDialog();
        isConfirmCode = false;
        edtPhoneNumberSell.setEnabled(true);
        edtPhoneStandard.setEnabled(true);
        showSnackbar("Quá số lần qua phép");
    }

    @Override
    public void codevalid() {
        dismissDialog();
        isConfirmCode = true;
        btnSendCode.setEnabled(false);
        btnResendCode.setEnabled(false);
        btnVerifyCode.setEnabled(false);
        edtPhoneNumberSell.setEnabled(false);
        isConfirmCode = true;
        isVisibilityBtnRegisterSell();
        showSnackbar("Xác nhận thành công");
    }

    @Override
    public void codeInvalid() {
        dismissDialog();
        isConfirmCode = false;
        showSnackbar("Xác nhận thất bại, kiểm tra lại");
    }

    @OnClick(R.id.img_back)
    public void onBackActivity() {
        if (NetworkUtils.isConnected(this)) {
            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
            finish();
        } else {
            showNoInternet();
        }

    }

    @OnClick(R.id.btn_send_code)
    public void sendCode() {
        edtPhoneNumberSell.setEnabled(false);
        edtPhoneStandard.setEnabled(false);
        if (NetworkUtils.isConnected(this)) {
            showDialog();
            mPresenter.startPhoneNumberVerification(edtPhoneStandard.getText().toString().trim() + edtPhoneNumberSell.getText().toString().trim());
        } else {
            showNoInternet();
        }

    }

    @OnClick(R.id.btn_resend_code)
    public void resendCode() {
        if (NetworkUtils.isConnected(this)) {
            showDialog();
            mPresenter.resendVerificationCode(edtPhoneStandard.getText().toString().trim() + edtPhoneNumberSell.getText().toString().trim());
        } else {
            showNoInternet();
        }

    }

    @OnClick(R.id.btn_vevify_code)
    public void verifyCode() {
        if (NetworkUtils.isConnected(this)) {
            showDialog();
            if (edtCode.getText().toString().trim().length() > 0) {
                mPresenter.verifyPhoneNumberWithCode(edtCode.getText().toString().trim());
            } else {
                edtCode.setError("Chưa có mã");
            }
        } else {
            showNoInternet();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismissDialog();
    }
}
