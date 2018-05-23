package com.example.user.banhangonline.screen.search;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public interface SearchContact {
    interface View extends IBaseView {
        void getFilterSuccess(List<Object> searchSPList);

        void getFilterError();
    }

    interface Presenter extends IBasePresenter<View> {

        void getTextSearchProduct(DatabaseReference databaseReference, String fileter);

        void getTextSearchAccount(DatabaseReference databaseReference, String name);
    }

}
