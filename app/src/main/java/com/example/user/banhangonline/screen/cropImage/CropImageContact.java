package com.example.user.banhangonline.screen.cropImage;

import android.graphics.Bitmap;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.Account;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public interface CropImageContact {

    interface View extends IBaseView {

        void uploadImageAvtSuccess();

        void uploadImageAvtError();

        void uploadImageLansCapeSuccess();

        void uploadImageLanscapeError();

        void upLoading();

        void updateInfomationSuccess();

        void updateInfomationError();
    }


    interface Presenter extends IBasePresenter<View> {
        void uploadImageAvtToFirebase(StorageReference storageReference, Account account, Bitmap bmAvt);

        void uploadImageLanscapeToFirebase(StorageReference storageReference, Account account, Bitmap bmLans);

        void updateInfomationToFirebase(DatabaseReference databaseReference, Account account);
    }
}

