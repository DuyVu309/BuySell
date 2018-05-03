package com.example.user.banhangonline.screen.mySanPham;

import android.net.Uri;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public interface MySanPhamContact {

    interface View extends IBaseView {
        void getListSuccess();

        void getListError();

        void deleteSuscess();

        void deleteError();


        void getInfoSuccess(Account account);

        void getInfroError();

        void updateInfoSuccess();

        void updateInfoError();

        void uploadImageLansSuccess(String nameLans);

        void uploadImageAvtSuccess(String nameAvt);

        void uploadImageError();

        void displayPercent(String percent);
    }

    interface Presenter extends IBasePresenter<View> {
        void getListSanphamMyAccount(DatabaseReference databaseReference);

        void deleteSanPhamMyAccount(DatabaseReference databaseReference, StorageReference storageReference, SanPham sanPham);

        void getInfomationSuccess(DatabaseReference databaseReference);

        void updateInfomation(DatabaseReference databaseReference, Account account);

        void upLoadImageLanscapeToStorage(StorageReference storageReference, Account account, String imgLanscape);

        void upLoadImageAvtToStorage(StorageReference storageReference, Account account, String imgAvt);

    }

}
