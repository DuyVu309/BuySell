package com.example.user.banhangonline.screen.mySanPham;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.AppConstants;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.changeSanpham.ChangeSanPhamActivity;
import com.example.user.banhangonline.screen.cropImage.CropImageActivity;
import com.example.user.banhangonline.screen.mySanPham.adapter.ListImagesCartAdapter;
import com.example.user.banhangonline.screen.mySanPham.adapter.SanPhamMyAccountAdapter;
import com.example.user.banhangonline.screen.showImage.ShowImageActivity;
import com.example.user.banhangonline.utils.DialogUntils;
import com.example.user.banhangonline.utils.FileUtils;
import com.example.user.banhangonline.views.swipe.SwipeBackLayout;
import com.example.user.banhangonline.widget.dialog.DialogChangeAccount;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartSP;
import static com.example.user.banhangonline.utils.KeyUntils.keyShowImage;

public class MySanPhamActivity extends BaseActivity implements
         MySanPhamContact.View,
         SanPhamMyAccountAdapter.IOnClickSanphamAdapter,
         ListImagesCartAdapter.IOnClickImageMyAccount {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_LOAD_AVT = 2;

    @BindView(R.id.recycerview_my_account)
    RecyclerView rvMyAccount;

    @BindView(R.id.img_my_account)
    ImageView imgMyLanscape;

    @BindView(R.id.tv_choose_lanscape)
    TextView tvChooseLans;

    @BindView(R.id.img_avt)
    ImageView imgAvt;

    @BindView(R.id.tv_choose_avt)
    TextView tvChooseAvt;

    @BindView(R.id.tv_account_name)
    TextView tvAccountName;

    @BindView(R.id.tv_account_phone)
    TextView tvAccountPhone;

    @BindView(R.id.tv_account_address)
    TextView tvAccountAdress;

    @BindView(R.id.btn_change_detail)
    Button btnChangeDetail;

    private SanPhamMyAccountAdapter mAdapter;
    private MySanPhamPresenter mPresenter;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);
        mPresenter = new MySanPhamPresenter();
        mPresenter.onCreate();
        mPresenter.attachView(this);
        mPresenter.setContext(this);
        initAdapter();
        if (PreferManager.getUserID(this) != null && PreferManager.getNameAccount(this) != null) {
            mPresenter.getInfomationSuccess(mDataBase);
        }
    }

    private void initAdapter() {
        rvMyAccount.setLayoutManager(new LinearLayoutManager(this));
        rvMyAccount.setHasFixedSize(true);
        mAdapter = new SanPhamMyAccountAdapter(this, mPresenter.getSanPhamList(), this);
        rvMyAccount.setAdapter(mAdapter);
        mPresenter.getListSanphamMyAccount(mDataBase);
    }


    @OnClick(R.id.img_my_account)
    public void showImageLans() {
        Intent intent = new Intent(MySanPhamActivity.this, ShowImageActivity.class);
        intent.putExtra(keyShowImage, mPresenter.getAccount().getUrlLanscape());
        startActivity(intent);
    }

    @OnClick(R.id.img_avt)
    public void showImageAvt() {
        Intent intent = new Intent(MySanPhamActivity.this, ShowImageActivity.class);
        intent.putExtra(keyShowImage, mPresenter.getAccount().getUrlAvt());
        startActivity(intent);
    }

    @OnClick(R.id.img_arrow_back)
    public void onBackActivity() {
        onBackPressed();
    }

    @OnClick(R.id.btn_change_detail)
    public void onChangeDetail() {
        DialogUntils.showChangeInfoDialog(this,
                 tvAccountName.getText().toString().trim(),
                 tvAccountPhone.getText().toString().trim(),
                 tvAccountAdress.getText().toString().trim(), new DialogChangeAccount.IOnClickDoneChange() {
                     @Override
                     public void doneChange(String name, String phone, String address) {
                         showDialog();
                         mPresenter.updateInfomation(mDataBase, new Account(mPresenter.getAccount().getUserId(),
                                  mPresenter.getAccount().getEmailId(),
                                  PreferManager.getIDBuySell(MySanPhamActivity.this),
                                  name,
                                  phone,
                                  mPresenter.getAccount().getUrlAvt(),
                                  mPresenter.getAccount().getNameAvt(),
                                  mPresenter.getAccount().getUrlLanscape(),
                                  mPresenter.getAccount().getNameLans(),
                                  address));
                     }
                 });
    }


    @OnClick(R.id.tv_choose_lanscape)
    public void getImageLanscape() {
        try {
            String s[] = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
            if (checkPermissions(s)) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            } else {
                ActivityCompat.requestPermissions(MySanPhamActivity.this,
                         s,
                         AppConstants.REQUEST_PERMISSION_READ_LIBRARY);
            }

        } catch (Exception exp) {
        }

    }

    @OnClick(R.id.tv_choose_avt)
    public void getImageAvt() {
        try {
            String s[] = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
            if (checkPermissions(s)) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_AVT);
            } else {
                ActivityCompat.requestPermissions(MySanPhamActivity.this,
                         s,
                         AppConstants.REQUEST_PERMISSION_READ_LIBRARY);
            }
        } catch (Exception exp) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstants.REQUEST_PERMISSION_READ_LIBRARY && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_AVT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            tvChooseAvt.setVisibility(View.GONE);
            File file = FileUtils.convertUriToFile(this, pickedImage);
            if (file != null) {
                showCropImageActivity(file.getPath(), imgMyLanscape.getWidth(), imgMyLanscape.getHeight(), false);
            }

        }
        if (requestCode == RESULT_LOAD_AVT && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            tvChooseLans.setVisibility(View.GONE);
            File file = FileUtils.convertUriToFile(this, pickedImage);
            if (file != null) {
                showCropImageActivity(file.getPath(), imgAvt.getWidth(), imgAvt.getHeight(), true);
            }
        }

    }

    private void showCropImageActivity(String sourcePath, int width, int height, boolean isAvt) {
        Intent i = new Intent(this, CropImageActivity.class);
        i.putExtra(CropImageActivity.EXTRA_ACCOUNT, mPresenter.getAccount());
        i.putExtra(CropImageActivity.EXTRA_SOURCE_PATH, sourcePath);
        i.putExtra(CropImageActivity.EXTRA_WIDTH, width);
        i.putExtra(CropImageActivity.EXTRA_HEIGHT, height);
        i.putExtra(CropImageActivity.IS_AVT, isAvt);
        startActivity(i);
        finish();
    }

    @Override
    public void getListSuccess() {
        if (mPresenter.getSanPhamList() != null) {
            if (mPresenter.getSanPhamList().size() != 0) {

            }
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void getListError() {
        showSnackbar(getString(R.string.error));
    }

    @Override
    public void deleteSuscess() {
        mAdapter.notifyDataSetChanged();
        mPresenter.getListSanphamMyAccount(mDataBase);
        Toast.makeText(this, R.string.da_xoa, Toast.LENGTH_SHORT).show();
        dismissDialog();
    }

    @Override
    public void deleteError() {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        dismissDialog();
    }

    @Override
    public void getInfoSuccess(Account account) {
        mPresenter.setAccount(account);
        btnChangeDetail.setEnabled(true);
        if (account.getName() != null) {
            tvAccountName.setText(account.getName());
        }

        if (account.getAddress() != null) {
            tvAccountAdress.setText(account.getAddress());
        } else {
            tvAccountAdress.setText(getString(R.string.dont_address));
            tvAccountAdress.setTextColor(Color.RED);
        }

        if (account.getPhoneNumber() != null) {
            tvAccountPhone.setText(account.getPhoneNumber());
        } else {
            tvAccountPhone.setText(PreferManager.getPhoneNumber(this));
            tvAccountPhone.setTextColor(Color.RED);
        }

        Glide.with(this).load(account.getUrlLanscape()).error(R.drawable.bg_app).into(imgMyLanscape);
        Glide.with(this).load(account.getUrlAvt()).into(imgAvt);
        PreferManager.setMyAddress(this, account.getAddress() != null ? account.getAddress() : null);
    }

    @Override
    public void getInfroError() {

    }

    @Override
    public void updateInfoSuccess() {
        showSnackbar(getString(R.string.cap_nhat_thanh_cong));
        PreferManager.setNameAccount(this, tvAccountName.getText().toString().trim());
        PreferManager.setMyAddress(this, !tvAccountAdress.getText().toString().equals("") ? tvAccountAdress.getText().toString().trim() : null);
        PreferManager.setPhoneNumber(this, tvAccountPhone.getText().toString().trim());
        mAdapter.notifyDataSetChanged();
        dismissDialog();
        visibleTvChooseImage();
    }

    @Override
    public void updateInfoError() {
        showSnackbar(getString(R.string.khong_the_cap_nhat));
        dismissDialog();
    }

    private void visibleTvChooseImage() {
        tvChooseLans.setVisibility(View.VISIBLE);
        tvChooseAvt.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickSanPham(SanPham sanPham) {
        if (sanPham != null) {
            Intent intent = new Intent(MySanPhamActivity.this, ChangeSanPhamActivity.class);
            intent.putExtra(keyStartSP, sanPham);
            startActivity(intent);
        }
    }

    @Override
    public void onChangeSanpham(SanPham sanPham) {
        if (sanPham != null) {
            Intent intent = new Intent(MySanPhamActivity.this, ChangeSanPhamActivity.class);
            intent.putExtra(keyStartSP, sanPham);
            startActivity(intent);
        }
    }

    @Override
    public void onDeleteSanPham(int position, SanPham sanPham) {
        if (sanPham != null) {
            if (sanPham.getIdNguoiban().equals(PreferManager.getUserID(this))) {
                showDialog();
                mPresenter.deleteSanPhamMyAccount(mDataBase, mStorageReferrence, sanPham);
            }
        }
        mPresenter.getSanPhamList().remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mPresenter.getSanPhamList().size());
    }

    @Override
    public void onClickImageMyAccount() {

    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter.detach();
        super.onDestroy();
    }


}
