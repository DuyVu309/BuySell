package com.example.user.banhangonline.screen.mySanPham;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

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

    }

    interface Presenter extends IBasePresenter<View> {
        void getListSanphamMyAccount(DatabaseReference databaseReference);

        void deleteSanPhamMyAccount(DatabaseReference databaseReference, StorageReference storageReference, SanPham sanPham);

        void getInfomationSuccess(DatabaseReference databaseReference);

        void updateInfomation(DatabaseReference databaseReference, Account account);

    }

}
