package com.example.user.banhangonline.screen.register;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.user.banhangonline.base.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisPresenter extends BasePresenter implements RegisterContact.Presenter {

    private Context context;
    private RegisterContact.View mView;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String phone;

    public void setphoneNumber(String stadard, String number) {
        phone = stadard + number;
    }

    public String getPhoneNumber() {
        return phone != null ? phone : null;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setFireBaseAuth(FirebaseAuth firebaseAuth) {
        this.mAuth = firebaseAuth;
    }

    public void setCallBacksPhone() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                updateUI(STATE_VERIFY_SUCCESS, credential);
                mView.verificationSuccess(credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mView.notifyErrorPhoneNumber();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    mView.notifyQuotaexceeded();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
                mView.sendingCode();
            }
        };
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void attachView(RegisterContact.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public void registerBuy(String name, String email, String password, String confirmpassword) {
        if (!isViewAttached()) return;
        if (name.toString().trim().length() > 0
                 && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                 && email.endsWith(".com")
                 && password.length() >= 6
                 && confirmpassword.equals(password)) {
            if (mView != null) {
                mView.registerBuySuccess();
            }
        } else {
            if (mView != null) {
                mView.registerBuyError();
            }

        }
    }

    @Override
    public void registerSell(String name, String email, String password, String confirmPassword) {
        if (!isViewAttached()) return;
        if (name.toString().trim().length() > 0
                 && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                 && email.endsWith(".com")
                 && password.length() >= 6
                 && confirmPassword.equals(password)
                 && phone.length() > 8) {
            if (mView != null) {
                mView.registerSellSuccess();
            }
        } else {
            if (mView != null) {
                mView.registerSellError();
            }
        }
    }

    @Override
    public void startPhoneNumberVerification(String phoneNumber) {
        if (!isViewAttached()) return;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 phoneNumber,
                 60,
                 TimeUnit.SECONDS,
                 (Activity) context,
                 mCallbacks);
    }

    @Override
    public void resendVerificationCode(String phoneNumber) {
        if (!isViewAttached()) return;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 phoneNumber,
                 60,
                 TimeUnit.SECONDS,
                 (Activity) context,
                 mCallbacks,
                 mResendToken);
    }

    @Override
    public void verifyPhoneNumberWithCode(String code) {
        if (!isViewAttached()) return;
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        if (mAuth != null) {
            mAuth.signInWithCredential(credential)
                     .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()) {
                                 if (mView != null) {
                                     mView.codevalid();
                                 }

                             } else {
                                 if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                     if (mView != null) {
                                         mView.codeInvalid();
                                     }
                                 }
                             }
                         }
                     });
        }

    }
}
