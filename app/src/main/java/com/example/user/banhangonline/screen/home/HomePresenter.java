package com.example.user.banhangonline.screen.home;

import android.content.Context;

import com.example.user.banhangonline.base.BasePresenter;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.Categories;
import com.example.user.banhangonline.model.Pay;
import com.example.user.banhangonline.model.search.SearchSP;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.utils.KeyUntils.keyAccount;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateCongNghe;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateDoAn;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateDoChoi;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateGiaDung;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateHocTap;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateKhac;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCatePhuKien;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateThoiTrang;
import static com.example.user.banhangonline.utils.KeyUntils.titleDienTu;
import static com.example.user.banhangonline.utils.KeyUntils.titleDoAn;
import static com.example.user.banhangonline.utils.KeyUntils.titleDochoi;
import static com.example.user.banhangonline.utils.KeyUntils.titleGiaDung;
import static com.example.user.banhangonline.utils.KeyUntils.titleGiaHocTap;
import static com.example.user.banhangonline.utils.KeyUntils.titleHome;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdCateMyPham;
import static com.example.user.banhangonline.utils.KeyUntils.titleKhac;
import static com.example.user.banhangonline.utils.KeyUntils.titleMyPham;
import static com.example.user.banhangonline.utils.KeyUntils.titlePhuKien;
import static com.example.user.banhangonline.utils.KeyUntils.titleThoiTrang;

public class HomePresenter extends BasePresenter implements HomeContact.Presenter {

    private Context context;
    private HomeContact.View mView;
    private List<Categories> listCategories;

    public List<Categories> getListCategories() {
        return listCategories;
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


    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        listCategories = new ArrayList<>();
        initCategories();
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
    public void getInfomationAccount(DatabaseReference databaseReference) {
        if (!isViewAttached()) {
            return;
        }
        if (keyAccount != null && PreferManager.getUserID(context) != null) {
            databaseReference.child(keyAccount).child(PreferManager.getUserID(context)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Account account = dataSnapshot.getValue(Account.class);
                    if (account != null) {
                        if (mView != null) {
                            mView.getInfoSuccess(account);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (mView != null) {
                        mView.getInfoError();
                    }

                }
            });
        }
    }

}
