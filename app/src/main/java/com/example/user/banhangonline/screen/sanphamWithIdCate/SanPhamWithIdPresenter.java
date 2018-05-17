package com.example.user.banhangonline.screen.sanphamWithIdCate;

import android.util.Log;

import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.model.search.SearchSP;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.utils.KeyUntils.keySanPham;

public class SanPhamWithIdPresenter implements SanPhamWithIdContact.Presenter {

    private List<SanPham> sanPhamList = new ArrayList<>();
    private List<String> keyList = new ArrayList<>();
    private List<Object> searchList = new ArrayList<>();
    boolean isLoadedWithLittle = false;
    private SanPhamWithIdContact.View mView;
    private int total = 0;


    public List<SanPham> getSanPhamList() {
        return sanPhamList;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public List<Object> getSearchList() {
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
    public void getIdSanPhamFromFireBase(final DatabaseReference databaseReference, final String idCate, final String idPart) {
        if (!isViewAttached()) return;
        databaseReference.child(keySanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham sanPham = snapshot.getValue(SanPham.class);
                    if (sanPham != null) {
                        if (idPart != null && sanPham.getIdPart() != null) {
                            if (sanPham.getIdCategory().equals(idCate)) {
                                if (sanPham.getIdPart().equals(idPart)) {
                                    keyList.add(snapshot.getKey().toString());
                                }
                            }
                        } else {
                            if (sanPham.getIdCategory().equals(idCate)) {
                                keyList.add(snapshot.getKey().toString());
                            }
                        }

                    }
                }
                if (keyList != null) {
                    if (mView != null) {
                        mView.getKeySuccess();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mView != null) {
                    mView.getKeyError();
                }
            }
        });
    }

    @Override
    public void getSanPhamFromFirebase(final DatabaseReference databaseReference, final String idCate, final String idPart) {
        if (!isViewAttached()) return;
        if (keyList.size() >= total + 10) {
            databaseReference.child(keySanPham).orderByKey().startAt(keyList.get(total)).endAt(keyList.get(total + 9)).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    if (sanPham != null) {
                        if (idPart != null && sanPham.getIdPart() != null) {
                            if (sanPham.getIdCategory().equals(idCate)) {
                                if (sanPham.getIdPart().equals(idPart)) {
                                    Log.d("TAG ID3", sanPham.getIdPart());
                                    sanPhamList.add(sanPham);
                                }
                            }
                        } else {
                            if (sanPham.getIdCategory().equals(idCate)) {
                                sanPhamList.add(sanPham);
                            }
                        }
                    }

                    if (sanPhamList != null) {
                        if (mView != null) {
                            mView.getSpSuccess();
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
                        mView.getSpError();
                    }
                }
            });

        } else if (keyList.size() < total + 10) {
            if (!isLoadedWithLittle) {
                databaseReference.child(keySanPham).orderByKey().startAt(keyList.get(total)).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                        if (sanPham != null) {
                            if (idPart != null && sanPham.getIdPart() != null) {
                                if (sanPham.getIdCategory().equals(idCate)) {
                                    if (sanPham.getIdPart().equals(idPart)) {
                                        sanPhamList.add(sanPham);
                                    }
                                }
                            } else {
                                if (sanPham.getIdCategory().equals(idCate)) {
                                    sanPhamList.add(sanPham);
                                }
                            }
                        }
                        if (sanPhamList != null) {
                            if (mView != null) {
                                mView.getSpSuccess();
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
                        if (mView != null) {
                            mView.getSpError();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void getListSearchFromFirebase(DatabaseReference databaseReference, final String filter, final String idCate, final String idPart) {
        if (!isViewAttached()) return;
        searchList.clear();
        databaseReference.child(keySanPham).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                if (sanPham != null) {
                    if (idPart != null && sanPham.getIdPart() != null) {
                        if (sanPham.getIdCategory().equals(idCate)) {
                            if (sanPham.getIdPart().equals(idPart)) {
                                if (sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                                    searchList.add(new SearchSP(sanPham.getIdSanPham(), sanPham.getHeader()));
                                }
                            }
                        }
                    } else {
                        if (sanPham.getIdCategory().equals(idCate)) {
                            if (sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                                searchList.add(new SearchSP(sanPham.getIdSanPham(), sanPham.getHeader()));
                            }
                        }
                    }
                }

                if (searchList != null) {
                    if (mView != null) {
                        mView.getSearchSuccess();
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
                    mView.getSearchError();
                }
            }
        });


    }
}
