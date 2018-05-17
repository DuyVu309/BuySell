package com.example.user.banhangonline.screen.home.fragment.pay;

import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.utils.KeyUntils.keyIdCategory;
import static com.example.user.banhangonline.utils.KeyUntils.keySanPham;

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
        sanPhamList.clear();
        database.child(keySanPham).orderByChild(keyIdCategory).equalTo(idCategory).limitToLast(100).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                if (sanPham != null && sanPham.getIdCategory() != null) {
                    if (sanPham.getIdCategory().equals(idCategory)) {
                        sanPhamList.add(sanPham);
                    }
                }

                if (sanPhamList != null) {
                    if (mView != null) {
                        mView.loadSanPhamSuccess();
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
                    mView.loadSanPhamError();
                }
            }
        });
    }

}
