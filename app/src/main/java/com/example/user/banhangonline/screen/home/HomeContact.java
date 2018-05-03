package com.example.user.banhangonline.screen.home;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.Account;
import com.google.firebase.database.DatabaseReference;

public interface HomeContact {
    interface View extends IBaseView {

        void getInfoSuccess(Account account);

        void getInfoError();
    }

    interface Presenter extends IBasePresenter<View> {

        void getInfomationAccount(DatabaseReference databaseReference);

        void loadPay();
    }
}
