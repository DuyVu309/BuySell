package com.example.user.banhangonline.screen.detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.detail.adapter.DetailAdapter;
import com.example.user.banhangonline.screen.thanhToan.ThanhToanActivity;
import com.example.user.banhangonline.screen.spAccount.SanPhamAccountActivity;
import com.example.user.banhangonline.utils.NetworkUtils;
import com.example.user.banhangonline.views.swipe.SwipeBackLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartDetail;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartPhone;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartSPAccount;

public class SanPhamDetailActivity extends BaseActivity implements SanPhamDetailContact.View {
    @BindView(R.id.img_lanscape)
    ImageView imgLanscape;

    @BindView(R.id.img_account)
    ImageView imgAccount;

    @BindView(R.id.tv_account_name)
    TextView tvAccoutName;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.tv_mota)
    TextView tvMota;

    @BindView(R.id.tv_gia)
    TextView tvGia;

    @BindView(R.id.recycerview_image_detail)
    RecyclerView rvImageDetail;

    private SanPhamDetailPresenter mPresenter;
    private DetailAdapter mAdapter;
    private String phone;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_detail);
        ButterKnife.bind(this);
        mPresenter = new SanPhamDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.setSanPham((SanPham) getIntent().getSerializableExtra(keyStartDetail));
        if (mPresenter.getSanPham() != null) {
            initAdapter();
        }
    }


    private void initAdapter() {
        mPresenter.getInfomationWithIdAccount(mDataBase, mPresenter.getSanPham().getIdNguoiban());
        tvTime.setText(mPresenter.getSanPham().getTime());
        tvMota.setText(mPresenter.getSanPham().getHeader() + " - " + mPresenter.getSanPham().getMota());
        tvGia.setText(mPresenter.getSanPham().getGia());

        mAdapter = new DetailAdapter(this, mPresenter.getSanPham().getListFiles().getListUrl());
        rvImageDetail.setHasFixedSize(true);
        rvImageDetail.setLayoutManager(new LinearLayoutManager(this));
        rvImageDetail.setNestedScrollingEnabled(false);
        rvImageDetail.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

    @Override
    public void getInfoMationSuccess(Account account) {
        phone = account.getPhoneNumber();
        Glide.with(this).load(account.getUrlAvt()).error(R.drawable.ic_product).into(imgAccount);
        Glide.with(this).load(account.getUrlLanscape()).error(R.drawable.bg_app).into(imgLanscape);
        tvAccoutName.setText(account.getName());
    }

    @OnClick(R.id.btn_mua_hang)
    public void clickMuaHang(){
        if (NetworkUtils.isConnected(SanPhamDetailActivity.this)) {
            if (mPresenter.getSanPham() != null) {
                Intent intent = new Intent(SanPhamDetailActivity.this, ThanhToanActivity.class);
                intent.putExtra(keyStartDetail, mPresenter.getSanPham());
                if (phone != null) {
                    intent.putExtra(keyStartPhone, phone);
                }
                startActivity(intent);
            }
        } else {
            showNoInternet();
        }
    }

    @OnClick(R.id.img_account)
    public void startAccount(){
        if (mPresenter.getSanPham() != null) {
            if (mPresenter.getSanPham().getIdNguoiban() != null) {
                Intent intent = new Intent(SanPhamDetailActivity.this, SanPhamAccountActivity.class);
                intent.putExtra(keyStartSPAccount, mPresenter.getSanPham().getIdNguoiban());
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void getInfomationError() {
        showSnackbar(getString(R.string.error));
    }


    @OnClick(R.id.img_arrow_back)
    public void backActivity() {
        finish();
    }
}
