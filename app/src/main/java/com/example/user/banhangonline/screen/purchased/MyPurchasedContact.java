package com.example.user.banhangonline.screen.purchased;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.DonHang;
import com.example.user.banhangonline.screen.myGioHang.MyGioHangContact;
import com.google.firebase.database.DatabaseReference;

public interface MyPurchasedContact {
    //san pham minh da mua
    interface View extends IBaseView {

        void getAllKeySuccess();

        void getAllKeyError();

        void getPurchsedSuccess();

        void getPurchsedError();

        void deletePurchsedSuccess();

        void deletePurchsedError();
    }

    interface Presenter extends IBasePresenter<MyGioHangContact.View> {

        void getAllKeyMyCartFromFirebase(DatabaseReference databaseReference, String idNguoiBan);

        void getListPurchsedFromFirebase(DatabaseReference databaseReference);

        void deletePurchsed(DatabaseReference databaseReference, DonHang donHang);
    }
}
