package com.example.user.banhangonline.screen.sanphamWithIdCate;

import android.location.Location;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.google.firebase.database.DatabaseReference;

public interface SanPhamWithIdContact {

    interface View extends IBaseView {

        void getKeySuccess();

        void getKeyError();

        void getSpSuccess();

        void getSpError();

        void getSearchSuccess();

        void getSearchError();
    }

    interface Presenter extends IBasePresenter<View> {

        void getIdSanPhamFromFireBase(DatabaseReference databaseReference, String idCate, String part);

        void getSanPhamFromFirebase(DatabaseReference databaseReference, String iDCategory, String part, Location mLocation);

        void getListSearchFromFirebase(DatabaseReference databaseReference, String filter, String idCate, String idPart);
    }
}
