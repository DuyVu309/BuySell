package com.example.user.banhangonline.screen.home;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;

public interface HomeContact {
    interface View extends IBaseView {
        void loadPaySuccess();

        void loadPayError();
    }

    interface Presenter extends IBasePresenter<View> {
        void loadPay();
    }
}
