package com.example.user.banhangonline.screen.search;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.SearchSP;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public interface SearchContact {
    interface View extends IBaseView {
        void getFilterSuccess(List<SearchSP> searchSPList);

        void getFilterError();
    }

    interface Presenter extends IBasePresenter<View> {

        void getTextSearch(DatabaseReference databaseReference, String fileter);
    }

}
