package com.example.user.banhangonline.screen.login;

import android.util.Log;

import com.example.user.banhangonline.base.BasePresenter;

public class LoginPresenter extends BasePresenter implements LoginContact.Presenter {

    private LoginContact.View mView;

    @Override
    public void onCreate() {
        Log.d("TAG", "OK");
    }

    @Override
    public void onDestroy() {
        Log.d("TAG", "ERROR");
    }

    @Override
    public void attachView(LoginContact.View View) {
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
    public void signInWithEmailPassword(String number, String email, String password) {
        if (!isViewAttached()) return;
        if (number.matches("\\d+(?:\\.\\d+)?")
                 && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                 && password.length() >= 6) {
            mView.signInSuccess();
        } else {
            mView.signInError();
        }
    }
}
