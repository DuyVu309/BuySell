package com.example.user.banhangonline.screen.sanphamWithId;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public interface SanPhamWithIdContact {

    interface View extends IBaseView {

        void getKeySuccess();

        void getKeyError();

        void getSpSuccess(List<SanPham> sanPham);

        void getSpError();
    }

    interface Presenter extends IBasePresenter<View> {

        void getIdSanPhamFromFireBase(DatabaseReference databaseReference, String idCate);

        void getSanPhamFromFirebase(DatabaseReference databaseReference, String iDCategory);
    }
}
