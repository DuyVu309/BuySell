package com.example.user.banhangonline.screen.register;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.google.firebase.auth.PhoneAuthCredential;

public interface RegisterContact {
    interface View extends IBaseView {
        void registerBuySuccess();

        void registerBuyError();

        void registerSellSuccess();

        void registerSellError();

        void verificationSuccess(PhoneAuthCredential credential);

        void sendingCode();

        void notifyErrorPhoneNumber();

        void notifyQuotaexceeded();

        void codevalid();

        void codeInvalid();

    }

    interface Presenter extends IBasePresenter<View> {
        void registerBuy(String name, String email, String password, String confirmPassword);

        void registerSell(String name, String email, String password, String confirmPassword);

        void startPhoneNumberVerification(String phoneNumber);

        void resendVerificationCode(String phoneNumber);

        void verifyPhoneNumberWithCode(String code);
    }
}


