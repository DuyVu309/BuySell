package com.example.user.banhangonline.screen.account;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.google.firebase.database.DatabaseReference;

public interface SearchAccountContact {
    interface View extends IBaseView {

        void getKeySuccess();

        void getKeyError();

        void getListAccountSuccess();

        void getListAccountError();
    }

    interface Presenter extends IBasePresenter<View> {

        void getKeyAccount(DatabaseReference databaseReference, String filter);

        void getListAccuntWithFilter(DatabaseReference databaseReference, String filter);
    }

}
