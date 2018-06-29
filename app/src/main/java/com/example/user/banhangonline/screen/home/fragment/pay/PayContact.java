package com.example.user.banhangonline.screen.home.fragment.pay;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;
import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.DatabaseReference;

public interface PayContact {
    interface View extends IBaseView {
        void loadSanPhamSuccess(SanPham sanPham);

        void loadSanPhamError();

    }

    interface Presenter extends IBasePresenter<View> {
        void loadSanPhamFromFirebase(DatabaseReference database, String idCategory);
    }
}
