package com.example.user.banhangonline.screen.detail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.Account;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.detail.adapter.DetailAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartDetail;

public class SanPhamDetailActivity extends BaseActivity implements SanPhamDetailContact.View {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_detail);
        unbinder = ButterKnife.bind(this);
        mPresenter = new SanPhamDetailPresenter();
        mPresenter.attachView(this);
        sanPham = (SanPham) getIntent().getSerializableExtra(keyStartDetail);
        if (sanPham != null) {
            initAdapter();
        }
    }

    private void initAdapter() {
        mPresenter.getInfomationWithIdAccount(mDataBase, sanPham.getIdNguoiban());
        tvTime.setText(sanPham.getTime());
        tvMota.setText(sanPham.getHeader() + " - " + sanPham.getMota());

        rvImageDetail.setLayoutManager(new LinearLayoutManager(this));
        rvImageDetail.setHasFixedSize(true);
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
        Glide.with(this).load(account.getUrlImage()).error(R.drawable.ic_account).into(imgAccount);
        tvAccoutName.setText(account.getName());
    }

    @Override
    public void getInfomationError() {

    }
}
