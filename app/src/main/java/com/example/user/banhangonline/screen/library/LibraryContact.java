package com.example.user.banhangonline.screen.library;

import com.example.user.banhangonline.base.IBasePresenter;
import com.example.user.banhangonline.base.IBaseView;

import java.io.File;
import java.util.List;

public interface LibraryContact {

    interface View extends IBaseView{

    }

    interface Presenter extends IBasePresenter<View> {

        List<File> getListImageFromStorage(File file);
    }


}
