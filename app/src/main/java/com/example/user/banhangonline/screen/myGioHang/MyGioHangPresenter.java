package com.example.user.banhangonline.screen.myGioHang;

import android.content.Context;

import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.DonHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.utils.KeyUntils.keyDonHang;

public class MyGioHangPresenter implements MyGioHangContact.Presenter {
    private MyGioHangContact.View mView;
    private Context context;
    private List<DonHang> mList;
    private List<String> mListKey;
    boolean isLoadedWithLittle = false;
    private int total = 0;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<String> getmListKey() {
        return mListKey;
    }

    public List<DonHang> getmList() {
        return mList;
    }

    public void setmList(List<DonHang> mList) {
        this.mList = mList;
    }

    @Override
    public void attachView(MyGioHangContact.View View) {
        mView = View;
        mList = new ArrayList<>();
        mListKey = new ArrayList<>();
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
    public void getAllKeyMyCartFromFirebase(DatabaseReference databaseReference, String idNguoiBan) {
        if (!isViewAttached()) return;
        if (context == null) return;
        mListKey.clear();
        databaseReference.child(keyDonHang).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonHang donHang = snapshot.getValue(DonHang.class);
                    if (donHang != null) {
                        if (donHang.getIdNguoiBan().equals(PreferManager.getUserID(context))) {
                            mListKey.add(snapshot.getKey().toString());

                        }
                    }
                }

                if (mListKey != null) {
                    if (mView != null) {
                        mView.getAllKeySuccess();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mView != null) {
                    mView.getAllKeyError();
                }
            }
        });
    }

    @Override
    public void getListCartFromFirebase(DatabaseReference databaseReference) {
        if (!isViewAttached()) return;
        if (context == null) return;

        if (mListKey.size() >= total + 10) {
            databaseReference.child(keyDonHang).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (donHang.getIdNguoiBan() != null && PreferManager.getUserID(context) != null) {
                        if (donHang.getIdNguoiBan().equals(PreferManager.getUserID(context))) {
                            mList.add(donHang);
                        }
                    }

                    if (mList != null) {
                        if (mView != null) {
                            mView.getCartSuccess();
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
                        mView.getCartError();
                    }
                }
            });
        } else if (mListKey.size() < total + 10) {
            if (!isLoadedWithLittle) {
                databaseReference.child(keyDonHang).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        DonHang donHang = dataSnapshot.getValue(DonHang.class);
                        if (donHang.getIdNguoiBan() != null && PreferManager.getUserID(context) != null) {
                            if (donHang.getIdNguoiBan().equals(PreferManager.getUserID(context))) {
                                mList.add(donHang);
                                isLoadedWithLittle = true;
                            }
                        }
                        if (mView != null) {
                            mView.getCartSuccess();
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
                            mView.getCartError();
                        }
                    }
                });
            }
        }
    }


}
