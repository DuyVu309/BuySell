package com.example.user.banhangonline.screen.cropImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.user.banhangonline.base.BasePresenter;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.UUID;

import static com.example.user.banhangonline.utils.KeyUntils.keyAccount;

public class CropImagePresenter extends BasePresenter implements CropImageContact.Presenter {

    private Context context;
    private CropImageContact.View mView;
    private Account account;
    private String urlLink;
    private String nameImage;

    public String getUrlLink() {
        return urlLink;
    }

    public String getNameImage() {
        return nameImage;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(CropImageContact.View View) {
        mView = View;
    }

    @Override
    public void detach() {
        mView = null;
    }


    @Override
    public void uploadImageAvtToFirebase(StorageReference storageReference, Account account, Bitmap bitmapAvt) {
        if (!isViewAttached()) return;
        //deleteOldAvt
        storageReference.child("imagesAccount/" + account.getNameAvt()).delete();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapAvt.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        String date = String.valueOf(Calendar.getInstance().getTimeInMillis()) + UUID.randomUUID();
        StorageReference riversRef = storageReference.child("imagesAccount/" + PreferManager.getUserID(context) + date);
        nameImage = PreferManager.getUserID(context) + date;
        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (mView != null) {
                    mView.uploadImageAvtError();
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (mView != null) {
                    urlLink = taskSnapshot.getDownloadUrl().toString();
                    mView.uploadImageAvtSuccess();
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                if (mView != null) {
                    mView.upLoading();
                }
            }
        });

    }

    @Override
    public void uploadImageLanscapeToFirebase(StorageReference storageReference, Account account, Bitmap bitmapLans) {
        if (!isViewAttached()) return;
        //deleteOldLans
        storageReference.child("imagesAccount/" + account.getNameLans()).delete();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapLans.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        String date = String.valueOf(Calendar.getInstance().getTimeInMillis()) + UUID.randomUUID();
        StorageReference riversRef = storageReference.child("imagesAccount/" + PreferManager.getUserID(context) + date);
        nameImage = PreferManager.getUserID(context) + date;

        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (mView != null) {
                    mView.uploadImageLanscapeError();
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (mView != null) {
                    urlLink = taskSnapshot.getDownloadUrl().toString();
                    mView.uploadImageLansCapeSuccess();
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                if (mView != null) {
                    mView.upLoading();
                }
            }
        });

    }

    @Override
    public void updateInfomationToFirebase(DatabaseReference databaseReference, Account account) {
        if (!isViewAttached()) return;
        if (context == null) return;
        databaseReference.child(keyAccount).child(PreferManager.getUserID(context)).setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (mView != null) {
                        mView.updateInfomationSuccess();
                    }
                } else {
                    if (mView != null) {
                        mView.updateInfomationSuccess();
                    }
                }
            }
        });
    }
}
