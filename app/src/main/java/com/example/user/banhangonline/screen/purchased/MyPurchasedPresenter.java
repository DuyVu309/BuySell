package com.example.user.banhangonline.screen.purchased;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.DonHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.utils.KeyUntils.keyDonHang;

public class MyPurchasedPresenter implements MyPurchasedContact.Presenter {

    MyPurchasedContact.View mView;
    private Context context;
    private List<DonHang> donHangList;

    public void setContext(Context context) {
        this.context = context;
    }

    public List<DonHang> getDonHangList() {
        return donHangList;
    }

    public void setDonHangList(List<DonHang> donHangList) {
        this.donHangList = donHangList;
    }

    @Override
    public void attachView(MyPurchasedContact.View View) {
        donHangList = new ArrayList<>();
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
    public void getListPurchsedFromFirebase(DatabaseReference databaseReference) {
        if (!isViewAttached()) return;
        if (context == null) return;
        donHangList.clear();
        databaseReference.child(keyDonHang).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DonHang donHang = dataSnapshot.getValue(DonHang.class);
                if (donHang != null) {
                    if (donHang.getIdNguoiBan() != null && PreferManager.getUserID(context) != null) {
                        if (donHang.getIdNguoiMua().equals(PreferManager.getUserID(context))) {
                            donHangList.add(donHang);
                        }
                    }
                }


                if (donHangList != null) {
                    if (mView != null) {
                        mView.getPurchsedSuccess();
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
                    mView.getPurchsedError();
                }
            }
        });
    }

    @Override
    public void deletePurchsed(final DatabaseReference databaseReference, final DonHang donHang) {
        if (!isViewAttached()) return;
        if (!isViewAttached()) return;
        if (donHang == null) return;
        databaseReference.child(keyDonHang).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonHang donHang1 = snapshot.getValue(DonHang.class);
                    if (PreferManager.getUserID(context).equals(donHang.getIdNguoiMua())) {
                        if (donHang1.getIdDonHang().equals(donHang.getIdDonHang())) {
                            databaseReference.child(keyDonHang).child(snapshot.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (mView != null) {
                                            mView.deletePurchsedSuccess();
                                            return;
                                        }
                                    } else {
                                        if (mView != null) {
                                            mView.deletePurchsedError();
                                        }
                                    }
                                }
                            });
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
