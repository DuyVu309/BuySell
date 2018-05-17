package com.example.user.banhangonline.screen.cropImage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.screen.mySanPham.MySanPhamActivity;
import com.example.user.banhangonline.utils.BitmapUtils;
import com.example.user.banhangonline.utils.FileUtils;
import com.example.user.banhangonline.utils.NetworkUtils;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CropImageActivity extends BaseActivity implements CropImageContact.View {

    public static final String EXTRA_ACCOUNT = "account";
    public static final String EXTRA_SOURCE_PATH = "avatar_path";
    public static final String EXTRA_WIDTH = "result_width";
    public static final String EXTRA_HEIGHT = "result_height";
    public static final String IS_AVT = "avatar";

    @BindView(R.id.iv_crop)
    CropImageView ivCrop;

    private String mPath;
    private Bitmap mBitmap;
    private int resultWidth;
    private int resultHeight;
    private boolean isAvt = false;

    CropImagePresenter mPresenter;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        mPresenter = new CropImagePresenter();
        mPresenter.attachView(this);
        mPresenter.setContext(this);

        if (bundle != null) {
            mPresenter.setAccount((Account) getIntent().getSerializableExtra(EXTRA_ACCOUNT));
            mPath = bundle.getString(EXTRA_SOURCE_PATH);
            resultWidth = bundle.getInt(EXTRA_WIDTH);
            resultHeight = bundle.getInt(EXTRA_HEIGHT);
            isAvt = bundle.getBoolean(IS_AVT);
        }
        if (mPath != null) {
            int maxSize = Math.max(resultWidth, resultHeight);
            mBitmap = BitmapUtils.decodeBitmapFromFile(new File(mPath), maxSize, maxSize);
            ivCrop.setImageBitmap(mBitmap);
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
        ivCrop.setFixedAspectRatio(true);
        ivCrop.setAspectRatio(resultWidth, resultHeight);

    }

    @OnClick(R.id.btn_save)
    public void SaveImage() {
        if (NetworkUtils.isConnected(this)) {
            mBitmap = ivCrop.getCroppedImage();
            if (mBitmap != null) {
                mBitmap = FileUtils.getResizedBitmap(mBitmap, resultHeight, resultWidth);
                if (isAvt) {
                    mPresenter.uploadImageAvtToFirebase(mStorageReferrence, mPresenter.getAccount(), mBitmap);
                } else {
                    mPresenter.uploadImageLanscapeToFirebase(mStorageReferrence, mPresenter.getAccount(), mBitmap);
                }
            } else {
                setResult(RESULT_CANCELED);
            }

        } else {
            showNoInternet();
        }
    }

    @OnClick(R.id.img_arrow_back)
    public void backActivity(){
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        super.onDestroy();
    }

    @Override
    public void uploadImageAvtSuccess() {
        if (NetworkUtils.isConnected(this)) {
            mPresenter.updateInfomationToFirebase(mDataBase, new Account(mPresenter.getAccount().getUserId(),
                     mPresenter.getAccount().getEmailId(),
                     PreferManager.getIDBuySell(CropImageActivity.this),
                     mPresenter.getAccount().getName(),
                     mPresenter.getAccount().getPhoneNumber(),
                     mPresenter.getUrlLink(),
                     mPresenter.getNameImage(),
                     mPresenter.getAccount().getUrlLanscape(),
                     mPresenter.getAccount().getNameLans(),
                     mPresenter.getAccount().getAddress()));
        } else {
            showNoInternet();
        }
    }

    @Override
    public void uploadImageAvtError() {
        showSnackbar(getString(R.string.error_retry));
        dismissDialog();
    }

    @Override
    public void uploadImageLansCapeSuccess() {
        if (NetworkUtils.isConnected(this)) {
            mPresenter.updateInfomationToFirebase(mDataBase, new Account(mPresenter.getAccount().getUserId(),
                     mPresenter.getAccount().getEmailId(),
                     PreferManager.getIDBuySell(CropImageActivity.this),
                     mPresenter.getAccount().getName(),
                     mPresenter.getAccount().getPhoneNumber(),
                     mPresenter.getAccount().getUrlAvt(),
                     mPresenter.getAccount().getNameAvt(),
                     mPresenter.getUrlLink(),
                     mPresenter.getNameImage(),
                     mPresenter.getAccount().getAddress()));
        } else {
            showNoInternet();
        }

    }

    @Override
    public void uploadImageLanscapeError() {
        showSnackbar(getString(R.string.error_retry));
        dismissDialog();
    }

    @Override
    public void upLoading() {
        showDialog();
    }

    @Override
    public void updateInfomationSuccess() {
        dismissDialog();
        startActivity(new Intent(CropImageActivity.this, MySanPhamActivity.class));
        finish();
    }

    @Override
    public void updateInfomationError() {
        showSnackbar(getString(R.string.error_retry));
        dismissDialog();
        startActivity(new Intent(CropImageActivity.this, MySanPhamActivity.class));
        finish();
    }

}
