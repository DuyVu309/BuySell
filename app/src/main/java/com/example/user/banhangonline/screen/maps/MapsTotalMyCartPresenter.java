package com.example.user.banhangonline.screen.maps;

public class MapsTotalMyCartPresenter implements MapsTotalMyCartContact.Presenter {

    MapsTotalMyCartContact.View mView;

    @Override
    public void attachView(MapsTotalMyCartContact.View View) {
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

}
