package com.example.user.banhangonline;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Demo extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private FirebaseAuth mAuth;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    private TextView mStatusText;
    private TextView mDetailText;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;

    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private Button mSignOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        // Assign views

        mStatusText = findViewById(R.id.tv_status);
        mDetailText = findViewById(R.id.tv_detail);

        mPhoneNumberField = findViewById(R.id.edt_number_phone);
        mVerificationField = findViewById(R.id.edt_verify);

        mStartButton = findViewById(R.id.btn_mStartButton);
        mVerifyButton = findViewById(R.id.btn_mVerifyButton);
        mResendButton = findViewById(R.id.btn_mResendButton);
        mSignOutButton = findViewById(R.id.btn_mSignOutButton);

        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                updateUI(STATE_VERIFY_SUCCESS, credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Số điện thoại không hợp lệ", Snackbar.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                             Snackbar.LENGTH_SHORT).show();
                }

                updateUI(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;

                updateUI(STATE_CODE_SENT);
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 phoneNumber,        // Phone number to verify
                 60,                 // Timeout duration
                 TimeUnit.SECONDS,   // Unit of timeout
                 this,               // Activity (for callback binding)
                 mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 phoneNumber,        // Phone number to verify
                 60,                 // Timeout duration
                 TimeUnit.SECONDS,   // Unit of timeout
                 this,               // Activity (for callback binding)
                 mCallbacks,         // OnVerificationStateChangedCallbacks
                 token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             FirebaseUser user = task.getResult().getUser();
                             updateUI(STATE_SIGNIN_SUCCESS, user);
                         } else {
                             if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                 mVerificationField.setError("Invalid code.");
                             }
                             updateUI(STATE_SIGNIN_FAILED);
                         }
                     }
                 });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                enableViews(mStartButton, mPhoneNumberField);
                disableViews(mVerifyButton, mResendButton, mVerificationField);
                mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                disableViews(mStartButton);
                mDetailText.setText("status");
                break;
            case STATE_VERIFY_FAILED:
                enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                         mVerificationField);
                mDetailText.setText("verify");
                break;
            case STATE_VERIFY_SUCCESS:
                disableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                         mVerificationField);
                mDetailText.setText("sucess");

                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText("validation");
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                mDetailText.setText("sign failed");
                break;
            case STATE_SIGNIN_SUCCESS:
                break;
        }

        if (user == null) {

            mStatusText.setText("sign out");
        } else {
            enableViews(mPhoneNumberField, mVerificationField);
            mPhoneNumberField.setText(null);
            mVerificationField.setText(null);

            mStatusText.setText("signed in");
            mDetailText.setText("firebase_status" + user.getUid());
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mStartButton:
                if (!validatePhoneNumber()) {
                    return;
                }

                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                break;
            case R.id.btn_mVerifyButton:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.btn_mResendButton:
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                break;
            case R.id.btn_mSignOutButton:
                signOut();
                break;
        }
    }
}
