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
import com.example.user.banhangonline.screen.spAccount.SanPhamAccountActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartDetail;
import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartSPAccount;

public class SanPhamDetailActivity extends BaseActivity implements SanPhamDetailContact.View {

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.img_lanscape)
    ImageView imgLanscape;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_account)
    ImageView imgAccount;

    @BindView(R.id.tv_account_name)
    TextView tvAccoutName;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.tv_mota)
    TextView tvMota;

    @BindView(R.id.recycerview_image_detail)
    RecyclerView rvImageDetail;

    private Unbinder unbinder;
    private SanPhamDetailPresenter mPresenter;
    private DetailAdapter mAdapter;
    private SanPham sanPham;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_detail);
        unbinder = ButterKnife.bind(this);
        initToolBar();
        setSupportActionBar(toolbar);
        mPresenter = new SanPhamDetailPresenter();
        mPresenter.attachView(this);
        sanPham = (SanPham) getIntent().getSerializableExtra(keyStartDetail);
        if (sanPham != null) {
            initAdapter();
        }
    }

    private void initToolBar() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
    }

    private void initAdapter() {
        mPresenter.getInfomationWithIdAccount(mDataBase, sanPham.getIdNguoiban());
        tvTime.setText(sanPham.getTime());
        tvMota.setText(sanPham.getHeader() + " - " + sanPham.getMota());

        rvImageDetail.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DetailAdapter(this, sanPham.getListFiles().getListUrl());
        rvImageDetail.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void getInfoMationSuccess(Account account) {
        Glide.with(this).load(account.getUrlAvt()).error(R.drawable.ic_product).into(imgAccount);
        Glide.with(this).load(account.getUrlLanscape()).error(R.drawable.bg_app).into(imgLanscape);
        tvAccoutName.setText(account.getName());
        toolbar.setTitle(account.getName());
    }

    @OnClick(R.id.btn_mua_hang)
    public void clickMuaHang(){

    }

    @OnClick(R.id.img_account)
    public void startAccount(){
        if (sanPham != null) {
            if (sanPham.getIdNguoiban() != null) {
                Intent intent = new Intent(SanPhamDetailActivity.this, SanPhamAccountActivity.class);
                intent.putExtra(keyStartSPAccount, sanPham.getIdNguoiban());
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
}
