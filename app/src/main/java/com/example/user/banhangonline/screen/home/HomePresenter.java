package com.example.user.banhangonline.screen.home;


import com.example.user.banhangonline.base.BasePresenter;
import com.example.user.banhangonline.model.Categories;
import com.example.user.banhangonline.model.Pay;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateCongNghe;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateDoAn;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateDoChoi;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateGiaDung;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateHocTap;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateKhac;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCatePhuKien;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateThoiTrang;
import static com.example.user.banhangonline.untils.KeyUntils.titleDienTu;
import static com.example.user.banhangonline.untils.KeyUntils.titleDoAn;
import static com.example.user.banhangonline.untils.KeyUntils.titleDochoi;
import static com.example.user.banhangonline.untils.KeyUntils.titleGiaDung;
import static com.example.user.banhangonline.untils.KeyUntils.titleGiaHocTap;
import static com.example.user.banhangonline.untils.KeyUntils.titleHome;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateMyPham;
import static com.example.user.banhangonline.untils.KeyUntils.titleKhac;
import static com.example.user.banhangonline.untils.KeyUntils.titleMyPham;
import static com.example.user.banhangonline.untils.KeyUntils.titlePhuKien;
import static com.example.user.banhangonline.untils.KeyUntils.titleThoiTrang;

public class HomePresenter extends BasePresenter implements HomeContact.Presenter {

    private HomeContact.View mView;
    private List<Categories> listCategories;
    private List<Pay> listPays;

    public List<Categories> getListCategories() {
        return listCategories;
    }

    public List<Pay> getListPays() {
        return listPays;
    }

    private void initCategories() {
        listCategories.add(new Categories("", titleHome));
        listCategories.add(new Categories(keyIdCateDoAn, titleDoAn));
        listCategories.add(new Categories(keyIdCateMyPham, titleMyPham));
        listCategories.add(new Categories(keyIdCateThoiTrang, titleThoiTrang));
        listCategories.add(new Categories(keyIdCateCongNghe, titleDienTu));
        listCategories.add(new Categories(keyIdCatePhuKien, titlePhuKien));
        listCategories.add(new Categories(keyIdCateGiaDung, titleGiaDung));
        listCategories.add(new Categories(keyIdCateHocTap, titleGiaHocTap));
        listCategories.add(new Categories(keyIdCateDoChoi, titleDochoi));
        listCategories.add(new Categories(keyIdCateKhac, titleKhac));
    }

    @Override
    public void onCreate() {
        listCategories = new ArrayList<>();
        initCategories();
        listPays = new ArrayList<>();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(HomeContact.View View) {
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
    public void loadPay() {

    }
}
