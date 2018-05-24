package com.example.user.banhangonline.screen.spAccount;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.detail.SanPhamDetailActivity;
import com.example.user.banhangonline.screen.mySanPham.MySanPhamActivity;
import com.example.user.banhangonline.screen.showImage.ShowImageActivity;
import com.example.user.banhangonline.screen.spAccount.adapter.SanPhamAccountAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartDetail;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartSPAccount;
import static com.example.user.banhangonline.utils.KeyUntils.keyShowImage;

public class SanPhamAccountActivity extends BaseActivity implements SanPhamAccountContact.View {

    @BindView(R.id.appbar_account)
    AppBarLayout appBarLayout;

    @BindView(R.id.img_my_account)
    ImageView imgMyLanscape;

    @BindView(R.id.img_avt)
    ImageView imgAvt;

    @BindView(R.id.tv_account_name)
    TextView tvAccountName;

    @BindView(R.id.tv_account_phone)
    TextView tvAccountPhone;

    @BindView(R.id.tv_account_address)
    TextView tvAccountAddress;

    @BindView(R.id.recycerview)
    RecyclerView rvSanPhamAccount;

    SanPhamAccountPresenter mPresenter;
    SanPhamAccountAdapter mAdapter;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_account);
        ButterKnife.bind(this);
        mPresenter = new SanPhamAccountPresenter();
        mPresenter.attachView(this);
        mPresenter.setEmailId(getIntent().getStringExtra(keyStartSPAccount));
        if (mPresenter.getEmailId() != null) {
            mPresenter.getInfoAccount(mDataBase, mPresenter.getEmailId());
            iniAdapter();
        }
    }

    private void iniAdapter() {
        showDialog();
        mPresenter.getSanPhamAccount(mDataBase, mPresenter.getEmailId());
        rvSanPhamAccount.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new SanPhamAccountAdapter(this, mPresenter.getSanPhamList(), new SanPhamAccountAdapter.IOnClickSanPham() {
            @Override
            public void onClickSanPham(SanPham sanPham) {
                Intent intent = new Intent(SanPhamAccountActivity.this, SanPhamDetailActivity.class);
                intent.putExtra(keyStartDetail, sanPham);
                startActivity(intent);
                finish();
            }
        });

        rvSanPhamAccount.setAdapter(mAdapter);
    }

    @OnClick(R.id.img_avt)
    public void showImageAvt() {
        Intent intent = new Intent(SanPhamAccountActivity.this, ShowImageActivity.class);
        intent.putExtra(keyShowImage, mPresenter.getAccount().getUrlAvt());
        startActivity(intent);
    }

    @OnClick(R.id.img_my_account)
    public void showImageLans() {
        Intent intent = new Intent(SanPhamAccountActivity.this, ShowImageActivity.class);
        intent.putExtra(keyShowImage, mPresenter.getAccount().getUrlLanscape());
        startActivity(intent);
    }

    @Override
    public void getInfoSuccess() {
        if (mPresenter.getAccount() != null) {
            Account account = mPresenter.getAccount();
            if (account.getName() != null) {
                tvAccountName.setText(account.getName());
            }

            if (account.getAddress() != null) {
                tvAccountAddress.setText(account.getAddress());
            } else {
                tvAccountAddress.setText(getString(R.string.dont_address));
                tvAccountAddress.setTextColor(Color.RED);
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
    }

    @Override
    public void getInfoError() {
        appBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void getSanPhamSuccess() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            dismissDialog();
        }
    }

    @Override
    public void getSanPhamError() {
        showSnackbar(getString(R.string.error_retry));
        dismissDialog();
    }

    @OnClick(R.id.img_arrow_back)
    public void backActivity() {
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }
}
