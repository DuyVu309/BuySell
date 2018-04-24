package com.example.user.banhangonline.screen.myAccount;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public interface MyAccountContact {

    interface View extends IBaseView{
        void getListSuccess();

        void getListError();

        void deleteSuscess();

        void deleteError();
    }

    interface Presenter extends IBasePresenter<View> {
        void getListSanphamMyAccount(DatabaseReference databaseReference);

        void deleteSanPhamMyAccount(DatabaseReference databaseReference, StorageReference storageReference, SanPham sanPham);
    }

}
