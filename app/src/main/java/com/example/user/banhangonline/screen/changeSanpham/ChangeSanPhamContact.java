package com.example.user.banhangonline.screen.changeSanpham;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public interface ChangeSanPhamContact {

    interface View extends IBaseView {

        void updateSanPhamSuccess();

        void updateSanPhamError();

        void updateListImageSuccess();

        void updateListImageError();
    }

    interface Presenter extends IBasePresenter<View> {

        void updateSanPham(DatabaseReference databaseReference,SanPham sanPham);

        void updateListImage(StorageReference storageReference, SanPham sanPham);
    }

}
