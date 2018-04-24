package com.example.user.banhangonline.screen.sell;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.user.banhangonline.base.BasePresenter;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Categories;
import com.example.user.banhangonline.model.Part;
import com.example.user.banhangonline.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.user.banhangonline.untils.KeyUntils.keyDoNguNoiY;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateCongNghe;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateDoAn;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateDoChoi;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateGiaDung;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateHocTap;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateKhac;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateMyPham;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCatePhuKien;
import static com.example.user.banhangonline.untils.KeyUntils.keyIdCateThoiTrang;
import static com.example.user.banhangonline.untils.KeyUntils.keySanPham;
import static com.example.user.banhangonline.untils.KeyUntils.keybeNam;
import static com.example.user.banhangonline.untils.KeyUntils.keybeNu;
import static com.example.user.banhangonline.untils.KeyUntils.keygiayNam;
import static com.example.user.banhangonline.untils.KeyUntils.keygiayNu;
import static com.example.user.banhangonline.untils.KeyUntils.keyphuKienNam;
import static com.example.user.banhangonline.untils.KeyUntils.keyphuKienNu;
import static com.example.user.banhangonline.untils.KeyUntils.keythoiTrangNam;
import static com.example.user.banhangonline.untils.KeyUntils.keythoiTrangNu;
import static com.example.user.banhangonline.untils.KeyUntils.keytuiSachNam;
import static com.example.user.banhangonline.untils.KeyUntils.keytuiSachNu;
import static com.example.user.banhangonline.untils.KeyUntils.titleDienTu;
import static com.example.user.banhangonline.untils.KeyUntils.titleDoAn;
import static com.example.user.banhangonline.untils.KeyUntils.titleDochoi;
import static com.example.user.banhangonline.untils.KeyUntils.titleGiaDung;
import static com.example.user.banhangonline.untils.KeyUntils.titleGiaHocTap;
import static com.example.user.banhangonline.untils.KeyUntils.titleKhac;
import static com.example.user.banhangonline.untils.KeyUntils.titleMyPham;
import static com.example.user.banhangonline.untils.KeyUntils.titlePhuKien;
import static com.example.user.banhangonline.untils.KeyUntils.titleThoiTrang;
import static com.example.user.banhangonline.untils.TextUntils.doNguNoiY;
import static com.example.user.banhangonline.untils.TextUntils.giayNam;
import static com.example.user.banhangonline.untils.TextUntils.giayNu;
import static com.example.user.banhangonline.untils.TextUntils.phuKienNam;
import static com.example.user.banhangonline.untils.TextUntils.phuKienNu;
import static com.example.user.banhangonline.untils.TextUntils.thoiTrangBeGai;
import static com.example.user.banhangonline.untils.TextUntils.thoiTrangBeNam;
import static com.example.user.banhangonline.untils.TextUntils.thoiTrangNam;
import static com.example.user.banhangonline.untils.TextUntils.thoiTrangNu;
import static com.example.user.banhangonline.untils.TextUntils.tuiSachNam;
import static com.example.user.banhangonline.untils.TextUntils.tuiSachNu;

public class SellPresenter extends BasePresenter implements SellContact.Presenterr {

    private SellContact.View mView;
    private List<Object> listCategories;
    private List<Object> listPart;
    private List<File> listFiles;
    private List<String> listImages;
    private List<String> listNameImages;
    private String idCate, titleCate, idPart, titlePart;

    public List<Object> getListCategory() {
        listCategories.add(new Categories(keyIdCateDoAn, titleDoAn));
        listCategories.add(new Categories(keyIdCateMyPham, titleMyPham));
        listCategories.add(new Categories(keyIdCateThoiTrang, titleThoiTrang));
        listCategories.add(new Categories(keyIdCateCongNghe, titleDienTu));
        listCategories.add(new Categories(keyIdCatePhuKien, titlePhuKien));
        listCategories.add(new Categories(keyIdCateGiaDung, titleGiaDung));
        listCategories.add(new Categories(keyIdCateHocTap, titleGiaHocTap));
        listCategories.add(new Categories(keyIdCateDoChoi, titleDochoi));
        listCategories.add(new Categories(keyIdCateKhac, titleKhac));
        return listCategories;
    }

    public List<Object> getListPart() {

        if (listPart != null) {
            listPart.clear();
        }
        listPart.add(new Part(keyIdCateThoiTrang, keythoiTrangNam, 0, thoiTrangNam));
        listPart.add(new Part(keyIdCateThoiTrang, keygiayNam, 0, giayNam));
        listPart.add(new Part(keyIdCateThoiTrang, keytuiSachNam, 0, tuiSachNam));
        listPart.add(new Part(keyIdCateThoiTrang, keyphuKienNam, 0, phuKienNam));
        listPart.add(new Part(keyIdCateThoiTrang, keybeNam, 0, thoiTrangBeNam));

        listPart.add(new Part(keyIdCateThoiTrang, keythoiTrangNu, 0, thoiTrangNu));
        listPart.add(new Part(keyIdCateThoiTrang, keyDoNguNoiY, 0, doNguNoiY));
        listPart.add(new Part(keyIdCateThoiTrang, keygiayNu, 0, giayNu));
        listPart.add(new Part(keyIdCateThoiTrang, keytuiSachNu, 0, tuiSachNu));
        listPart.add(new Part(keyIdCateThoiTrang, keyphuKienNu, 0, phuKienNu));
        listPart.add(new Part(keyIdCateThoiTrang, keybeNu, 0, thoiTrangBeGai));
        return listPart != null ? listPart : null;
    }

    public List<File> getListFiles() {
        return listFiles;
    }

    public void setListFiles(List<File> listFiles) {
        this.listFiles = listFiles;
    }

    public List<String> getListImages() {
        return listImages;
    }

    public List<String> getListNameImages() {
        return listNameImages;
    }

    public String getIdCate() {
        return idCate;
    }

    public void setIdCate(String idCate) {
        this.idCate = idCate;
    }

    public String getTitleCate() {
        return titleCate;
    }

    public void setTitleCate(String titleCate) {
        this.titleCate = titleCate;
    }

    public String getIdPart() {
        return idPart;
    }

    public void setIdPart(String idPart) {
        this.idPart = idPart;
    }

    public String getTitlePart() {
        return titlePart;
    }

    public void setTitlePart(String titlePart) {
        this.titlePart = titlePart;
    }

    @Override
    public void onCreate() {
        listCategories = new ArrayList<>();
        listPart = new ArrayList<>();
        listFiles = new ArrayList<>();
        listImages = new ArrayList<>();
        listNameImages = new ArrayList<>();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(SellContact.View View) {
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
    public void upLoadSanPhamToFirebase(DatabaseReference database, SanPham sanPham) {
        if (!isViewAttached()) {
            return;
        }
        database.child(keySanPham).push().setValue(sanPham).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mView.upLoadToFirebaseSuccess();
                } else {
                    mView.upLoadToFirebaseError();

                }
            }
        });
    }

    @Override
    public void upLoadFileImageToStorage(StorageReference storageReference, String nameImage) {
        if (!isViewAttached()) {
            return;
        }
        for (int i = 0; i < listFiles.size(); i++) {
            Uri file = Uri.fromFile(new File(listFiles.get(i).toString()));
            String date = String.valueOf(Calendar.getInstance().getTimeInMillis());
            listNameImages.add(nameImage + date);
            StorageReference riversRef = storageReference.child("images/" + listNameImages.get(i));
            UploadTask uploadTask = riversRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    mView.upLoadImagErrror();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                    listImages.add(downloadUrl);
                    if (listImages.size() == listFiles.size()) {
                        mView.upLoadImagesSuccess(listNameImages);
                    }
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    int x = (int) Math.round(progress);
                    mView.displayPercent(x + "%...");
                }
            });
        }
    }
}
