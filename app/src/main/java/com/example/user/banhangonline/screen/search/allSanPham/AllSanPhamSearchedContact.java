package com.example.user.banhangonline.screen.search.allSanPham;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public interface AllSanPhamSearchedContact {
    interface View extends IBaseView {

        void getKeySuccess();

        void getKeyError();

        void getSpSuccess(List<SanPham> searchSPS);

        void getSpError();
    }

    interface Presenter extends IBasePresenter<View> {

        void getAllKeySanPham(DatabaseReference databaseReference, String filter, String idCate, String idPart);

        void getAllSpWithFilterFromFirebase(DatabaseReference databaseReference, String filter, String idCate, String idPart);
    }

}
