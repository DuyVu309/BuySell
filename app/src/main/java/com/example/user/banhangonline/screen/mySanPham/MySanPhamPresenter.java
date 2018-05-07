package com.example.user.banhangonline.screen.mySanPham;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.user.banhangonline.base.BasePresenter;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static com.example.user.banhangonline.untils.KeyUntils.keyAccount;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdSanPham;
import static com.example.user.banhangonline.untils.KeyUntils.keySanPham;

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
        databaseReference.child(keySanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham sanPham = snapshot.getValue(SanPham.class);
                    if (PreferManager.getEmailID(context).equals(sanPham.getIdNguoiban())) {
                        sanPhamList.add(sanPham);
                    }
                }
                if (mView != null) {
                    mView.getListSuccess();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mView.getListError();
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
        databaseReference.child(keySanPham).orderByChild(keyIdSanPham).equalTo(sanPham.getIdSanPham()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
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

        databaseReference.child(keyAccount).child(PreferManager.getEmailID(context)).addValueEventListener(new ValueEventListener() {
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
        databaseReference.child(keyAccount).child(PreferManager.getEmailID(context)).setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public void upLoadImageLanscapeToStorage(StorageReference storageReference, Account account, String imgLanscape) {
        storageReference.child("imagesAccount/" + account.getNameLans()).delete();
        Uri file = Uri.parse(imgLanscape);
        String date = String.valueOf(Calendar.getInstance().getTimeInMillis()) + UUID.randomUUID();
        StorageReference riversRef = storageReference.child("imagesAccount/" + PreferManager.getEmailID(context) + date);
        nameLans = PreferManager.getEmailID(context) + date;
        UploadTask uploadTask = riversRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (mView != null) {
                    mView.uploadImageError();
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (mView != null) {
                    mView.uploadImageLansSuccess(taskSnapshot.getDownloadUrl().toString());
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int x = (int) Math.round(progress);
                if (mView != null) {
                    mView.displayPercent(x + "%...");
                }
            }
        });
    }

    @Override
    public void upLoadImageAvtToStorage(StorageReference storageReference, Account account, String imgAvt) {
        storageReference.child("imagesAccount/" + account.getNameAvt()).delete();
        Uri file = Uri.parse(imgAvt);
        String date = String.valueOf(Calendar.getInstance().getTimeInMillis()) + UUID.randomUUID();
        StorageReference riversRef = storageReference.child("imagesAccount/" + PreferManager.getEmailID(context) + date);
        nameAvt = PreferManager.getEmailID(context) + date;
        UploadTask uploadTask = riversRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (mView != null) {
                    mView.uploadImageError();
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (mView != null) {
                    mView.uploadImageAvtSuccess(taskSnapshot.getDownloadUrl().toString());
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                int x = (int) Math.round(progress);
                if (mView != null) {
                    mView.displayPercent(x + "%...");
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
