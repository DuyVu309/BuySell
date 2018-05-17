package com.example.user.banhangonline.screen.spAccount;

import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.utils.KeyUntils.keySanPham;

public class SanPhamAccountPresenter implements SanPhamAccountContact.Presenter {

    SanPhamAccountContact.View mView;
    private String emailId ;
    private List<SanPham> sanPhamList;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public List<SanPham> getSanPhamList() {
        return sanPhamList;
    }

    @Override
    public void attachView(SanPhamAccountContact.View View) {
        sanPhamList = new ArrayList<>();
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
    public void getSanPhamAccount(DatabaseReference databaseReference, final String emailID) {
        if (!isViewAttached()) return;
        sanPhamList.clear();
        databaseReference.child(keySanPham).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                if (sanPham.getIdNguoiban() != null && sanPham.getIdNguoiban().equals(emailID)) {
                    sanPhamList.add(sanPham);
                }

                if (sanPhamList != null) {
                    if (mView != null) {
                        mView.getSanPhamSuccess();
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
                    mView.getSanPhamError();
                }
            }
        });
    }
}
