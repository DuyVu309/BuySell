package com.example.user.banhangonline.screen.login;

import android.util.Log;
import com.example.user.banhangonline.base.BasePresenter;

public class LoginPresenter extends BasePresenter implements LoginContact.Presenter {

    private LoginContact.View mView;
    private String idBuySell;
    private String name;
    private String phoneNumber;

    public String getIdBuySell() {
        return idBuySell;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setIdBuySell(String idBuySell) {
        this.idBuySell = idBuySell;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void onCreate() {
        Log.d("TAG", "onCreate Login");
    }

    @Override
    public void onDestroy() {
        Log.d("TAG", "onDestroy Login");
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
    public void signInWithEmailPassword(String email, String password) {
        if (!isViewAttached()) return;
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                 && password.length() >= 6) {
            if (mView != null) {
                mView.signInSuccess();
            }
        } else {
            if (mView != null) {
                mView.signInError();
            }

        }
    }

}
