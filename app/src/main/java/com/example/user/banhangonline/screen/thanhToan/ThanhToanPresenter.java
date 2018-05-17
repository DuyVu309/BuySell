package com.example.user.banhangonline.screen.thanhToan;

import android.support.annotation.NonNull;

import com.example.user.banhangonline.model.DonHang;
import com.example.user.banhangonline.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import static com.example.user.banhangonline.utils.KeyUntils.keyDonHang;

public class ThanhToanPresenter implements ThanhToanContact.Presenter{

    ThanhToanContact.View mView;

    private SanPham sanPham;

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    @Override
    public void attachView(ThanhToanContact.View View) {
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
    public void pushDonHangToFirebase(DatabaseReference databaseReference, DonHang donHang) {
        if (!isViewAttached()) return;
        databaseReference.child(keyDonHang).push().setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (mView != null) {
                        mView.pushDonHangSuccess();
                    }
                } else {
                    mView.pushDonHangError();
                }
            }
        });

    }
}
