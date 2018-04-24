package com.example.user.banhangonline.screen.home.fragment.pay;

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

    public List<SanPham> getSanPhamList() {
        return sanPhamList;
    }

    public void setSanPhamList(List<SanPham> sanPhamList) {
        this.sanPhamList = sanPhamList;
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
    public void loadSanPhamFromFirebase(final DatabaseReference database, String idCategory) {
        if (!isViewAttached()) {
            return;
        }
        sanPhamList.clear();
        database.child(keySanPham).orderByChild(keyIdCategory).equalTo(idCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    database.child(keySanPham).child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                            sanPhamList.add(sanPham);
                            if (sanPhamList != null) {
                                if (sanPhamList.size() > 0) {
                                    if (mView != null) {
                                        mView.loadSanPhamSuccess();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mView.loadSanPhamError();
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
