package com.example.user.banhangonline.screen.search;

import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.model.SearchSP;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.untils.KeyUntils.keySanPham;

public class SearchPresenter implements SearchContact.Presenter {
    SearchContact.View mView;
    private List<SearchSP> listSearchs;

    public List<SearchSP> getListSearchs() {
        return listSearchs;
    }

    @Override
    public void attachView(SearchContact.View View) {
        listSearchs = new ArrayList<>();
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
    public void getTextSearch(DatabaseReference databaseReference, final String filter) {
        if (!isViewAttached()) {
            return;
        }
        listSearchs.clear();
        databaseReference.child(keySanPham).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                if (sanPham != null) {
                    if (sanPham.getHeader() != null && sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                        listSearchs.add(new SearchSP(sanPham.getIdSanPham(), sanPham.getHeader()));
                    }
                    else if (sanPham.getMota() != null && sanPham.getMota().toLowerCase().contains(filter.toLowerCase())) {
                        listSearchs.add(new SearchSP(sanPham.getIdSanPham(), sanPham.getHeader()));
                    }
                    else if (sanPham.getNameNguoiBan() != null && sanPham.getNameNguoiBan().toLowerCase().contains(filter.toLowerCase())) {
                        listSearchs.add(new SearchSP(sanPham.getIdSanPham(), sanPham.getHeader()));
                    }
                }
                if (listSearchs != null) {
                    if (mView != null) {
                        mView.getFilterSuccess(listSearchs);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mView != null) {
                    mView.getFilterError();
                }
            }
        });
    }
}
