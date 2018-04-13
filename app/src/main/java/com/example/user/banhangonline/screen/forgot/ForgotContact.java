package com.example.user.banhangonline.screen.forgot;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;

public interface ForgotContact {

    interface View extends IBaseView{
        void resetPasswordSuccess();

        void resetPasswordFailed();
    }

    interface Presenter extends IBasePresenter<View> {
        void sendPasswordResetEmail(String email);
    }

}
