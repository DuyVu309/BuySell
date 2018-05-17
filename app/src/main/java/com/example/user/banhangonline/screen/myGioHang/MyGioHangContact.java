package com.example.user.banhangonline.screen.myGioHang;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.google.firebase.database.DatabaseReference;

public interface MyGioHangContact {

    //san pham minh da mua
    interface View extends IBaseView {

        void getAllKeySuccess();

        void getAllKeyError();

        void getCartSuccess();

        void getCartError();
    }

    interface Presenter extends IBasePresenter<View> {

        void getAllKeyMyCartFromFirebase(DatabaseReference databaseReference, String idNguoiBan);

        void getListCartFromFirebase(DatabaseReference databaseReference);
    }

}
