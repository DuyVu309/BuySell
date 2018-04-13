package com.example.user.banhangonline.screen.forgot;

import android.support.annotation.NonNull;

import com.example.user.banhangonline.base.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPresenter extends BasePresenter implements ForgotContact.Presenter {

    private ForgotContact.View mView;
    private FirebaseAuth firebaseAuth;

    public void setFirebaseAuth(FirebaseAuth auth){
        this.firebaseAuth = auth;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(ForgotContact.View View) {
        mView = View;
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
    public void sendPasswordResetEmail(String email) {
        if (firebaseAuth != null) {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mView.resetPasswordSuccess();
                    } else {
                        mView.resetPasswordFailed();
                    }
                }
            });
        }
    }
}
