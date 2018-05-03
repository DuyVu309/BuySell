package com.example.user.banhangonline.screen.home.fragment.pay;

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

public class PayPresenter implements PayContact.Presenter {

    private List<SanPham> sanPhamList = new ArrayList<>();
    private PayContact.View mView;
    private int position = 0;

    public List<SanPham> getSanPhamList() {
        return sanPhamList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void attachView(PayContact.View View) {
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
    public void loadSanPhamFromFirebase(final DatabaseReference database, final String idCategory) {
        if (!isViewAttached()) {
            return;
        }
        database.child(keySanPham).orderByChild(keyIdCategory).equalTo(idCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    database.child(keySanPham).child(snapshot.getKey()).limitToLast(100).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                            if (sanPham != null && sanPham.getIdCategory() != null) {
                                if (sanPham.getIdCategory().equals(idCategory)) {
                                    sanPhamList.add(sanPham);
                                }
                            }

                            if (mView != null) {
                                if (sanPhamList.size() > 0) {
                                    mView.loadSanPhamSuccess();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mView.loadSanPhamSuccess();
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mView.loadSanPhamError();
            }
        });

    }

}
