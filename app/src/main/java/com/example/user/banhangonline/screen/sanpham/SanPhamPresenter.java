package com.example.user.banhangonline.screen.sanpham;

import android.content.Context;
import android.util.Log;

import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.untils.KeyUntils.keyIdCategory;
import static com.example.user.banhangonline.untils.KeyUntils.keySanPham;

public class SanPhamPresenter implements SanPhamContact.Presenter {

    private Context context;
    private List<SanPham> sanPhamList = new ArrayList<>();
    private List<String> keyList = new ArrayList<>();
    boolean isLoadedWithLittle = false;
    private SanPhamContact.View mView;
    private int total = 0;

    public void setContext(Context context) {
        this.context = context;
    }

    public List<SanPham> getSanPhamList() {
        return sanPhamList;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public void attachView(SanPhamContact.View View) {
        mView = View;
    }

    @Override
    public void detach() {

    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public void getIdSanPhamFromFireBase(final DatabaseReference databaseReference, String idCate) {
        if (!isViewAttached()) return;
        if (context == null) return;

        databaseReference.child(keySanPham).orderByChild(keyIdCategory).equalTo(idCate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    keyList.add(snapshot.getKey().toString());
                }
                if (keyList != null) {
                    mView.getKeySuccess();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mView.getKeyError();
            }
        });
    }

    @Override
    public void getSanPhamFromFirebase(final DatabaseReference databaseReference, final String idCate) {
        if (!isViewAttached()) return;
        if (context == null) return;
        if (keyList.size() >= total + 10) {
            Log.d("TAG ID", idCate);
            databaseReference.child(keySanPham).orderByKey().startAt(keyList.get(total)).endAt(keyList.get(total + 9)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SanPham sanPham = snapshot.getValue(SanPham.class);
                        if (sanPham.getIdCategory().equals(idCate)) {
                            sanPhamList.add(sanPham);
                        }
                    }
                    if (sanPhamList != null) {
                        mView.getSpSuccess(sanPhamList);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mView.getSpError();
                }
            });

        } else if (keyList.size() < total + 10) {
            if (!isLoadedWithLittle) {
                databaseReference.child(keySanPham).orderByKey().startAt(keyList.get(total)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SanPham sanPham = snapshot.getValue(SanPham.class);
                            if (sanPham.getIdCategory().equals(idCate)) {
                                sanPhamList.add(sanPham);
                            }

                        }
                        if (sanPhamList != null) {
                            mView.getSpSuccess(sanPhamList);
                        }
                        isLoadedWithLittle = true;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }
    }
}
