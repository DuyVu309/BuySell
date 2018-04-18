package com.example.user.banhangonline.screen.sell;

import android.util.Log;

import com.example.user.banhangonline.base.BasePresenter;
import com.example.user.banhangonline.model.Categories;
import com.example.user.banhangonline.model.Part;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.untils.KeyUntils.keyCategory;
import static com.example.user.banhangonline.untils.KeyUntils.keyDoNguNoiY;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateCongNghe;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateDoAn;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateDoChoi;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateGiaDung;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateHocTap;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateKhac;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateMyPham;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCatePhuKien;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateThoiTrang;
import static com.example.user.banhangonline.untils.KeyUntils.keybeNam;
import static com.example.user.banhangonline.untils.KeyUntils.keybeNu;
import static com.example.user.banhangonline.untils.KeyUntils.keygiayNam;
import static com.example.user.banhangonline.untils.KeyUntils.keygiayNu;
import static com.example.user.banhangonline.untils.KeyUntils.keyphuKienNam;
import static com.example.user.banhangonline.untils.KeyUntils.keyphuKienNu;
import static com.example.user.banhangonline.untils.KeyUntils.keythoiTrangNam;
import static com.example.user.banhangonline.untils.KeyUntils.keythoiTrangNu;
import static com.example.user.banhangonline.untils.KeyUntils.keytuiSachNam;
import static com.example.user.banhangonline.untils.KeyUntils.keytuiSachNu;
import static com.example.user.banhangonline.untils.KeyUntils.titleDienTu;
import static com.example.user.banhangonline.untils.KeyUntils.titleDoAn;
import static com.example.user.banhangonline.untils.KeyUntils.titleDochoi;
import static com.example.user.banhangonline.untils.KeyUntils.titleGiaDung;
import static com.example.user.banhangonline.untils.KeyUntils.titleGiaHocTap;
import static com.example.user.banhangonline.untils.KeyUntils.titleKhac;
import static com.example.user.banhangonline.untils.KeyUntils.titleMyPham;
import static com.example.user.banhangonline.untils.KeyUntils.titlePhuKien;
import static com.example.user.banhangonline.untils.KeyUntils.titleThoiTrang;
import static com.example.user.banhangonline.untils.TextUntils.doNguNoiY;
import static com.example.user.banhangonline.untils.TextUntils.giayNam;
import static com.example.user.banhangonline.untils.TextUntils.giayNu;
import static com.example.user.banhangonline.untils.TextUntils.phuKienNam;
import static com.example.user.banhangonline.untils.TextUntils.phuKienNu;
import static com.example.user.banhangonline.untils.TextUntils.thoiTrangBeGai;
import static com.example.user.banhangonline.untils.TextUntils.thoiTrangBeNam;
import static com.example.user.banhangonline.untils.TextUntils.thoiTrangNam;
import static com.example.user.banhangonline.untils.TextUntils.thoiTrangNu;
import static com.example.user.banhangonline.untils.TextUntils.tuiSachNam;
import static com.example.user.banhangonline.untils.TextUntils.tuiSachNu;

public class SellPresenter extends BasePresenter implements SellContact.Presenterr {

    private SellContact.View mView;
    private List<Object> listCategories;
    private List<Object> listPart;

    public List<Object> getListCategory() {
        listCategories.add(new Categories(keyIdCateDoAn, titleDoAn));
        listCategories.add(new Categories(keyIdCateMyPham, titleMyPham));
        listCategories.add(new Categories(keyIdCateThoiTrang, titleThoiTrang));
        listCategories.add(new Categories(keyIdCateCongNghe, titleDienTu));
        listCategories.add(new Categories(keyIdCatePhuKien, titlePhuKien));
        listCategories.add(new Categories(keyIdCateGiaDung, titleGiaDung));
        listCategories.add(new Categories(keyIdCateHocTap, titleGiaHocTap));
        listCategories.add(new Categories(keyIdCateDoChoi, titleDochoi));
        listCategories.add(new Categories(keyIdCateKhac, titleKhac));
        return listCategories;
    }

    public List<Object> getListPart() {

        if (listPart != null) {
            listPart.clear();
        }
        listPart.add(new Part(keyIdCateThoiTrang, keythoiTrangNam, 0, thoiTrangNam));
        listPart.add(new Part(keyIdCateThoiTrang, keygiayNam, 0, giayNam));
        listPart.add(new Part(keyIdCateThoiTrang, keytuiSachNam, 0, tuiSachNam));
        listPart.add(new Part(keyIdCateThoiTrang, keyphuKienNam, 0, phuKienNam));
        listPart.add(new Part(keyIdCateThoiTrang, keybeNam, 0, thoiTrangBeNam));

        listPart.add(new Part(keyIdCateThoiTrang, keythoiTrangNu, 0, thoiTrangNu));
        listPart.add(new Part(keyIdCateThoiTrang, keyDoNguNoiY, 0, doNguNoiY));
        listPart.add(new Part(keyIdCateThoiTrang, keygiayNu, 0, giayNu));
        listPart.add(new Part(keyIdCateThoiTrang, keytuiSachNu, 0, tuiSachNu));
        listPart.add(new Part(keyIdCateThoiTrang, keyphuKienNu, 0, phuKienNu));
        listPart.add(new Part(keyIdCateThoiTrang, keybeNu, 0, thoiTrangBeGai));
        return listPart != null ? listPart : null;
    }

    @Override
    public void onCreate() {
        listCategories = new ArrayList<>();
        listPart = new ArrayList<>();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(SellContact.View View) {
        mView = View;
    }

    @Override
    public void detach() {
        mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return false;
    }
}
