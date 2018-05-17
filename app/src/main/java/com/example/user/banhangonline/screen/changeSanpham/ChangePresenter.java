package com.example.user.banhangonline.screen.changeSanpham;

import android.support.annotation.NonNull;

import com.example.user.banhangonline.base.BasePresenter;
import com.example.user.banhangonline.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import static com.example.user.banhangonline.utils.KeyUntils.keyIdSanPham;
import static com.example.user.banhangonline.utils.KeyUntils.keySanPham;

public class ChangePresenter extends BasePresenter implements ChangeSanPhamContact.Presenter {

    ChangeSanPhamContact.View mView;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(ChangeSanPhamContact.View View) {
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
    public void updateSanPham(final DatabaseReference databaseReference, final SanPham sanPham) {
        if (!isViewAttached()) {
            return;
        }
        databaseReference.child(keySanPham).orderByChild(keyIdSanPham).equalTo(sanPham.getIdSanPham()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    databaseReference.child(keySanPham).child(key).setValue(sanPham).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (mView != null) {
                                    mView.updateSanPhamSuccess();
                                }
                            } else {
                                if (mView != null) {
                                    mView.updateSanPhamError();
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void updateListImage(StorageReference storageReference, SanPham sanPham) {
        if (!isViewAttached()) {
            return;
        }

    }
}
