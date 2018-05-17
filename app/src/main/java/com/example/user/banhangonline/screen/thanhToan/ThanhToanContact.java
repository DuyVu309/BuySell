package com.example.user.banhangonline.screen.thanhToan;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.DonHang;
import com.google.firebase.database.DatabaseReference;

public interface ThanhToanContact {

    interface View extends IBaseView {
        void pushDonHangSuccess();

        void pushDonHangError();
    }

    interface Presenter extends IBasePresenter<View> {
        void pushDonHangToFirebase(DatabaseReference databaseReference, DonHang donHang);
    }

}
