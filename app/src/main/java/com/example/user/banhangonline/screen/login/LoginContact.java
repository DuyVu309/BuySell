package com.example.user.banhangonline.screen.login;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;

public interface LoginContact {
    interface View extends IBaseView {
        void signInSuccess();

        void signInError();

    }

    interface Presenter extends IBasePresenter<View> {
        void signInWithEmailPassword(String email, String password);
    }

}
