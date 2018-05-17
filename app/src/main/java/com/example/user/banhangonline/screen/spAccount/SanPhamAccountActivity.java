package com.example.user.banhangonline.screen.spAccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.detail.SanPhamDetailActivity;
import com.example.user.banhangonline.screen.spAccount.adapter.SanPhamAccountAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartDetail;
import static com.example.user.banhangonline.utils.KeyPreferUntils.keyStartSPAccount;

public class SanPhamAccountActivity extends BaseActivity implements SanPhamAccountContact.View {

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
        mPresenter.setEmailId( getIntent().getStringExtra(keyStartSPAccount));
        if (mPresenter.getEmailId() != null) {
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


    @Override
    public void getSanPhamSuccess() {
        if (mPresenter.getSanPhamList().size() == 0) {
            showSnackbar(getString(R.string.dont_have_product));

        }
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
