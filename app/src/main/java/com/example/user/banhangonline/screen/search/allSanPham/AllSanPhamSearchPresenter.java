package com.example.user.banhangonline.screen.search.allSanPham;

import android.util.Log;

import com.example.user.banhangonline.model.SanPham;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.untils.KeyUntils.keySanPham;

public class AllSanPhamSearchPresenter implements AllSanPhamSearchedContact.Presenter {

    AllSanPhamSearchedContact.View mView;
    private List<SanPham> sanPhamList;
    private List<String> keyList;
    boolean isLoadedWithLittle = false;
    private int total = 0;

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
    public void attachView(AllSanPhamSearchedContact.View View) {
        keyList = new ArrayList<>();
        sanPhamList = new ArrayList<>();
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
    public void getAllKeySanPham(DatabaseReference databaseReference, final String filter, final String idCate, final String idPart) {
        if (!isViewAttached()) return;

        keyList.clear();
        databaseReference.child(keySanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham sanPham = snapshot.getValue(SanPham.class);

                    if (idCate == null && idPart == null) { //all sanpham
                        if (sanPham.getHeader() != null && sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                            keyList.add(snapshot.getKey().toString());
                        } else if (sanPham.getNameNguoiBan() != null && sanPham.getNameNguoiBan().toLowerCase().contains(filter.toLowerCase())) {
                            keyList.add(snapshot.getKey().toString());
                        }
                    } else if (idCate != null && idPart == null) { // sanpham voi idCate
                        Log.d("TAG ERR", idCate);
                        if (sanPham.getIdCategory() != null && sanPham.getIdCategory().equals(idCate)) {
                            if (sanPham.getHeader() != null && sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                                keyList.add(snapshot.getKey().toString());
                            } else if (sanPham.getNameNguoiBan() != null && sanPham.getNameNguoiBan().toLowerCase().contains(filter.toLowerCase())) {
                                keyList.add(snapshot.getKey().toString());
                            }
                        }
                    } else if (idCate != null && idPart != null) {// sanpham voi idPart
                        if (sanPham.getIdCategory() != null && sanPham.getIdPart() != null) {
                            if (sanPham.getIdCategory().equals(idCate) && sanPham.getIdPart().equals(idPart)) {
                                if (sanPham.getHeader() != null && sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                                    keyList.add(snapshot.getKey().toString());
                                } else if (sanPham.getNameNguoiBan() != null && sanPham.getNameNguoiBan().toLowerCase().contains(filter.toLowerCase())) {
                                    keyList.add(snapshot.getKey().toString());
                                }
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
    public void getAllSpWithFilterFromFirebase(DatabaseReference databaseReference, final String filter, final String idCate, final String idPart) {
        if (!isViewAttached()) return;
        if (filter == null) return;

        if (keyList.size() >= total + 10) {
            databaseReference.child(keySanPham).orderByKey().startAt(keyList.get(total)).endAt(keyList.get(total + 9)).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);

                    if (idCate == null && idPart == null) { //all
                        if (sanPham.getHeader() != null && sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                            sanPhamList.add(sanPham);
                        } else if (sanPham.getNameNguoiBan() != null && sanPham.getNameNguoiBan().toLowerCase().contains(filter.toLowerCase())) {
                            sanPhamList.add(sanPham);
                        }
                    } else if (idCate != null && idPart == null) {
                        if (sanPham.getIdCategory().equals(idCate)) { //idCate
                            if (sanPham.getHeader() != null && sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                                sanPhamList.add(sanPham);
                            } else if (sanPham.getNameNguoiBan() != null && sanPham.getNameNguoiBan().toLowerCase().contains(filter.toLowerCase())) {
                                sanPhamList.add(sanPham);
                            }
                        }
                    } else if (idCate != null && idPart != null) { //idPart
                        if (sanPham.getIdPart() != null) {
                            if (sanPham.getIdCategory().equals(idCate) && sanPham.getIdPart().equals(idPart)) {
                                if (sanPham.getHeader() != null && sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                                    sanPhamList.add(sanPham);
                                } else if (sanPham.getNameNguoiBan() != null && sanPham.getNameNguoiBan().toLowerCase().contains(filter.toLowerCase())) {
                                    sanPhamList.add(sanPham);
                                }
                            }
                        }
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
                        if (idCate == null && idPart == null) { //all
                            if (sanPham.getHeader() != null && sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                                sanPhamList.add(sanPham);
                            } else if (sanPham.getNameNguoiBan() != null && sanPham.getNameNguoiBan().toLowerCase().contains(filter.toLowerCase())) {
                                sanPhamList.add(sanPham);
                            }
                            isLoadedWithLittle = true;
                        } else if (idCate != null && idPart == null) {
                            if (sanPham.getIdCategory().equals(idCate)) { //idCate
                                if (sanPham.getHeader() != null && sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                                    sanPhamList.add(sanPham);
                                } else if (sanPham.getNameNguoiBan() != null && sanPham.getNameNguoiBan().toLowerCase().contains(filter.toLowerCase())) {
                                    sanPhamList.add(sanPham);
                                }
                                isLoadedWithLittle = true;
                            }
                        } else if (idCate != null && idPart != null) { //idPart
                            if (sanPham.getIdCategory() != null && sanPham.getIdPart() != null) {
                                if (sanPham.getIdCategory().equals(idCate) && sanPham.getIdPart().equals(idPart)) {
                                    if (sanPham.getHeader() != null && sanPham.getHeader().toLowerCase().contains(filter.toLowerCase())) {
                                        sanPhamList.add(sanPham);
                                    } else if (sanPham.getNameNguoiBan() != null && sanPham.getNameNguoiBan().toLowerCase().contains(filter.toLowerCase())) {
                                        sanPhamList.add(sanPham);
                                    }
                                    isLoadedWithLittle = true;
                                }
                            }
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
                        if (mView != null) {
                            mView.getSpError();
                        }
                    }
                });

            }
        }
    }
}
