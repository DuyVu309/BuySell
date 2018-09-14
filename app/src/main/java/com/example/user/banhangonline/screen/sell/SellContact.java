package com.example.user.banhangonline.screen.sell;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public interface SellContact {
    interface View extends IBaseView {
        void upLoadToFirebaseSuccess();

        void upLoadToFirebaseError();

        void upLoadImagesSuccess();

        void upLoadImagErrror();

        void displayPercent(String percent);
    }

    interface Presenterr extends IBasePresenter<View> {
        void upLoadSanPhamToFirebase(DatabaseReference database, SanPham sanPham);

        void upLoadFileImageToStorage(StorageReference storageReference, String nameImage);
    }

}
