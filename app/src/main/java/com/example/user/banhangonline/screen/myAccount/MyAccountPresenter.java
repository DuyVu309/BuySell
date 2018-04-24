package com.example.user.banhangonline.screen.myAccount;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.user.banhangonline.base.BasePresenter;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.untils.KeyUntils.keyIdSanPham;
import static com.example.user.banhangonline.untils.KeyUntils.keySanPham;

public class MyAccountPresenter extends BasePresenter implements MyAccountContact.Presenter {
    private Context context;
    private List<SanPham> sanPhamList = new ArrayList<>();
    private MyAccountContact.View mView;

    public void setContext(Context context) {
        this.context = context;
    }

    public List<SanPham> getSanPhamList() {
        return sanPhamList;
    }

    @Override
    public void attachView(MyAccountContact.View View) {
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
    public void getListSanphamMyAccount(DatabaseReference databaseReference) {
        if (!isViewAttached()) return;
        if (context == null) return;
        sanPhamList.clear();
        databaseReference.child(keySanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham sanPham = snapshot.getValue(SanPham.class);
                    if (PreferManager.getEmailID(context).equals(sanPham.getIdNguoiban())) {
                        sanPhamList.add(sanPham);
                    }
                }
                if (mView != null) {
                    mView.getListSuccess();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mView.getListError();
            }
        });
    }

    @Override
    public void deleteSanPhamMyAccount(final DatabaseReference databaseReference, StorageReference storageReference, SanPham sanPham) {
        if (!isViewAttached()) {
            return;
        }
        for (int i = 0; i < sanPham.getListFiles().getListName().size(); i++) {
            storageReference.child("images/" + sanPham.getListFiles().getListName().get(i)).delete();
        }
        databaseReference.child(keySanPham).orderByChild(keyIdSanPham).equalTo(sanPham.getIdSanPham()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    databaseReference.child(keySanPham).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mView.deleteSuscess();
                            } else {
                                mView.deleteError();
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
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}
