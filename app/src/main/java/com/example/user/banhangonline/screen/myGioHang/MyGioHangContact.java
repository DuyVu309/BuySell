package com.example.user.banhangonline.screen.myGioHang;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.DonHang;
import com.google.firebase.database.DatabaseReference;

public interface MyGioHangContact {

    //san pham minh da mua
    interface View extends IBaseView {

        void getCartSuccess();

        void getCartError();

        void deleteDonHangSuccess();

        void deleteDonHangError();
    }

    interface Presenter extends IBasePresenter<View> {

        void getListCartFromFirebase(DatabaseReference databaseReference);

        void deleteDonHang(DatabaseReference databaseReference, DonHang donHang);
    }

}
