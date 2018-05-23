package com.example.user.banhangonline.screen.spAccount;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.google.firebase.database.DatabaseReference;

public interface SanPhamAccountContact {
    interface View extends IBaseView {

        void getInfoSuccess();

        void getInfoError();

        void getSanPhamSuccess();

        void getSanPhamError();
    }

    interface Presenter extends IBasePresenter<View> {
        void getInfoAccount(DatabaseReference databaseReference, String userId);

        void getSanPhamAccount(DatabaseReference databaseReference, String emailID);
    }

}
