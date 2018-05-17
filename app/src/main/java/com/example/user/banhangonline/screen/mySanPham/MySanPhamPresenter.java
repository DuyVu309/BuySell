package com.example.user.banhangonline.screen.mySanPham;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.user.banhangonline.base.BasePresenter;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.banhangonline.utils.KeyUntils.keyAccount;
import static com.example.user.banhangonline.utils.KeyUntils.keyIdSanPham;
import static com.example.user.banhangonline.utils.KeyUntils.keySanPham;

public class MySanPhamPresenter extends BasePresenter implements MySanPhamContact.Presenter {
    private Context context;
    private List<SanPham> sanPhamList = new ArrayList<>();
    private MySanPhamContact.View mView;
    private Account account = new Account();
    private String nameAvt = "";
    private String nameLans = "";

    public String getNameAvt() {
        return nameAvt;
    }

    public String getNameLans() {
        return nameLans;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<SanPham> getSanPhamList() {
        return sanPhamList;
    }

    @Override
    public void attachView(MySanPhamContact.View View) {
        mView = View;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
        databaseReference.child(keySanPham).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                if (PreferManager.getUserID(context).equals(sanPham.getIdNguoiban())) {
                    sanPhamList.add(sanPham);
                }
                if (sanPhamList != null) {
                    if (mView != null) {
                        mView.getListSuccess();
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
                    mView.getListError();
                }
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
        databaseReference.child(keySanPham).orderByChild(keyIdSanPham).equalTo(sanPham.getIdSanPham()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
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

    @Override
    public void getInfomationSuccess(DatabaseReference databaseReference) {
        if (!isViewAttached()) return;
        if (context == null) return;

        databaseReference.child(keyAccount).child(PreferManager.getUserID(context)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);
                if (account != null) {
                    if (mView != null) {
                        mView.getInfoSuccess(account);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mView.getInfroError();
            }
        });
    }

    @Override
    public void updateInfomation(DatabaseReference databaseReference, Account account) {
        if (!isViewAttached()) return;
        if (context == null) return;
        databaseReference.child(keyAccount).child(PreferManager.getUserID(context)).setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (mView != null) {
                        mView.updateInfoSuccess();
                    }
                } else {
                    if (mView != null) {
                        mView.updateInfoError();
                    }
                }
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
