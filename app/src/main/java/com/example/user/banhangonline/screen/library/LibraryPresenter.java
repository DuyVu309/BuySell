package com.example.user.banhangonline.screen.library;

import com.example.user.banhangonline.base.BasePresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibraryPresenter extends BasePresenter implements LibraryContact.Presenter {

    LibraryContact.View mView;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(LibraryContact.View View) {
        mView = View;
    }

    @Override
    public void detach() {
        mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public List<File> getListImageFromStorage(File file) {
        List<File> mList = new ArrayList<>();
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                mList.addAll(getListImageFromStorage(files[i]));
            } else {
                if (files[i].getName().endsWith(".jpg") || files[i].getName().endsWith(".png")) {
                    mList.add(files[i]);
                }
            }
        }
        return mList;
    }
}
