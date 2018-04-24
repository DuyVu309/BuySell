package com.example.user.banhangonline.screen.myAccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.user.banhangonline.R;
import com.example.user.banhangonline.base.BaseActivity;
import com.example.user.banhangonline.interactor.prefer.PreferManager;
import com.example.user.banhangonline.model.SanPham;
import com.example.user.banhangonline.screen.changeSanpham.ChangeSanPhamActivity;
import com.example.user.banhangonline.screen.home.HomeActivity;
import com.example.user.banhangonline.screen.myAccount.adapter.ListImagesCartAdapter;
import com.example.user.banhangonline.screen.myAccount.adapter.SanPhamMyAccountAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.user.banhangonline.untils.KeyPreferUntils.keyStartSP;

public class MyAccountActivity extends BaseActivity implements
         MyAccountContact.View,
         SanPhamMyAccountAdapter.IOnClickSanphamAdapter,
         ListImagesCartAdapter.IOnClickImageMyAccount {

    @BindView(R.id.recycerview_my_account)
    RecyclerView rvMyAccount;

    private SanPhamMyAccountAdapter mAdapter;
    private Unbinder unbinder;
    private MyAccountPresenter mPresenter;

    @Override
    public boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        unbinder = ButterKnife.bind(this);
        mPresenter = new MyAccountPresenter();
        mPresenter.onCreate();
        mPresenter.attachView(this);
        mPresenter.setContext(this);
        initAdapter();
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
        startActivity(new Intent(MyAccountActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        mPresenter.detach();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void getListSuccess() {
        mAdapter.notifyDataSetChanged();
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
    public void onClickSanPham(SanPham sanPham) {
        if (sanPham != null) {
            Intent intent = new Intent(MyAccountActivity.this, ChangeSanPhamActivity.class);
            intent.putExtra(keyStartSP, sanPham);
            startActivity(intent);
        }
    }

    @Override
    public void onChangeSanpham(SanPham sanPham) {
        if (sanPham != null) {
            Intent intent = new Intent(MyAccountActivity.this, ChangeSanPhamActivity.class);
            intent.putExtra(keyStartSP, sanPham);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDeleteSanPham(SanPham sanPham) {
        if (sanPham != null) {
            if (sanPham.getIdNguoiban().equals(PreferManager.getEmailID(this))) {
                showDialog();
                mPresenter.deleteSanPhamMyAccount(mDataBase, mStorageReferrence, sanPham);
            }
        }
    }

    @Override
    public void onClickImageMyAccount() {

    }
}
