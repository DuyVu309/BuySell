package com.example.user.banhangonline.screen.changeSanpham;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.mySanPham.MySanPhamActivity;
import com.example.user.banhangonline.screen.sell.adapter.ImageAdapter;
import com.example.user.banhangonline.utils.GoogleMapUtils;
import com.example.user.banhangonline.utils.NetworkUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartSP;

public class ChangeSanPhamActivity extends BaseActivity implements ChangeSanPhamContact.View {


    @BindView(R.id.img_account)
    ImageView imgAccount;

    @BindView(R.id.tv_account_name)
    TextView tvAccountName;

    @BindView(R.id.edt_header)
    EditText edtHeader;

    @BindView(R.id.edt_mota)
    EditText edtMota;

    @BindView(R.id.edt_gia)
    EditText edtGia;

    @BindView(R.id.rv_sanpham_myaccount)
    RecyclerView rvChangeSp;


    private ImageAdapter mAdapter;
    private ChangePresenter mPresenter;
    private SanPham sanPham;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_san_pham);
        ButterKnife.bind(this);
        mPresenter = new ChangePresenter();
        mPresenter.onCreate();
        mPresenter.attachView(this);
        sanPham = (SanPham) getIntent().getSerializableExtra(keyStartSP);
        if (sanPham != null) {
            initAdapter();
        }
    }

    private void initAdapter() {
        if (sanPham.getListFiles().getListUrl().size() == 1) {
            rvChangeSp.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            rvChangeSp.setLayoutManager(new GridLayoutManager(this, 2));
        }
        rvChangeSp.setHasFixedSize(true);
        List<File> files = new ArrayList<>();
        for (int i = 0; i < sanPham.getListFiles().getListUrl().size(); i++) {
            files.add(new File(sanPham.getListFiles().getListUrl().get(i)));
        }
        if (files.size() == sanPham.getListFiles().getListUrl().size()) {
            mAdapter = new ImageAdapter(this, files, new ImageAdapter.OnClickItemImage() {
                @Override
                public void onClickImageDelete(int position) {

                }
            });
        }
        rvChangeSp.setAdapter(mAdapter);

        tvAccountName.setText(PreferManager.getNameAccount(this));
        edtHeader.setText(sanPham.getHeader());
        edtMota.setText(sanPham.getMota());
        edtGia.setText(sanPham.getGia());
    }

    @OnClick(R.id.img_arrow_back)
    public void backActivity() {
        onBackPressed();
    }

    @OnClick(R.id.btn_luu)
    public void updateSp() {
        if (NetworkUtils.isConnected(this)) {
            GoogleMapUtils.getLatLongFromGivenAddress(this, PreferManager.getMyAddress(this));
            if (GoogleMapUtils.getLatLng() != null) {
                if (edtHeader.getText().length() < 255 && edtMota.getText().length() > 0) {
                    mPresenter.updateSanPham(mDataBase, new SanPham(sanPham.getIdNguoiban(),
                             sanPham.getNameNguoiBan(),
                             sanPham.getIdSanPham(),
                             sanPham.getIdCategory(),
                             sanPham.getIdPart(),
                             edtHeader.getText().toString().trim(),
                             edtMota.getText().toString().trim(),
                             sanPham.getTime(),
                             sanPham.getListFiles(),
                             edtGia.getText().toString().trim(),
                             PreferManager.getMyAddress(this),
                             GoogleMapUtils.getLatLng().latitude,
                             GoogleMapUtils.getLatLng().longitude));
                } else {
                    showSnackbar(getString(R.string.error_and_check));
                }


            } else {
                if (edtHeader.getText().length() < 255 && edtMota.getText().length() > 0) {
                    mPresenter.updateSanPham(mDataBase, new SanPham(sanPham.getIdNguoiban(),
                             sanPham.getNameNguoiBan(),
                             sanPham.getIdSanPham(),
                             sanPham.getIdCategory(),
                             sanPham.getIdPart(),
                             edtHeader.getText().toString().trim(),
                             edtMota.getText().toString().trim(),
                             sanPham.getTime(),
                             sanPham.getListFiles(),
                             edtGia.getText().toString().trim(),
                             PreferManager.getMyAddress(this)
                             , 0, 0));
                } else {
                    showSnackbar(getString(R.string.error_and_check));
                }
            }
        } else showNoInternet();


    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter.detach();
        super.onDestroy();
    }

    @Override
    public void updateSanPhamSuccess() {
        showSnackbar(getString(R.string.thanh_cong));
        startActivity(new Intent(ChangeSanPhamActivity.this, MySanPhamActivity.class));
        finish();
    }

    @Override
    public void updateSanPhamError() {
        showSnackbar(getString(R.string.error));
    }

    @Override
    public void updateListImageSuccess() {

    }

    @Override
    public void updateListImageError() {

    }
}
