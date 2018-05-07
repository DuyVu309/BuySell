package com.example.user.banhangonline.screen.sanphamWithId;

import android.content.Context;

import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.model.SearchSP;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.untils.KeyUntils.keyIdCategory;
import static com.example.user.banhangonline.untils.KeyUntils.keySanPham;
import static com.example.user.banhangonline.untils.KeyUntils.keybeNu;

public class SanPhamWithIdPresenter implements SanPhamWithIdContact.Presenter {

    private Context context;
    private List<SanPham> sanPhamList = new ArrayList<>();
    private List<String> keyList = new ArrayList<>();
    private List<SearchSP> searchList = new ArrayList<>();
    boolean isLoadedWithLittle = false;
    private SanPhamWithIdContact.View mView;
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

    public List<SearchSP> getSearchList() {
        return searchList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public void attachView(SanPhamWithIdContact.View View) {
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
            databaseReference.child(keySanPham).orderByKey().startAt(keyList.get(total)).endAt(keyList.get(total + 9)).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    if (sanPham.getIdCategory().equals(idCate)) {
                        sanPhamList.add(sanPham);
                        searchList.add(new SearchSP(sanPham.getIdSanPham(), sanPham.getHeader()));
                    }
                    if (sanPhamList != null) {
                        if (mView != null) {
                            mView.getSpSuccess(sanPhamList);
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

                }
            });

        } else if (keyList.size() < total + 10) {
            if (!isLoadedWithLittle) {
                databaseReference.child(keySanPham).orderByKey().startAt(keyList.get(total)).endAt(keyList.get(total + 9)).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                        if (sanPham.getIdCategory().equals(idCate)) {
                            sanPhamList.add(sanPham);
                            searchList.add(new SearchSP(sanPham.getIdSanPham(), sanPham.getHeader()));
                        }
                        if (sanPhamList != null) {
                            if (mView != null) {
                                mView.getSpSuccess(sanPhamList);
                            }
                        }
                        isLoadedWithLittle = true;
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

                    }
                });
            }
        }
    }
}
