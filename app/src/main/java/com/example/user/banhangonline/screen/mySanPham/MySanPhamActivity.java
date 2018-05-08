package com.example.user.banhangonline.screen.mySanPham;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.changeSanpham.ChangeSanPhamActivity;
import com.example.user.banhangonline.screen.home.HomeActivity;
import com.example.user.banhangonline.screen.mySanPham.adapter.ListImagesCartAdapter;
import com.example.user.banhangonline.screen.mySanPham.adapter.SanPhamMyAccountAdapter;
import com.example.user.banhangonline.screen.register.RegisterActivity;
import com.example.user.banhangonline.untils.DialogUntils;
import com.example.user.banhangonline.untils.NetworkUtils;
import com.example.user.banhangonline.widget.dialog.DialogChangeAccount;

import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartSP;

public class MySanPhamActivity extends BaseActivity implements
         MySanPhamContact.View,
         SanPhamMyAccountAdapter.IOnClickSanphamAdapter,
         ListImagesCartAdapter.IOnClickImageMyAccount {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_LOAD_AVT = 2;

    @BindView(R.id.btn_save)
    Button btnSave;

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

    @BindView(R.id.pb_load_lanscape)
    ProgressBar pbLoadLanscape;

    @BindView(R.id.pb_load_avt)
    ProgressBar pbLoadAvt;

    @BindView(R.id.btn_change_detail)
    Button btnChangeDetail;

    private SanPhamMyAccountAdapter mAdapter;
    private Unbinder unbinder;
    private MySanPhamPresenter mPresenter;
    String urlAvt, urlLanscape;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        unbinder = ButterKnife.bind(this);
        mPresenter = new MySanPhamPresenter();
        mPresenter.onCreate();
        mPresenter.attachView(this);
        mPresenter.setContext(this);
        initAdapter();
        if (PreferManager.getEmailID(this) != null && PreferManager.getNameAccount(this) != null) {
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

    @OnClick(R.id.img_arrow_back)
    public void onBackActivity() {
        startActivity(new Intent(MySanPhamActivity.this, HomeActivity.class));
        finish();
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
                         mPresenter.updateInfomation(mDataBase, new Account(mPresenter.getAccount().getEmailId(),
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
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        } catch (Exception exp) {
        }

    }

    @OnClick(R.id.tv_choose_avt)
    public void getImageAvt() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_AVT);
        } catch (Exception exp) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            urlLanscape = pickedImage.toString();
            Glide.with(this).load(pickedImage.toString()).into(imgMyLanscape);
            btnSave.setVisibility(View.VISIBLE);
            tvChooseAvt.setVisibility(View.GONE);

        }
        if (requestCode == RESULT_LOAD_AVT && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            urlAvt = pickedImage.toString();
            Glide.with(this).load(pickedImage.toString()).into(imgAvt);
            btnSave.setVisibility(View.VISIBLE);
            tvChooseLans.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.btn_save)
    public void saveImageAccount() {
        if (NetworkUtils.isConnected(this)) {
            showDialog();
            if (urlLanscape != null) {
                mPresenter.upLoadImageLanscapeToStorage(mStorageReferrence, mPresenter.getAccount(), urlLanscape);
                urlLanscape = null;
                return;
            }
            if (urlAvt != null) {
                mPresenter.upLoadImageAvtToStorage(mStorageReferrence, mPresenter.getAccount(), urlAvt);
                urlAvt = null;
                return;
            }
        } else {
            showNoInternet();
        }

    }

    @Override
    public void getListSuccess() {
        if (mPresenter.getSanPhamList() != null) {
            if (mPresenter.getSanPhamList().size() == 0) {
                showSnackbar(getString(R.string.dont_have_product));
            } else {
                Collections.sort(mPresenter.getSanPhamList(), new Comparator<SanPham>() {
                    @Override
                    public int compare(SanPham sanPham, SanPham t1) {
                        return sanPham.getTime().compareTo(t1.getTime());
                    }
                });
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
        Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void getInfroError() {

    }

    @Override
    public void updateInfoSuccess() {
        showSnackbar(getString(R.string.cap_nhat_thanh_cong));
        PreferManager.setNameAccount(this, tvAccountName.getText().toString().trim());
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
    public void uploadImageLansSuccess(String urlLans) {
        mPresenter.updateInfomation(mDataBase, new Account(mPresenter.getAccount().getEmailId(),
                 mPresenter.getAccount().getIdBS(),
                 mPresenter.getAccount().getName(),
                 mPresenter.getAccount().getPhoneNumber(),
                 mPresenter.getAccount().getUrlAvt(),
                 mPresenter.getAccount().getNameAvt(),
                 urlLans,
                 mPresenter.getNameLans(),
                 mPresenter.getAccount().getAddress()));
        btnSave.setVisibility(View.GONE);
        dismissDialog();
    }

    @Override
    public void uploadImageAvtSuccess(String urlAvt) {
        mPresenter.updateInfomation(mDataBase, new Account(mPresenter.getAccount().getEmailId(),
                 mPresenter.getAccount().getIdBS(),
                 mPresenter.getAccount().getName(),
                 mPresenter.getAccount().getPhoneNumber(),
                 urlAvt,
                 mPresenter.getNameAvt(),
                 mPresenter.getAccount().getUrlLanscape(),
                 mPresenter.getAccount().getNameLans(),
                 mPresenter.getAccount().getAddress()));
        btnSave.setVisibility(View.GONE);
        dismissDialog();
    }

    @Override
    public void uploadImageError() {
        showSnackbar("Đã có lỗi xảy ra, thử lại!");
        dismissDialog();
        btnSave.setVisibility(View.GONE);
    }

    @Override
    public void displayPercent(String percent) {
        Log.d("TAG X", percent);
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
            finish();
        }
    }

    @Override
    public void onDeleteSanPham(int position, SanPham sanPham) {
        if (sanPham != null) {
            if (sanPham.getIdNguoiban().equals(PreferManager.getEmailID(this))) {
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
        unbinder.unbind();
        super.onDestroy();
    }


}
