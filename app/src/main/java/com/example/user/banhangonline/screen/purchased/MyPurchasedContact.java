package com.example.user.banhangonline.screen.purchased;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.DonHang;
import com.google.firebase.database.DatabaseReference;

public interface MyPurchasedContact {
    //san pham minh da mua
    interface View extends IBaseView {

        void getPurchsedSuccess();

        void getPurchsedError();

        void deletePurchsedSuccess();

        void deletePurchsedError();
    }

    interface Presenter extends IBasePresenter<View> {

        void getListPurchsedFromFirebase(DatabaseReference databaseReference);

        void deletePurchsed(DatabaseReference databaseReference, DonHang donHang);
    }
}
