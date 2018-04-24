package com.example.user.banhangonline.screen.detail;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.Account;
import com.google.firebase.database.DatabaseReference;

public interface SanPhamDetailContact {
    interface View extends IBaseView{

        void getInfoMationSuccess(Account account);

        void getInfomationError();

    }

    interface Presenter extends IBasePresenter<View> {

        void getInfomationWithIdAccount(DatabaseReference databaseReference, String idAccount);
    }

}
