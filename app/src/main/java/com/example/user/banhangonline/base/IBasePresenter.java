package com.example.user.banhangonline.base;

public interface IBasePresenter<V> {
    void attachView(V View);

    void detach();

    boolean isViewAttached();
}
