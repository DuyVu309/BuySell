package com.example.user.banhangonline.screen.detail;

import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.example.user.banhangonline.utils.KeyUntils.keyAccount;

public class SanPhamDetailPresenter implements SanPhamDetailContact.Presenter {
    SanPham sanPham;
    SanPhamDetailContact.View mView;

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    @Override
    public void attachView(SanPhamDetailContact.View View) {
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
    public void getInfomationWithIdAccount(DatabaseReference databaseReference, String idAccount) {
        if (!isViewAttached()) return;
        databaseReference.child(keyAccount).child(idAccount).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);
                if (account != null) {
                    if (mView != null) {
                        mView.getInfoMationSuccess(account);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mView.getInfomationError();
            }
        });
    }
}
